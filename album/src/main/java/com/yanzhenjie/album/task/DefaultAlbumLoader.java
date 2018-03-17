/*
 * Copyright © 2017 Yan Zhenjie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanzhenjie.album.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;
import com.yanzhenjie.album.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>LRU Cache.</p>
 * Created by Yan Zhenjie on 2017/3/25.
 */
public class DefaultAlbumLoader implements AlbumLoader {

    private static final String TAG = DefaultAlbumLoader.class.getSimpleName();

    private static Drawable sErrorDrawable = new ColorDrawable(Color.parseColor("#FF2B2B2B"));
    private static Drawable sPlaceHolderDrawable = new ColorDrawable(Color.parseColor("#FF2B2B2B"));

    /**
     * Single module.
     */
    private static DefaultAlbumLoader mInstance;
    /**
     * Post message.
     */
    private static Handler instanceHandler;

    /**
     * Get single object.
     *
     * @return {@link DefaultAlbumLoader}.
     */
    public static DefaultAlbumLoader getInstance() {
        if (mInstance == null)
            synchronized (DefaultAlbumLoader.class) {
                if (mInstance == null)
                    mInstance = new DefaultAlbumLoader();
            }
        return mInstance;
    }

    /**
     * Get single handler.
     *
     * @return {@link Handler}.
     */
    private static Handler getHandler() {
        if (instanceHandler == null)
            synchronized (DefaultAlbumLoader.class) {
                if (instanceHandler == null)
                    instanceHandler = new Handler(Looper.getMainLooper());
            }
        return instanceHandler;
    }

    public static void setPlaceHolderDrawable(Drawable drawable) {
        if (drawable != null)
            sPlaceHolderDrawable = drawable;
    }

    /**
     * Set the default placeholder.
     */
    public static void setErrorDrawable(Drawable drawable) {
        if (drawable != null)
            sErrorDrawable = drawable;
    }

    /**
     * Image cache.
     */
    private final LruCache<String, Bitmap> mLruCache;
    /**
     * Thread pool.
     */
    private final ExecutorService mExecutorService;

    private DefaultAlbumLoader() {
        this.mExecutorService = Executors.newFixedThreadPool(6);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 4);
        mLruCache = new LruCache<String, Bitmap>(maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    // ---------- Interface Impl ---------- //

    @Override
    public void loadAlbumFile(ImageView imageView, AlbumFile albumFile, int viewWidth, int viewHeight) {
        if (albumFile.getMediaType() == AlbumFile.TYPE_IMAGE) {
            loadImage(imageView, albumFile.getPath(), viewWidth, viewHeight);
        } else {
            loadVideo(imageView, albumFile.getPath(), viewWidth, viewHeight);
        }
    }

    @Override
    public void loadImage(ImageView imageView, String imagePath, int viewWidth, int viewHeight) {
        imageView.setTag(R.id.album_image_load_tag, imagePath);
        Bitmap bitmap = getImageFromCache(imagePath, viewWidth, viewHeight);
        if (bitmap == null) {
            imageView.setImageDrawable(sPlaceHolderDrawable);
            mExecutorService.execute(new LoadImageTask(this, imageView, imagePath, viewWidth, viewHeight));
        } else {
            BitmapHolder holder = new BitmapHolder();
            holder.mImageView = imageView;
            holder.mTargetPath = imagePath;
            holder.mBitmap = bitmap;
            getHandler().post(holder);
        }
    }

    public void loadVideo(ImageView imageView, String videoPath, int viewWidth, int viewHeight) {
        imageView.setTag(R.id.album_image_load_tag, videoPath);
        Bitmap bitmap = getImageFromCache(videoPath, viewWidth, viewHeight);
        if (bitmap == null) {
            imageView.setImageDrawable(sPlaceHolderDrawable);
            mExecutorService.execute(new LoadVideoTask(this, imageView, videoPath, viewWidth, viewHeight));
        } else {
            BitmapHolder holder = new BitmapHolder();
            holder.mImageView = imageView;
            holder.mTargetPath = videoPath;
            holder.mBitmap = bitmap;
            getHandler().post(holder);
        }
    }

    // ---------- Load Task ---------- //

    private static class LoadVideoTask implements Runnable {

        private DefaultAlbumLoader mLoader;
        private ImageView mImageView;
        private String mFilePath;
        private int mViewWidth;
        private int mViewHeight;

        LoadVideoTask(DefaultAlbumLoader loader, ImageView imageView, String filePath, int viewWidth, int viewHeight) {
            this.mLoader = loader;
            this.mFilePath = filePath;
            this.mImageView = imageView;
            this.mViewWidth = viewWidth;
            this.mViewHeight = viewHeight;
        }

        @Override
        public void run() {
            String thumbnail = new ThumbnailBuilder(mImageView.getContext()).createThumbnailForVideo(mFilePath);
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeFile(thumbnail);
            } catch (Exception e) {
                Log.w(TAG, "Load thumbnail error, the path of file: " + mFilePath, e);
            }
            postResult(bitmap);
        }

        private void postResult(Bitmap result) {
            mLoader.addImageToCache(mFilePath, mViewWidth, mViewHeight, result);
            BitmapHolder holder = new BitmapHolder();
            holder.mBitmap = result;
            holder.mImageView = mImageView;
            holder.mTargetPath = mFilePath;
            getHandler().post(holder);
        }
    }

    private static class LoadImageTask implements Runnable {

        private DefaultAlbumLoader mLoader;
        private ImageView mImageView;
        private String mImagePath;
        private int mViewWidth;
        private int mViewHeight;

        LoadImageTask(DefaultAlbumLoader loader, ImageView imageView, String imagePath, int viewWidth, int viewHeight) {
            this.mLoader = loader;
            this.mImagePath = imagePath;
            this.mImageView = imageView;
            this.mViewWidth = viewWidth;
            this.mViewHeight = viewHeight;
        }

        @Override
        public void run() {
            Bitmap bitmap = readImageFromPath(mImagePath, mViewWidth, mViewHeight);
            postResult(bitmap);
        }

        private void postResult(Bitmap result) {
            mLoader.addImageToCache(mImagePath, mViewWidth, mViewHeight, result);
            BitmapHolder holder = new BitmapHolder();
            holder.mBitmap = result;
            holder.mImageView = mImageView;
            holder.mTargetPath = mImagePath;
            getHandler().post(holder);
        }
    }

    /**
     * Deposit in the province read images, mViewWidth is high, the greater the picture clearer, but also the memory.
     *
     * @param imagePath pictures in the path of the memory card.
     * @param maxWidth  the highest limit value target mViewWidth.
     * @param maxHeight the highest limit value target mViewHeight.
     * @return Bitmap.
     */
    public static Bitmap readImageFromPath(String imagePath, int maxWidth, int maxHeight) {
        File imageFile = new File(imagePath);
        if (imageFile.exists()) {
            BufferedInputStream inputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(imageFile));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();

                options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
                options.inJustDecodeBounds = false;

                Bitmap decodeSampledBitmap = null;

                boolean decodeAttemptSuccess = false;
                while (!decodeAttemptSuccess) {
                    inputStream = new BufferedInputStream(new FileInputStream(imageFile));
                    try {
                        decodeSampledBitmap = BitmapFactory.decodeStream(inputStream, null, options);
                        decodeAttemptSuccess = true;
                    } catch (Exception e) {
                        options.inSampleSize *= 2;
                    }
                    inputStream.close();
                }

                if (imagePath.endsWith(".jpg")
                        || imagePath.endsWith(".JPG")
                        || imagePath.endsWith(".jpeg")
                        || imagePath.endsWith(".JPEG")) {
                    int degrees = readDegree(imagePath);
                    if (degrees > 0) {
                        Matrix matrix = new Matrix();
                        matrix.setRotate(degrees, decodeSampledBitmap.getWidth() / 2, decodeSampledBitmap.getHeight() / 2);
                        decodeSampledBitmap = Bitmap.createBitmap(
                                decodeSampledBitmap,
                                0,
                                0,
                                decodeSampledBitmap.getWidth(),
                                decodeSampledBitmap.getHeight(),
                                matrix,
                                true);
                    }
                }
                return decodeSampledBitmap;
            } catch (Exception ignored) {
            } finally {
                if (inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
        return null;
    }

    /**
     * Calculate inSampleSize of options.
     *
     * @param options   options.
     * @param reqWidth  request mViewWidth.
     * @param reqHeight request mViewHeight.
     * @return inSampleSize.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            while ((height / inSampleSize) > reqHeight || (width / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * Read the rotation angle of the picture file.
     *
     * @param path image path.
     * @return one of 0, 90, 180, 270.
     */
    public static int readDegree(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    // ----------------------------- General --------------------------- //

    private Bitmap getImageFromCache(String path, int width, int height) {
        synchronized (mLruCache) {
            return mLruCache.get(path + width + height);
        }
    }

    private void addImageToCache(String path, int width, int height, Bitmap bitmap) {
        if (getImageFromCache(path, width, height) == null && bitmap != null)
            synchronized (mLruCache) {
                mLruCache.put(path + width + height, bitmap);
            }
    }

    private static class BitmapHolder implements Runnable {
        Bitmap mBitmap;
        ImageView mImageView;
        String mTargetPath;

        @Override
        public void run() {
            if (TextUtils.isEmpty(mTargetPath)) {
                mImageView.setImageDrawable(sErrorDrawable);
            } else if (mTargetPath.equals(mImageView.getTag(R.id.album_image_load_tag))) {
                if (mBitmap == null) {
                    mImageView.setImageDrawable(sErrorDrawable);
                } else {
                    mImageView.setImageBitmap(mBitmap);
                }
            }
        }
    }
}

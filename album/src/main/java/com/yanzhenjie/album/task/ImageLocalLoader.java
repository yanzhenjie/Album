/*
 * Copyright © Yan Zhenjie. All Rights Reserved
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

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
public class ImageLocalLoader {

    public static final Drawable DEFAULT_DRAWABLE = new ColorDrawable(Color.parseColor("#FF2B2B2B"));

    /**
     * Single module.
     */
    private static ImageLocalLoader mInstance;
    /**
     * Post message.
     */
    private static Handler instanceHandler;
    /**
     * Image cache.
     */
    private final LruCache<String, Bitmap> mLruCache;
    /**
     * Thread pool.
     */
    private final ExecutorService mExecutorService;

    /**
     * Get single object.
     *
     * @return {@link ImageLocalLoader}.
     */
    public static ImageLocalLoader getInstance() {
        if (mInstance == null)
            synchronized (ImageLocalLoader.class) {
                if (mInstance == null)
                    mInstance = new ImageLocalLoader();
            }
        return mInstance;
    }

    private ImageLocalLoader() {
        mExecutorService = Executors.newFixedThreadPool(6);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 4);
        mLruCache = new LruCache<String, Bitmap>(maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    /**
     * Load image from local SDCard.
     *
     * @param imageView {@link ImageView}.
     * @param imagePath path.
     */
    public void loadImage(ImageView imageView, String imagePath) {
        loadImage(imageView, imagePath, 0, 0, null);
    }


    /**
     * According to the specified width high loading pictures, wide high, the greater the picture clearer, more memory.
     *
     * @param imageView {@link ImageView}.
     * @param imagePath path from local SDCard.
     * @param width     target width.
     * @param height    target height.
     */
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        loadImage(imageView, imagePath, width, height, null);
    }

    /**
     * According to the specified width high loading pictures, wide high, the greater the picture clearer, more memory.
     *
     * @param imageView    {@link ImageView}.
     * @param imagePath    path from local SDCard.
     * @param width        target width.
     * @param height       target height.
     * @param loadListener {@link LoadListener}.
     */
    public void loadImage(ImageView imageView, String imagePath, int width, int height, LoadListener loadListener) {
        imageView.setTag(imagePath);
        Bitmap bitmap = getImageFromCache(imagePath, width, height);
        if (bitmap == null) {
            imageView.setImageDrawable(DEFAULT_DRAWABLE);
            mExecutorService.execute(new TaskThread(this, imageView, imagePath, width, height, loadListener));
        } else {
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.imageView = imageView;
            holder.imagePath = imagePath;
            holder.bitmap = bitmap;
            holder.loadListener = loadListener;
            getHandler().post(holder);
        }
    }

    private Bitmap getImageFromCache(String key, int width, int height) {
        synchronized (mLruCache) {
            return mLruCache.get(key + width + height);
        }
    }

    private void addImageToCache(String key, int width, int height, Bitmap bitmap) {
        if (getImageFromCache(key, width, height) == null && bitmap != null)
            synchronized (mLruCache) {
                mLruCache.put(key, bitmap);
            }
    }

    private static class TaskThread implements Runnable {

        private ImageLocalLoader loader;
        private ImageView mImageView;
        private String mImagePath;
        private int width;
        private int height;
        private LoadListener loadListener;

        TaskThread(ImageLocalLoader loader, ImageView imageView, String imagePath, int width, int height, LoadListener
                loadListener) {
            this.loader = loader;
            this.mImagePath = imagePath;
            this.mImageView = imageView;
            this.width = width;
            this.height = height;
            this.loadListener = loadListener;
        }

        @Override
        public void run() {
            if (TextUtils.isEmpty(mImagePath))
                Log.e("Album", "The image path is null");
            else {
                Bitmap bitmap;
                if (width != 0 && height != 0)
                    bitmap = readImage(mImagePath, width, height);
                else {
                    int[] viewSizes = new int[2];
                    measureSize(mImageView, viewSizes);
                    bitmap = readImage(mImagePath, viewSizes[0], viewSizes[1]);
                }
                loader.addImageToCache(mImagePath, width, height, bitmap);
                ImgBeanHolder holder = new ImgBeanHolder();
                holder.bitmap = bitmap;
                holder.imageView = mImageView;
                holder.imagePath = mImagePath;
                holder.loadListener = loadListener;
                getHandler().post(holder);
            }
        }
    }

    private static class ImgBeanHolder implements Runnable {
        Bitmap bitmap;
        ImageView imageView;
        String imagePath;
        LoadListener loadListener;

        @Override
        public void run() {
            if (imagePath.equals(imageView.getTag())) {
                if (bitmap == null) {
                    imageView.setImageDrawable(DEFAULT_DRAWABLE);
                } else {
                    if (loadListener == null)
                        imageView.setImageBitmap(bitmap);
                    else
                        loadListener.onLoadFinish(bitmap, imageView, imagePath);
                }
            }
        }
    }

    /**
     * Deposit in the province read images, width is high, the greater the picture clearer, but also the memory.
     *
     * @param imagePath pictures in the path of the memory card.
     * @param maxWidth  the highest limit value target width.
     * @param maxHeight the highest limit value target height.
     * @return Bitmap.
     */
    private static Bitmap readImage(String imagePath, int maxWidth, int maxHeight) {
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
     * @param reqWidth  request width.
     * @param reqHeight request height.
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
     * According to the ImageView obtains appropriate width and height of compression.
     *
     * @param imageView {@link ImageView}.
     * @param viewSizes ViewSize.
     */
    public static void measureSize(ImageView imageView, int[] viewSizes) {
        final DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        final LayoutParams params = imageView.getLayoutParams();
        if (params == null) {
            viewSizes[0] = displayMetrics.widthPixels;
            viewSizes[1] = displayMetrics.heightPixels;
        } else {
            viewSizes[0] = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView.getMeasuredWidth(); // Get actual image width
            viewSizes[1] = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView.getMeasuredWidth(); // Get actual image
            // height

            if (viewSizes[0] <= 0)
                viewSizes[0] = displayMetrics.widthPixels; // Get layout width parameter
            if (viewSizes[1] <= 0)
                viewSizes[1] = displayMetrics.heightPixels; // Get layout height parameter
        }
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

    public interface LoadListener {
        void onLoadFinish(Bitmap bitmap, ImageView imageView, String imagePath);
    }

    private static Handler getHandler() {
        if (instanceHandler == null)
            synchronized (ImageLocalLoader.class) {
                if (instanceHandler == null)
                    instanceHandler = new Handler(Looper.getMainLooper());
            }
        return instanceHandler;
    }

}

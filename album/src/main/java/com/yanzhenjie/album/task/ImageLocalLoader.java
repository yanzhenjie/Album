/*
 * Copyright 2015 Yan Zhenjie
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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
 * Created in Nov 4, 2015 3:07:29 PM.
 *
 * @author Yan Zhenjie.
 */
public class ImageLocalLoader {

    public static final Drawable DEFAULT_DRAWABLE = new ColorDrawable(Color.parseColor("#FF2B2B2B"));

    /**
     * Single module.
     */
    private static ImageLocalLoader mInstance;
    /**
     * Image cache.
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * Thread pool.
     */
    private ExecutorService mExecutorService;

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
        mExecutorService = Executors.newFixedThreadPool(5);

        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 8);
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
            mExecutorService.execute(new TaskThread(imageView, imagePath, width, height, loadListener));
        } else {
            ImgBeanHolder holder = new ImgBeanHolder();
            holder.imageView = imageView;
            holder.imagePath = imagePath;
            holder.bitmap = bitmap;
            holder.loadListener = loadListener;
            Poster.getInstance().post(holder);
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

    public Bitmap readImage(String imagePath, int maxWidth, int maxHeight) {
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
                return decodeSampledBitmap;
            } catch (IOException e) {
                e.printStackTrace();
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

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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
    private void measureSize(ImageView imageView, int[] viewSizes) {
        final DisplayMetrics displayMetrics = imageView.getContext().getResources().getDisplayMetrics();
        final LayoutParams params = imageView.getLayoutParams();
        if (params == null) {
            viewSizes[0] = displayMetrics.widthPixels;
            viewSizes[1] = displayMetrics.heightPixels;
        } else {
            viewSizes[0] = params.width == LayoutParams.WRAP_CONTENT ? 0 : imageView.getMeasuredWidth(); // Get actual image width
            viewSizes[1] = params.height == LayoutParams.WRAP_CONTENT ? 0 : imageView.getMeasuredWidth(); // Get actual image height

            if (viewSizes[0] <= 0)
                viewSizes[0] = displayMetrics.widthPixels; // Get layout width parameter
            if (viewSizes[1] <= 0)
                viewSizes[1] = displayMetrics.heightPixels; // Get layout height parameter
        }
    }

    private Bitmap getImageFromCache(String key, int width, int height) {
        return mLruCache.get(key + width + height);
    }

    private void addImageToCache(String key, int width, int height, Bitmap bitmap) {
        if (getImageFromCache(key, width, height) == null && bitmap != null)
            mLruCache.put(key, bitmap);
    }

    private class TaskThread implements Runnable {
        private ImageView mImageView;
        private String mImagePath;
        private int width;
        private int height;
        private LoadListener loadListener;

        TaskThread(ImageView imageView, String imagePath, int width, int height, LoadListener loadListener) {
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
                addImageToCache(mImagePath, width, height, bitmap);
                ImgBeanHolder holder = new ImgBeanHolder();
                holder.bitmap = bitmap;
                holder.imageView = mImageView;
                holder.imagePath = mImagePath;
                holder.loadListener = loadListener;
                Poster.getInstance().post(holder);
            }
        }
    }

    private class ImgBeanHolder implements Runnable {
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

    public interface LoadListener {
        void onLoadFinish(Bitmap bitmap, ImageView imageView, String imagePath);
    }
}

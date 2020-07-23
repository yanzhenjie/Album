/*
 * Copyright 2017 Yan Zhenjie.
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
package com.yanzhenjie.album.app.album.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.webkit.URLUtil;

import com.yanzhenjie.album.util.AlbumUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

/**
 * Created by YanZhenjie on 2017/10/15.
 */
public class ThumbnailBuilder {
    
    private static final int THUMBNAIL_SIZE = 360;
    private static final int THUMBNAIL_QUALITY = 80;
    
    private File mCacheDir;
    
    public ThumbnailBuilder(Context context) {
        this.mCacheDir = AlbumUtils.getAlbumRootPath(context);
        if (mCacheDir.exists() && mCacheDir.isFile()) {
            mCacheDir.delete();
        }
        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }
    
    /**
     * Create a thumbnail for the image.
     *
     * @param imageUri image path.
     * @return thumbnail path.
     */
    @WorkerThread
    @Nullable
    public Uri createThumbnailForImage(Context context, Uri imageUri, String mimeType) {
        if (imageUri == null || TextUtils.isEmpty(imageUri.toString())) {
            return null;
        }
        
        //        File inFile = new File(imagePath);
        //        if (!inFile.exists()) {
        //            return null;
        //        }
        
        File thumbnailFile = randomPath(imageUri.toString());
        if (thumbnailFile.exists()) {
            return AlbumUtils.getUri(context, thumbnailFile.getAbsolutePath());
        }
        
        Bitmap inBitmap = readImageFromPath(context, imageUri, THUMBNAIL_SIZE, THUMBNAIL_SIZE, mimeType);
        if (inBitmap == null) {
            return null;
        }
        
        ByteArrayOutputStream compressStream = new ByteArrayOutputStream();
        inBitmap.compress(Bitmap.CompressFormat.JPEG, THUMBNAIL_QUALITY, compressStream);
        
        try {
            compressStream.close();
            thumbnailFile.createNewFile();
            FileOutputStream writeStream = new FileOutputStream(thumbnailFile);
            writeStream.write(compressStream.toByteArray());
            writeStream.flush();
            writeStream.close();
            return AlbumUtils.getUri(context, thumbnailFile.getAbsolutePath());
        } catch (Exception ignored) {
            return null;
        }
    }
    
    /**
     * Create a thumbnail for the video.
     *
     * @param videoUri video path.
     * @return thumbnail path.
     */
    @WorkerThread
    @Nullable
    public Uri createThumbnailForVideo(Context context, Uri videoUri) {
        if (videoUri == null || TextUtils.isEmpty(videoUri.toString())) {
            return null;
        }
        
        File thumbnailFile = randomPath(videoUri.toString());
        if (thumbnailFile.exists()) {
            return AlbumUtils.getUri(context, thumbnailFile.getAbsolutePath());
        }
        
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            if (URLUtil.isNetworkUrl(videoUri.toString())) {
                retriever.setDataSource(videoUri.toString(), new HashMap<String, String>());
            } else {
                ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(videoUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                retriever.setDataSource(fileDescriptor);
            }
            Bitmap bitmap = retriever.getFrameAtTime();
            thumbnailFile.createNewFile();
            bitmap.compress(Bitmap.CompressFormat.JPEG, THUMBNAIL_QUALITY, new FileOutputStream(thumbnailFile));
            return AlbumUtils.getUri(context, thumbnailFile.getAbsolutePath());
        } catch (Exception ignored) {
            return null;
        }
    }
    
    private File randomPath(String filePath) {
        String outFilePath = AlbumUtils.getMD5ForString(filePath) + ".album";
        return new File(mCacheDir, outFilePath);
    }
    
    /**
     * Deposit in the province read images, mViewWidth is high, the greater the picture clearer, but also the memory.
     *
     * @param imageUri pictures in the path of the memory card.
     * @return bitmap.
     */
    @Nullable
    public static Bitmap readImageFromPath(Context context, Uri imageUri, int width, int height, String mimeType) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = context.getContentResolver().openFileDescriptor(imageUri, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
            options.inJustDecodeBounds = false;
            options.inSampleSize = computeSampleSize(options, width, height);
            Bitmap sampledBitmap = null;
            boolean attemptSuccess = false;
            while (!attemptSuccess ) {
                try {
                    sampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
                    attemptSuccess = true;
                } catch (Exception e) {
                    options.inSampleSize *= 2;
                }
            }
            
            if (mimeType.endsWith("jpg") || mimeType.endsWith("jpeg")) {
                int degrees = computeDegree(context, imageUri, fileDescriptor);
                if (degrees > 0) {
                    Matrix matrix = new Matrix();
                    matrix.setRotate(degrees);
                    Bitmap newBitmap = Bitmap.createBitmap(sampledBitmap, 0, 0, sampledBitmap.getWidth(), sampledBitmap.getHeight(), matrix, true);
                    if (newBitmap != sampledBitmap) {
                        sampledBitmap.recycle();
                        sampledBitmap = newBitmap;
                    }
                }
            }
            return sampledBitmap;
        } catch (Exception ignored) {
            ignored.printStackTrace();
        } finally {
            try {
                if (parcelFileDescriptor != null) {
                    parcelFileDescriptor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        return null;
    }
    
    private static int computeSampleSize(BitmapFactory.Options options, int width, int height) {
        int inSampleSize = 1;
        if (options.outWidth > width || options.outHeight > height) {
            int widthRatio = Math.round((float) options.outWidth / (float) width);
            int heightRatio = Math.round((float) options.outHeight / (float) height);
            inSampleSize = Math.min(widthRatio, heightRatio);
        }
        return inSampleSize;
    }
    
    private static int computeDegree(Context context, Uri imageUri, FileDescriptor fileDescriptor) {
        try {
            ExifInterface exifInterface = null;
            
            if (AlbumUtils.isBeforeAndroidTen()) {
                String filePath = MediaReader.getFilePathByUriBeforeAndroidQ(context, imageUri);
                if (!TextUtils.isEmpty(filePath)) {
                    exifInterface = new ExifInterface(filePath);
                }
            } else {
                exifInterface = new ExifInterface(fileDescriptor);
            }
            if (exifInterface != null) {
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90: {
                        return 90;
                    }
                    case ExifInterface.ORIENTATION_ROTATE_180: {
                        return 180;
                    }
                    case ExifInterface.ORIENTATION_ROTATE_270: {
                        return 270;
                    }
                    default: {
                        return 0;
                    }
                }
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}
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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.annotation.WorkerThread;

import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.util.AlbumUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YanZhenjie on 2017/8/15.
 */
public class MediaReader {
    
    private Context mContext;
    
    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;
    private boolean mFilterVisibility;
    
    public MediaReader(Context context, Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter, boolean filterVisibility) {
        this.mContext = context;
        
        this.mSizeFilter = sizeFilter;
        this.mMimeFilter = mimeFilter;
        this.mDurationFilter = durationFilter;
        this.mFilterVisibility = filterVisibility;
    }
    
    /**
     * Image attribute.
     */
    private static final String[] IMAGES = {
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.MIME_TYPE,
        MediaStore.Images.Media.DATE_ADDED,
        MediaStore.Images.Media.LATITUDE,
        MediaStore.Images.Media.LONGITUDE,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media._ID
    };
    
    /**
     * Scan for image files.
     */
    @WorkerThread
    private void scanImageFile(Map<String, AlbumFolder> albumFolderMap, AlbumFolder allFileFolder) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            IMAGES,
            null,
            null,
            null);
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    //Android Q 公有目录只能通过Content Uri + id的方式访问，以前的File路径全部无效，如果是Video，记得换成MediaStore.Videos
                    String id = cursor.getString(7);
                    Uri uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                    String path = cursor.getString(0);
                    String bucketName = cursor.getString(1);
                    String mimeType = cursor.getString(2);
                    long addDate = cursor.getLong(3);
                    float latitude = 0;
                    float longitude = 0;
                    if (AlbumUtils.isBeforeAndroidTen()) {
                        latitude = cursor.getFloat(4);
                        longitude = cursor.getFloat(5);
                    } else {
                        float[] latLong = getMediaLatLong(uri);
                        if (latLong.length > 1) {
                            latitude = latLong[0];
                            longitude = latLong[1];
                        }
                    }
                    long size = cursor.getLong(6);
                    
                    //                    if (!AlbumUtils.isBeforeAndroidTen()) {
                    //                        path = MediaStore.Images.Media
                    //                            .EXTERNAL_CONTENT_URI
                    //                            .buildUpon()
                    //                            .appendPath(String.valueOf(id)).build().toString();
                    //                    }
                    AlbumFile imageFile = new AlbumFile();
                    imageFile.setMediaType(AlbumFile.TYPE_IMAGE);
                    //                    imageFile.setPath(path);
                    imageFile.setUri(uri);
                    imageFile.setBucketName(bucketName);
                    imageFile.setMimeType(mimeType);
                    imageFile.setAddDate(addDate);
                    imageFile.setLatitude(latitude);
                    imageFile.setLongitude(longitude);
                    imageFile.setSize(size);
                    
                    if (mSizeFilter != null && mSizeFilter.filter(size)) {
                        if (!mFilterVisibility) {
                            continue;
                        }
                        imageFile.setDisable(true);
                    }
                    if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                        if (!mFilterVisibility) {
                            continue;
                        }
                        imageFile.setDisable(true);
                    }
                    
                    allFileFolder.addAlbumFile(imageFile);
                    AlbumFolder albumFolder = albumFolderMap.get(bucketName);
                    
                    if (albumFolder != null) {
                        albumFolder.addAlbumFile(imageFile);
                    } else {
                        albumFolder = new AlbumFolder();
                        albumFolder.setName(bucketName);
                        albumFolder.addAlbumFile(imageFile);
                        
                        albumFolderMap.put(bucketName, albumFolder);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            cursor.close();
        }
    }
    
    /**
     * Video attribute.
     */
    private static final String[] VIDEOS = {
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.LATITUDE,
        MediaStore.Video.Media.LONGITUDE,
        MediaStore.Video.Media.SIZE,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media._ID
    };
    
    /**
     * Scan for image files.
     */
    @WorkerThread
    private void scanVideoFile(Map<String, AlbumFolder> albumFolderMap, AlbumFolder allFileFolder) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            VIDEOS,
            null,
            null,
            null);
        
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(8);
                String path = cursor.getString(0);
                Uri uri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);
                String bucketName = cursor.getString(1);
                String mimeType = cursor.getString(2);
                long addDate = cursor.getLong(3);
                float latitude = 0f;
                float longitude = 0f;
                if (AlbumUtils.isBeforeAndroidTen()) {
                    latitude = cursor.getFloat(4);
                    longitude = cursor.getFloat(5);
                } else {
                    float[] latLong = getMediaLatLong(uri);
                    if (latLong != null && latLong.length > 1) {
                        latitude = latLong[0];
                        longitude = latLong[1];
                    }
                }
                long size = cursor.getLong(6);
                long duration = cursor.getLong(7);
                //                if (!AlbumUtils.isBeforeAndroidTen()) {
                //                    path = MediaStore.Video.Media
                //                        .EXTERNAL_CONTENT_URI
                //                        .buildUpon()
                //                        .appendPath(String.valueOf(id)).build().toString();
                //                }
                AlbumFile videoFile = new AlbumFile();
                videoFile.setMediaType(AlbumFile.TYPE_VIDEO);
                //                videoFile.setPath(path);
                videoFile.setUri(uri);
                videoFile.setBucketName(bucketName);
                videoFile.setMimeType(mimeType);
                videoFile.setAddDate(addDate);
                videoFile.setLatitude(latitude);
                videoFile.setLongitude(longitude);
                videoFile.setSize(size);
                videoFile.setDuration(duration);
                
                if (mSizeFilter != null && mSizeFilter.filter(size)) {
                    if (!mFilterVisibility) {
                        continue;
                    }
                    videoFile.setDisable(true);
                }
                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                    if (!mFilterVisibility) {
                        continue;
                    }
                    videoFile.setDisable(true);
                }
                if (mDurationFilter != null && mDurationFilter.filter(duration)) {
                    if (!mFilterVisibility) {
                        continue;
                    }
                    videoFile.setDisable(true);
                }
                
                allFileFolder.addAlbumFile(videoFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);
                
                if (albumFolder != null) {
                    albumFolder.addAlbumFile(videoFile);
                } else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(videoFile);
                    
                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
    }
    
    private float[] getMediaLatLong(Uri mediaUri) {
        float[] latLong = new float[2];
        if (!AlbumUtils.isBeforeAndroidTen()) {
            InputStream stream;
            try {
                // 从ExifInterface类获取位置信息
                mediaUri = MediaStore.setRequireOriginal(mediaUri);
                stream = mContext.getContentResolver().openInputStream(mediaUri);
                if (stream != null) {
                    ExifInterface exifInterface = new ExifInterface(stream);
                    exifInterface.getLatLong(latLong);
                    // Don't reuse the stream associated with the instance of "ExifInterface".
                    stream.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return latLong;
    }
    
    public static String getFilePathByUriBeforeAndroidQ(Context context, Uri uri) {
        String path = null;
        // 以 file:// 开头的
        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            path = uri.getPath();
            return path;
        }
        // 以 content:// 开头的，比如 content://media/extenral/images/media/17766
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    if (columnIndex > -1) {
                        path = cursor.getString(columnIndex);
                    }
                }
                cursor.close();
            }
            return path;
        }
        // 4.4及之后的 是以 content:// 开头的，比如 content://com.android.providers.media.documents/document/image%3A235700
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                if (isExternalStorageDocument(uri)) {
                    // ExternalStorageProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        path = Environment.getExternalStorageDirectory() + "/" + split[1];
                        return path;
                    }
                } else if (isDownloadsDocument(uri)) {
                    // DownloadsProvider
                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                    path = getDataColumn(context, contentUri, null, null);
                    return path;
                } else if (isMediaDocument(uri)) {
                    // MediaProvider
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{split[1]};
                    path = getDataColumn(context, contentUri, selection, selectionArgs);
                    return path;
                }
            }
        }
        return null;
    }
    
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
    
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    
    
    /**
     * Scan the list of pictures in the library.
     */
    @WorkerThread
    public ArrayList<AlbumFolder> getAllImage() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setChecked(true);
        allFileFolder.setName(mContext.getString(R.string.album_all_images));
        
        scanImageFile(albumFolderMap, allFileFolder);
        
        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getAlbumFiles());
        albumFolders.add(allFileFolder);
        
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAlbumFiles());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
    
    /**
     * Scan the list of videos in the library.
     */
    @WorkerThread
    public ArrayList<AlbumFolder> getAllVideo() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setChecked(true);
        allFileFolder.setName(mContext.getString(R.string.album_all_videos));
        
        scanVideoFile(albumFolderMap, allFileFolder);
        
        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getAlbumFiles());
        albumFolders.add(allFileFolder);
        
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAlbumFiles());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
    
    /**
     * Get all the multimedia files, including videos and pictures.
     */
    @WorkerThread
    public ArrayList<AlbumFolder> getAllMedia() {
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();
        AlbumFolder allFileFolder = new AlbumFolder();
        allFileFolder.setChecked(true);
        allFileFolder.setName(mContext.getString(R.string.album_all_images_videos));
        
        scanImageFile(albumFolderMap, allFileFolder);
        scanVideoFile(albumFolderMap, allFileFolder);
        
        ArrayList<AlbumFolder> albumFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getAlbumFiles());
        albumFolders.add(allFileFolder);
        
        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getAlbumFiles());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
    
}
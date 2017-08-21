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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.AnyThread;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumFolder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YanZhenjie on 2017/8/15.
 */
public class MediaReader {

    private Context mContext;

    @AnyThread
    public MediaReader(Context context) {
        mContext = context;
    }

    /**
     * Image attribute.
     */
    private static final String[] IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE
    };

    /**
     * Image thumb.
     */
    private static final String[] IMAGE_THUMB = {
            MediaStore.Images.Thumbnails.DATA
    };

    /**
     * Video attribute.
     */
    private static final String[] VIDEOS = {
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.BUCKET_ID,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.DATE_MODIFIED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.RESOLUTION
    };

    /**
     * Video thumb.
     */
    private static final String[] VIDEO_THUMB = {
            MediaStore.Video.Thumbnails.DATA
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
                MediaStore.Images.Media.DATE_ADDED);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(IMAGES[0]));
                String path = cursor.getString(cursor.getColumnIndex(IMAGES[1]));

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                String name = cursor.getString(cursor.getColumnIndex(IMAGES[2]));
                String title = cursor.getString(cursor.getColumnIndex(IMAGES[3]));
                int bucketId = cursor.getInt(cursor.getColumnIndex(IMAGES[4]));
                String bucketName = cursor.getString(cursor.getColumnIndex(IMAGES[5]));
                String mimeType = cursor.getString(cursor.getColumnIndex(IMAGES[6]));
                long addDate = cursor.getLong(cursor.getColumnIndex(IMAGES[7]));
                long modifyDate = cursor.getLong(cursor.getColumnIndex(IMAGES[8]));
                float latitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[9]));
                float longitude = cursor.getFloat(cursor.getColumnIndex(IMAGES[10]));
                long size = cursor.getLong(cursor.getColumnIndex(IMAGES[11]));

                AlbumFile imageFile = new AlbumFile();
                imageFile.setMediaType(AlbumFile.TYPE_IMAGE);
                imageFile.setId(id);
                imageFile.setPath(path);
                imageFile.setName(name);
                imageFile.setTitle(title);
                imageFile.setBucketId(bucketId);
                imageFile.setBucketName(bucketName);
                imageFile.setMimeType(mimeType);
                imageFile.setAddDate(addDate);
                imageFile.setModifyDate(modifyDate);
                imageFile.setLatitude(latitude);
                imageFile.setLongitude(longitude);
                imageFile.setSize(size);

                String thumbPath = null;
                Cursor thumbCursor = contentResolver.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                        IMAGE_THUMB,
                        MediaStore.Images.Thumbnails.IMAGE_ID + "=" + id,
                        null,
                        null);
                if (thumbCursor != null) {
                    if (thumbCursor.moveToFirst()) {
                        thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(IMAGE_THUMB[0]));
                    }
                    thumbCursor.close();
                }
                imageFile.setThumbPath(thumbPath);

                allFileFolder.addAlbumFile(imageFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);

                if (albumFolder != null)
                    albumFolder.addAlbumFile(imageFile);
                else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setId(bucketId);
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(imageFile);

                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
    }

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
                MediaStore.Video.Media.DATE_ADDED);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(VIDEOS[0]));
                String path = cursor.getString(cursor.getColumnIndex(VIDEOS[1]));

                File file = new File(path);
                if (!file.exists() || !file.canRead()) continue;

                String name = cursor.getString(cursor.getColumnIndex(VIDEOS[2]));
                String title = cursor.getString(cursor.getColumnIndex(VIDEOS[3]));
                int bucketId = cursor.getInt(cursor.getColumnIndex(VIDEOS[4]));
                String bucketName = cursor.getString(cursor.getColumnIndex(VIDEOS[5]));
                String mimeType = cursor.getString(cursor.getColumnIndex(VIDEOS[6]));
                long addDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[7]));
                long modifyDate = cursor.getLong(cursor.getColumnIndex(VIDEOS[8]));
                float latitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[9]));
                float longitude = cursor.getFloat(cursor.getColumnIndex(VIDEOS[10]));
                long size = cursor.getLong(cursor.getColumnIndex(VIDEOS[11]));
                long duration = cursor.getLong(cursor.getColumnIndex(VIDEOS[12]));
                String resolution = cursor.getString(cursor.getColumnIndex(VIDEOS[13]));

                AlbumFile videoFile = new AlbumFile();
                videoFile.setMediaType(AlbumFile.TYPE_VIDEO);
                videoFile.setId(id);
                videoFile.setPath(path);
                videoFile.setName(name);
                videoFile.setTitle(title);
                videoFile.setBucketId(bucketId);
                videoFile.setBucketName(bucketName);
                videoFile.setMimeType(mimeType);
                videoFile.setAddDate(addDate);
                videoFile.setModifyDate(modifyDate);
                videoFile.setLatitude(latitude);
                videoFile.setLongitude(longitude);
                videoFile.setSize(size);
                videoFile.setDuration(duration);

                String thumbPath = null;
                Cursor thumbCursor = contentResolver.query(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        VIDEO_THUMB,
                        MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id,
                        null,
                        null);
                if (thumbCursor != null) {
                    if (thumbCursor.moveToFirst()) {
                        thumbPath = thumbCursor.getString(thumbCursor.getColumnIndex(VIDEO_THUMB[0]));
                    }
                    thumbCursor.close();
                }
                videoFile.setThumbPath(thumbPath);

                int width = 0, height = 0;
                if (!TextUtils.isEmpty(resolution) && resolution.contains("x")) {
                    String[] resolutionArray = resolution.split("x");
                    width = Integer.valueOf(resolutionArray[0]);
                    height = Integer.valueOf(resolutionArray[1]);
                }
                videoFile.setWidth(width);
                videoFile.setHeight(height);

                allFileFolder.addAlbumFile(videoFile);
                AlbumFolder albumFolder = albumFolderMap.get(bucketName);

                if (albumFolder != null)
                    albumFolder.addAlbumFile(videoFile);
                else {
                    albumFolder = new AlbumFolder();
                    albumFolder.setId(bucketId);
                    albumFolder.setName(bucketName);
                    albumFolder.addAlbumFile(videoFile);

                    albumFolderMap.put(bucketName, albumFolder);
                }
            }
            cursor.close();
        }
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
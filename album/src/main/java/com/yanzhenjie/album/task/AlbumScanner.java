/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.task;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.entity.AlbumImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yan Zhenjie on 2016/10/14.
 */
public class AlbumScanner {

    private static AlbumScanner instance;

    private AlbumScanner() {
    }

    public static AlbumScanner getInstance() {
        if (instance == null)
            synchronized (AlbumScanner.class) {
                if (instance == null)
                    instance = new AlbumScanner();
            }
        return instance;
    }

    /**
     * 设置获取图片的属性
     */
    private static final String[] STORE_IMAGES = {
            /**
             * 图片ID。
            */
            MediaStore.Images.Media._ID,
            /**
             * 图片完整路径。
            */
            MediaStore.Images.Media.DATA,
            /**
             * 文件名称。
            */
            MediaStore.Images.Media.DISPLAY_NAME,
            /**
             * 被添加到库中的时间。
            */
            MediaStore.Images.Media.DATE_ADDED,
            /**
             * 目录ID。
            */
            MediaStore.Images.Media.BUCKET_ID,
            /**
             * 所在文件夹名称。
            */
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    /**
     * 获取文件夹列表。
     *
     * @param context {@link Context}.
     * @return {@code List<AlbumFolder>}.
     */
    public List<AlbumFolder> getPhotoAlbum(Context context) {
        Cursor cursor = MediaStore.Images.Media.query(context.getContentResolver(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
        Map<String, AlbumFolder> albumFolderMap = new HashMap<>();

        AlbumFolder allImageAlbumFolder = new AlbumFolder();
        allImageAlbumFolder.setChecked(true);
        allImageAlbumFolder.setName(context.getString(R.string.album_all_image));

        while (cursor.moveToNext()) {
            int imageId = cursor.getInt(0);
            String imagePath = cursor.getString(1);
            String imageName = cursor.getString(2);
            long addTime = cursor.getLong(3);

            int bucketId = cursor.getInt(4);
            String bucketName = cursor.getString(5);

            AlbumImage albumImage = new AlbumImage();
            albumImage.setId(imageId);
            albumImage.setPath(imagePath);
            albumImage.setName(imageName);
            albumImage.setAddTime(addTime);

            allImageAlbumFolder.addPhoto(albumImage);

            AlbumFolder albumFolder = albumFolderMap.get(bucketName);
            if (albumFolder != null) {
                albumFolder.addPhoto(albumImage);
            } else {
                albumFolder = new AlbumFolder();
                albumFolder.setId(bucketId);
                albumFolder.setName(bucketName);
                albumFolder.addPhoto(albumImage);

                albumFolderMap.put(bucketName, albumFolder);
            }
        }
        cursor.close();
        List<AlbumFolder> albumFolders = new ArrayList<>();

        Collections.sort(allImageAlbumFolder.getPhotos());
        albumFolders.add(allImageAlbumFolder);

        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getPhotos());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }

}

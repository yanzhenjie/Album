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

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.dialog.AlbumWaitDialog;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.entity.AlbumImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Image scan task.</p>
 * Created by Yan Zhenjie on 2017/3/28.
 */
public class ScanTask extends AsyncTask<List<String>, Void, List<AlbumFolder>> {

    /**
     * Media attribute.
     */
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID, // image id.
            MediaStore.Images.Media.DATA, // image absolute path.
            MediaStore.Images.Media.DISPLAY_NAME, // image name.
            MediaStore.Images.Media.DATE_ADDED, // The time to be added to the library.
            MediaStore.Images.Media.BUCKET_ID, // folder id.
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME // folder name.
    };

    public interface Callback {
        /**
         * AlbumCallback scan results.
         *
         * @param folders AlbumFolder list.
         */
        void onScanCallback(List<AlbumFolder> folders);
    }

    private Context mContext;
    private Callback mCallback;
    private List<AlbumImage> mCheckedImages;
    private AlbumWaitDialog mWaitDialog;

    public ScanTask(Context context, Callback callback, List<AlbumImage> checkedImages) {
        this.mContext = context;
        this.mCallback = callback;
        this.mCheckedImages = checkedImages;
        this.mWaitDialog = new AlbumWaitDialog(context);
    }

    @Override
    protected void onPreExecute() {
        if (!mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    @Override
    protected void onPostExecute(List<AlbumFolder> albumFolders) {
        if (mWaitDialog.isShowing()) mWaitDialog.dismiss();
        mCallback.onScanCallback(albumFolders);
    }

    @SafeVarargs
    @Override
    protected final List<AlbumFolder> doInBackground(List<String>... params) {
        List<AlbumFolder> folders = getPhotoAlbum(mContext);
        List<String> checkedList = params[0];
        if (checkedList != null && checkedList.size() > 0) {
            List<AlbumImage> images = folders.get(0).getImages();
            for (String path : checkedList) {
                for (int i = 0; i < images.size(); i++) {
                    AlbumImage image = images.get(i);
                    if (path.equals(image.getPath())) {
                        image.setChecked(true);
                        mCheckedImages.add(image);
                    }
                }
            }
        }
        return folders;
    }

    /**
     * Scan the list of pictures in the library.
     *
     * @param context {@link Context}.
     * @return {@code List<AlbumFolder>}.
     */
    private List<AlbumFolder> getPhotoAlbum(Context context) {
        Cursor cursor = MediaStore.Images.Media.query(
                context.getContentResolver(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, STORE_IMAGES);
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

            AlbumImage albumImage = new AlbumImage(imageId, imagePath, imageName, addTime);
            allImageAlbumFolder.addPhoto(albumImage);

            AlbumFolder albumFolder = albumFolderMap.get(bucketName);
            if (albumFolder != null)
                albumFolder.addPhoto(albumImage);
            else {
                albumFolder = new AlbumFolder(bucketId, bucketName);
                albumFolder.addPhoto(albumImage);

                albumFolderMap.put(bucketName, albumFolder);
            }
        }
        cursor.close();
        List<AlbumFolder> albumFolders = new ArrayList<>();

        Collections.sort(allImageAlbumFolder.getImages());
        albumFolders.add(allImageAlbumFolder);

        for (Map.Entry<String, AlbumFolder> folderEntry : albumFolderMap.entrySet()) {
            AlbumFolder albumFolder = folderEntry.getValue();
            Collections.sort(albumFolder.getImages());
            albumFolders.add(albumFolder);
        }
        return albumFolders;
    }
}

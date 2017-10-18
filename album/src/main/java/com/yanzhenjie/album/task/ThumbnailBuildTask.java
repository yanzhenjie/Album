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

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;

import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.loading.dialog.LoadingDialog;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/10/15.
 */
public class ThumbnailBuildTask extends AsyncTask<Void, Void, ArrayList<AlbumFile>> {

    private ArrayList<AlbumFile> mAlbumFiles;
    private Callback mCallback;

    private Dialog mDialog;
    private ThumbnailBuilder mThumbnailBuilder;

    public ThumbnailBuildTask(Context context, ArrayList<AlbumFile> albumFiles, Callback callback) {
        this.mAlbumFiles = albumFiles;
        this.mCallback = callback;

        this.mDialog = new LoadingDialog(context);
        this.mThumbnailBuilder = new ThumbnailBuilder(context);
    }

    @Override
    protected void onPreExecute() {
        if (!mDialog.isShowing())
            mDialog.show();
    }

    @Override
    protected ArrayList<AlbumFile> doInBackground(Void... params) {
        for (AlbumFile albumFile : mAlbumFiles) {
            @AlbumFile.MediaType int mediaType = albumFile.getMediaType();
            String thumbnail = null;
            if (mediaType == AlbumFile.TYPE_IMAGE) {
                thumbnail = mThumbnailBuilder.createThumbnailForImage(albumFile.getPath());
            } else if (mediaType == AlbumFile.TYPE_VIDEO) {
                thumbnail = mThumbnailBuilder.createThumbnailForVideo(albumFile.getPath());
            }
            albumFile.setThumbPath(thumbnail);
        }
        return mAlbumFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<AlbumFile> albumFiles) {
        if(mDialog.isShowing())
            mDialog.dismiss();
        mCallback.onThumbnailCallback(albumFiles);
    }

    public interface Callback {
        void onThumbnailCallback(ArrayList<AlbumFile> albumFiles);
    }
}

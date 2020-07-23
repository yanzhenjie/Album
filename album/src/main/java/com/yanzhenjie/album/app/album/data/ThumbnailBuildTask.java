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
import android.os.AsyncTask;

import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/10/15.
 */
public class ThumbnailBuildTask extends AsyncTask<Void, Void, ArrayList<AlbumFile>> {
    
    public interface Callback {
        /**
         * The task begins.
         */
        void onThumbnailStart();
        
        /**
         * Callback results.
         *
         * @param albumFiles result.
         */
        void onThumbnailCallback(ArrayList<AlbumFile> albumFiles);
    }
    
    private ArrayList<AlbumFile> mAlbumFiles;
    private Callback mCallback;
    
    private ThumbnailBuilder mThumbnailBuilder;
    private Context mContext;
    
    public ThumbnailBuildTask(Context context, ArrayList<AlbumFile> albumFiles, Callback callback) {
        this.mAlbumFiles = albumFiles;
        if (mAlbumFiles == null) {
            mAlbumFiles = new ArrayList<>();
        }
        this.mCallback = callback;
        this.mContext = context;
        this.mThumbnailBuilder = new ThumbnailBuilder(mContext);
    }
    
    @Override
    protected void onPreExecute() {
        mCallback.onThumbnailStart();
    }
    
    @Override
    protected ArrayList<AlbumFile> doInBackground(Void... params) {
        for (AlbumFile albumFile : mAlbumFiles) {
            int mediaType = albumFile.getMediaType();
            if (mediaType == AlbumFile.TYPE_IMAGE) {
                albumFile.setThumbPath(mThumbnailBuilder.createThumbnailForImage(mContext, albumFile.getUri(), albumFile.getMimeType()));
            } else if (mediaType == AlbumFile.TYPE_VIDEO) {
                albumFile.setThumbPath(mThumbnailBuilder.createThumbnailForVideo(mContext, albumFile.getUri()));
            }
        }
        return mAlbumFiles;
    }
    
    @Override
    protected void onPostExecute(ArrayList<AlbumFile> albumFiles) {
        mCallback.onThumbnailCallback(albumFiles);
    }
}
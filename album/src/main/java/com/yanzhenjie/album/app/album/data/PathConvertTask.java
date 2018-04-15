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

import android.os.AsyncTask;

import com.yanzhenjie.album.AlbumFile;

/**
 * Created by YanZhenjie on 2017/10/18.
 */
public class PathConvertTask extends AsyncTask<String, Void, AlbumFile> {

    public interface Callback {
        /**
         * The task begins.
         */
        void onConvertStart();

        /**
         * Callback results.
         *
         * @param albumFile result.
         */
        void onConvertCallback(AlbumFile albumFile);
    }

    private PathConversion mConversion;
    private Callback mCallback;

    public PathConvertTask(PathConversion conversion, Callback callback) {
        this.mConversion = conversion;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        mCallback.onConvertStart();
    }

    @Override
    protected AlbumFile doInBackground(String... params) {
        return mConversion.convert(params[0]);
    }

    @Override
    protected void onPostExecute(AlbumFile file) {
        mCallback.onConvertCallback(file);
    }
}
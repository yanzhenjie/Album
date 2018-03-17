/*
 * Copyright © 2017 Yan Zhenjie.
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
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.loading.dialog.LoadingDialog;

/**
 * Created by YanZhenjie on 2017/10/18.
 */
public class PathConvertTask extends AsyncTask<String, Void, AlbumFile> {

    public interface Callback {
        void onConvertCallback(AlbumFile albumFile);
    }

    private Dialog mDialog;
    private Callback mCallback;

    private PathConversion mConversion;

    public PathConvertTask(Context context, Callback callback,
                           Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter) {
        this.mDialog = new LoadingDialog(context);
        this.mCallback = callback;

        this.mConversion = new PathConversion(sizeFilter, mimeFilter, durationFilter);
    }

    @Override
    protected void onPreExecute() {
        if (!mDialog.isShowing())
            mDialog.show();
    }

    @Override
    protected AlbumFile doInBackground(String... params) {
        return mConversion.convert(params[0]);

    }

    @Override
    protected void onPostExecute(AlbumFile file) {
        if (mDialog.isShowing())
            mDialog.dismiss();
        mCallback.onConvertCallback(file);
    }
}

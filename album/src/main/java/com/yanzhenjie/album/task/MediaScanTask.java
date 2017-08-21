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

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.loading.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Image scan task.</p>
 * Created by Yan Zhenjie on 2017/3/28.
 */
public class MediaScanTask extends AsyncTask<ArrayList<AlbumFile>, Void, ArrayList<AlbumFolder>> {

    public interface Callback {
        /**
         * AlbumCallback scan results.
         *
         * @param folders AlbumFolder list.
         */
        void onScanCallback(ArrayList<AlbumFolder> folders);
    }

    private Context mContext;
    @Album.ChoiceFunction
    private int mFunction;
    private Callback mCallback;
    private List<AlbumFile> mCheckedFiles;

    private Dialog mWaitDialog;

    public MediaScanTask(Context context, @Album.ChoiceFunction int function, Callback callback, List<AlbumFile> checkedFiles) {
        this.mContext = context;
        this.mFunction = function;
        this.mCallback = callback;
        this.mCheckedFiles = checkedFiles;
        this.mWaitDialog = new LoadingDialog(context);
    }

    @Override
    protected void onPreExecute() {
        if (!mWaitDialog.isShowing())
            mWaitDialog.show();
    }

    @Override
    protected void onPostExecute(ArrayList<AlbumFolder> albumFolders) {
        if (mWaitDialog.isShowing()) mWaitDialog.dismiss();
        mCallback.onScanCallback(albumFolders);
    }

    @SafeVarargs
    @Override
    protected final ArrayList<AlbumFolder> doInBackground(ArrayList<AlbumFile>... params) {
        ArrayList<AlbumFolder> albumFolders;
        switch (mFunction) {
            case Album.FUNCTION_CHOICE_IMAGE: {
                albumFolders = new MediaReader(mContext).getAllImage();
                break;
            }
            case Album.FUNCTION_CHOICE_VIDEO: {
                albumFolders = new MediaReader(mContext).getAllVideo();
                break;
            }
            case Album.FUNCTION_CHOICE_ALBUM:
            default: {
                albumFolders = new MediaReader(mContext).getAllMedia();
                break;
            }
        }

        ArrayList<AlbumFile> checkFiles = params[0];
        if (checkFiles != null && checkFiles.size() > 0) {
            List<AlbumFile> albumFiles = albumFolders.get(0).getAlbumFiles();
            for (AlbumFile checkAlbumFile : checkFiles) {
                for (int i = 0; i < albumFiles.size(); i++) {
                    AlbumFile albumFile = albumFiles.get(i);
                    if (checkAlbumFile.equals(albumFile)) {
                        albumFile.setChecked(true);
                        mCheckedFiles.add(albumFile);
                    }
                }
            }
        }
        return albumFolders;
    }
}

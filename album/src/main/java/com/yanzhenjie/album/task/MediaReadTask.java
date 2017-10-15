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
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.loading.dialog.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Image scan task.</p>
 * Created by Yan Zhenjie on 2017/3/28.
 */
public class MediaReadTask extends AsyncTask<ArrayList<AlbumFile>, Void, ArrayList<AlbumFolder>> {

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

    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;
    private boolean mFilterVisibility;

    private Dialog mWaitDialog;

    public MediaReadTask(Context context, @Album.ChoiceFunction int function, Callback callback, List<AlbumFile> checkedFiles,
                         Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter, boolean filterVisibility) {
        this.mContext = context;
        this.mFunction = function;
        this.mCallback = callback;
        this.mCheckedFiles = checkedFiles;

        this.mSizeFilter = sizeFilter;
        this.mMimeFilter = mimeFilter;
        this.mDurationFilter = durationFilter;
        this.mFilterVisibility = filterVisibility;

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
        MediaReader mediaReader = new MediaReader(mContext, mSizeFilter, mMimeFilter, mDurationFilter, mFilterVisibility);
        switch (mFunction) {
            case Album.FUNCTION_CHOICE_IMAGE: {
                albumFolders = mediaReader.getAllImage();
                break;
            }
            case Album.FUNCTION_CHOICE_VIDEO: {
                albumFolders = mediaReader.getAllVideo();
                break;
            }
            case Album.FUNCTION_CHOICE_ALBUM:
            default: {
                albumFolders = mediaReader.getAllMedia();
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

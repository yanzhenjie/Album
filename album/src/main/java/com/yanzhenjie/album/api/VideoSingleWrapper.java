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
package com.yanzhenjie.album.api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.ui.AlbumActivity;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public final class VideoSingleWrapper extends BasicChoiceWrapper<VideoSingleWrapper, ArrayList<AlbumFile>, String, AlbumFile> {

    public VideoSingleWrapper(@NonNull Context context) {
        super(context);
    }

    @Override
    public void start() {
        AlbumActivity.sResult = mResult;
        AlbumActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, AlbumActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_VIDEO);
        intent.putExtra(Album.KEY_INPUT_CHOICE_MODE, Album.MODE_SINGLE);
        intent.putExtra(Album.KEY_INPUT_COLUMN_COUNT, mColumnCount);
        intent.putExtra(Album.KEY_INPUT_ALLOW_CAMERA, mHasCamera);
        intent.putExtra(Album.KEY_INPUT_LIMIT_COUNT, 1);
        mContext.startActivity(intent);
    }
}

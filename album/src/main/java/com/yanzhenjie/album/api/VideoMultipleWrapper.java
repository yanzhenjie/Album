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
package com.yanzhenjie.album.api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.album.ui.AlbumActivity;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public final class VideoMultipleWrapper extends BasicChoiceVideoWrapper<VideoMultipleWrapper, ArrayList<AlbumFile>, String, ArrayList<AlbumFile>> {

    @IntRange(from = 1, to = Integer.MAX_VALUE)
    private int mLimitCount = Integer.MAX_VALUE;

    private Filter<Long> mDurationFilter;

    public VideoMultipleWrapper(@NonNull Context context) {
        super(context);
    }

    /**
     * Set the list has been selected.
     */
    public final VideoMultipleWrapper checkedList(ArrayList<AlbumFile> checked) {
        this.mChecked = checked;
        return this;
    }

    /**
     * Set the maximum number to be selected.
     */
    public VideoMultipleWrapper selectCount(@IntRange(from = 1, to = Integer.MAX_VALUE) int count) {
        this.mLimitCount = count;
        return this;
    }

    /**
     * Filter video duration.
     */
    public VideoMultipleWrapper filterDuration(Filter<Long> filter) {
        this.mDurationFilter = filter;
        return this;
    }

    @Override
    public void start() {
        AlbumActivity.mSizeFilter = mSizeFilter;
        AlbumActivity.mMimeFilter = mMimeTypeFilter;
        AlbumActivity.mDurationFilter = mDurationFilter;
        AlbumActivity.sResult = mResult;
        AlbumActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, AlbumActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
        intent.putParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_VIDEO);
        intent.putExtra(Album.KEY_INPUT_CHOICE_MODE, Album.MODE_MULTIPLE);
        intent.putExtra(Album.KEY_INPUT_COLUMN_COUNT, mColumnCount);
        intent.putExtra(Album.KEY_INPUT_ALLOW_CAMERA, mHasCamera);
        intent.putExtra(Album.KEY_INPUT_LIMIT_COUNT, mLimitCount);
        intent.putExtra(Album.KEY_INPUT_FILTER_VISIBILITY, mFilterVisibility);
        intent.putExtra(Album.KEY_INPUT_CAMERA_QUALITY, mQuality);
        intent.putExtra(Album.KEY_INPUT_CAMERA_DURATION, mLimitDuration);
        intent.putExtra(Album.KEY_INPUT_CAMERA_BYTES, mLimitBytes);
        mContext.startActivity(intent);
    }
}

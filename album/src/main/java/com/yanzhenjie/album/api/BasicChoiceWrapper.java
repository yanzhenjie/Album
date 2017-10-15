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
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.yanzhenjie.album.Filter;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public abstract class BasicChoiceWrapper<T extends BasicChoiceWrapper, Result, Cancel, Checked> extends BasicAlbumWrapper<T, Result, Cancel, Checked> {

    boolean mHasCamera = true;
    @IntRange(from = 1, to = 4)
    int mColumnCount = 2;

    Filter<Long> mSizeFilter;
    Filter<String> mMimeTypeFilter;

    boolean mFilterVisibility = true;

    BasicChoiceWrapper(@NonNull Context context) {
        super(context);
    }

    /**
     * Turn on the camera function.
     */
    public T camera(boolean hasCamera) {
        this.mHasCamera = hasCamera;
        return (T) this;
    }

    /**
     * Sets the number of columns for the page.
     */
    public T columnCount(@IntRange(from = 1, to = 4) int count) {
        this.mColumnCount = count;
        return (T) this;
    }

    /**
     * Filter the file size.
     */
    public T filterSize(Filter<Long> filter) {
        this.mSizeFilter = filter;
        return (T) this;
    }

    /**
     * Filter the file extension.
     */
    public T filterMimeType(Filter<String> filter) {
        this.mMimeTypeFilter = filter;
        return (T) this;
    }

    /**
     * The visibility of the filtered file.
     */
    public T afterFilterVisibility(boolean visibility) {
        this.mFilterVisibility = visibility;
        return (T) this;
    }

}

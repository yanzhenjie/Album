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

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/8/19.
 */
public abstract class BasicGalleryWrapper<T extends BasicGalleryWrapper, Result, Cancel, Checked> extends BasicAlbumWrapper<T, ArrayList<Result>, Cancel, ArrayList<Checked>> {

    @IntRange(from = 1, to = Integer.MAX_VALUE)
    int mCurrentPosition = 0;
    boolean mCheckable = true;
    @IntRange(from = 0, to = 255)
    int mNavigationAlpha = 80;

    public BasicGalleryWrapper(Context context) {
        super(context);
    }

    /**
     * Set the list has been selected.
     */
    public final T checkedList(ArrayList<Checked> checked) {
        this.mChecked = checked;
        return (T) this;
    }

    /**
     * Set the show position of List.
     */
    public T currentPosition(@IntRange(from = 1, to = Integer.MAX_VALUE) int currentPosition) {
        this.mCurrentPosition = currentPosition;
        return (T) this;
    }

    /**
     * The ability to select pictures.
     */
    public T checkable(boolean checkable) {
        this.mCheckable = checkable;
        return (T) this;
    }

    /**
     * Set alpha of NavigationBar.
     */
    public T navigationAlpha(@IntRange(from = 0, to = 255) int alpha) {
        this.mNavigationAlpha = alpha;
        return (T) this;
    }
}

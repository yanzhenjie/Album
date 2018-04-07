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
import android.support.annotation.IntRange;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/8/19.
 */
public abstract class BasicGalleryWrapper<Returner extends BasicGalleryWrapper, Result, Cancel, Checked> extends BasicAlbumWrapper<Returner, ArrayList<Result>, Cancel, ArrayList<Checked>> {

    int mCurrentPosition;
    boolean mCheckable;
    int mNavigationAlpha;

    public BasicGalleryWrapper(Context context) {
        super(context);
    }

    /**
     * Set the list has been selected.
     */
    public final Returner checkedList(ArrayList<Checked> checked) {
        this.mChecked = checked;
        return (Returner) this;
    }

    /**
     * Set the show position of List.
     */
    public Returner currentPosition(@IntRange(from = 0, to = Integer.MAX_VALUE) int currentPosition) {
        this.mCurrentPosition = currentPosition;
        return (Returner) this;
    }

    /**
     * The ability to select pictures.
     */
    public Returner checkable(boolean checkable) {
        this.mCheckable = checkable;
        return (Returner) this;
    }

    /**
     * Set alpha of NavigationBar.
     */
    public Returner navigationAlpha(@IntRange(from = 0, to = 255) int alpha) {
        this.mNavigationAlpha = alpha;
        return (Returner) this;
    }
}

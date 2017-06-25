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
package com.yanzhenjie.album;

import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * <p>Album wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class AlbumWrapper extends RadioAlbumWrapper<AlbumWrapper> {

    private Intent mIntent;
    private int requestCode;

    AlbumWrapper(Object o) {
        super(o, VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM);
        this.mIntent = getIntent();
        mIntent.putExtra(KEY_INPUT_LIMIT_COUNT, Integer.MAX_VALUE);
    }

    @Override
    public AlbumWrapper statusBarColor(@ColorInt int color) {
        mIntent.putExtra(KEY_INPUT_STATUS_COLOR, color);
        return this;
    }

    @Override
    public AlbumWrapper toolBarColor(@ColorInt int color) {
        mIntent.putExtra(KEY_INPUT_TOOLBAR_COLOR, color);
        return this;
    }

    @Override
    public AlbumWrapper navigationBarColor(@ColorInt int color) {
        mIntent.putExtra(KEY_INPUT_NAVIGATION_COLOR, color);
        return this;
    }

    /**
     * Request code, callback to {@code onActivityResult()}.
     *
     * @param requestCode int.
     * @return a subclass of {@link BasicWrapper}.
     * @deprecated use {@link #start(int)} instead.
     */
    @Deprecated
    public AlbumWrapper requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * Sets the list of selected files.
     *
     * @param pathList path list.
     * @return a subclass of {@link BasicWrapper}.
     */
    public AlbumWrapper checkedList(@NonNull ArrayList<String> pathList) {
        mIntent.putStringArrayListExtra(KEY_INPUT_CHECKED_LIST, pathList);
        return this;
    }

    /**
     * Set the number of photos to select.
     *
     * @param count count.
     * @return {@link AlbumWrapper}.
     */
    public AlbumWrapper selectCount(int count) {
        mIntent.putExtra(KEY_INPUT_LIMIT_COUNT, count);
        return this;
    }

    /**
     * @see #start(int)
     * @deprecated use {@link #start(int)} instead.
     */
    @Deprecated
    public void start() {
        start(requestCode);
    }
}

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
public class AlbumWrapper extends UIWrapper<AlbumWrapper> {

    public static final String KEY_INPUT_TITLE = "KEY_INPUT_TITLE";
    public static final String KEY_INPUT_COLUMN_COUNT = "KEY_INPUT_COLUMN_COUNT";
    public static final String KEY_INPUT_LIMIT_COUNT = "KEY_INPUT_LIMIT_COUNT";
    public static final String KEY_INPUT_ALLOW_CAMERA = "KEY_INPUT_ALLOW_CAMERA";

    private Intent intent;

    AlbumWrapper(Object o) {
        this(o, new Intent(getContext(o), AlbumActivity.class), VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM);
    }

    private AlbumWrapper(Object o, Intent intent, int function) {
        super(o, intent, function);
        this.intent = intent;
    }

    @Override
    public AlbumWrapper requestCode(int requestCode) {
        intent.putExtra(KEY_INPUT_REQUEST_CODE, requestCode);
        return this;
    }

    @Override
    public AlbumWrapper statusBarColor(@ColorInt int color) {
        intent.putExtra(KEY_INPUT_STATUS_COLOR, color);
        return this;
    }

    @Override
    public AlbumWrapper toolBarColor(@ColorInt int color) {
        intent.putExtra(KEY_INPUT_TOOLBAR_COLOR, color);
        return this;
    }

    @Override
    public AlbumWrapper navigationBarColor(@ColorInt int color) {
        intent.putExtra(KEY_INPUT_NAVIGATION_COLOR, color);
        return this;
    }

    @Override
    public AlbumWrapper checkedList(@NonNull ArrayList<String> pathList) {
        intent.putStringArrayListExtra(KEY_INPUT_CHECKED_LIST, pathList);
        return this;
    }

    /**
     * Set the title of ui.
     *
     * @param title string.
     * @return a subclass of {@link BasicWrapper}.
     */
    public AlbumWrapper title(@NonNull String title) {
        intent.putExtra(KEY_INPUT_TITLE, title);
        return this;
    }

    /**
     * Sets the number of column that the photo shows.
     *
     * @param count count.
     * @return {@link AlbumWrapper}.
     */
    public AlbumWrapper columnCount(int count) {
        intent.putExtra(KEY_INPUT_COLUMN_COUNT, count);
        return this;
    }

    /**
     * Set the number of photos to select.
     *
     * @param count count.
     * @return {@link AlbumWrapper}.
     */
    public AlbumWrapper selectCount(int count) {
        intent.putExtra(KEY_INPUT_LIMIT_COUNT, count);
        return this;
    }

    /**
     * Allow to take pictures.
     *
     * @param camera true, other wise false.
     * @return {@link AlbumWrapper}.
     */
    public AlbumWrapper camera(boolean camera) {
        intent.putExtra(KEY_INPUT_ALLOW_CAMERA, camera);
        return this;
    }

}

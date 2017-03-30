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

import java.util.ArrayList;

/**
 * <p>Gallery wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class GalleryWrapper extends BasicWrapper<GalleryWrapper> {

    public static final String KEY_INPUT_CHECK_FUNCTION = "KEY_INPUT_CHECK_FUNCTION";
    public static final String KEY_INPUT_CURRENT_POSITION = "KEY_INPUT_CURRENT_POSITION";

    private Intent intent;

    GalleryWrapper(Object o) {
        this(o, new Intent(getContext(o), AlbumActivity.class));
    }

    private GalleryWrapper(Object o, Intent intent) {
        super(o, intent);
        this.intent = intent;
        this.intent.putExtra(KEY_INPUT_FRAMEWORK_FUNCTION, VALUE_INPUT_FRAMEWORK_FUNCTION_GALLERY);
    }

    @Override
    public GalleryWrapper requestCode(int requestCode) {
        intent.putExtra(KEY_INPUT_REQUEST_CODE, requestCode);
        return this;
    }

    @Override
    public GalleryWrapper statusBarColor(@ColorInt int color) {
        intent.putExtra(KEY_INPUT_STATUS_COLOR, color);
        return this;
    }

    @Override
    public GalleryWrapper toolBarColor(@ColorInt int color) {
        intent.putExtra(KEY_INPUT_TOOLBAR_COLOR, color);
        return this;
    }

    @Override
    public GalleryWrapper navigationBarColor(@ColorInt int color) {
        intent.putExtra(KEY_INPUT_NAVIGATION_COLOR, color);
        return this;
    }

    @Override
    public GalleryWrapper checkedList(ArrayList<String> pathList) {
        intent.putStringArrayListExtra(KEY_INPUT_CHECKED_LIST, pathList);
        return this;
    }

    /**
     * Have the function of selecting pictures.
     *
     * @param check true, other wise false.
     * @return {@link GalleryWrapper}.
     */
    public GalleryWrapper checkFunction(boolean check) {
        intent.putExtra(KEY_INPUT_CHECK_FUNCTION, check);
        return this;
    }

    /**
     * The image to be displayed is the position in the list.
     *
     * @param position position.
     * @return {@link GalleryWrapper}.
     */
    public GalleryWrapper currentPosition(int position) {
        intent.putExtra(KEY_INPUT_CURRENT_POSITION, position);
        return this;
    }

}

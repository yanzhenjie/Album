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
 * <p>Basic UI Wrapper.</p>
 * Created by Yan Zhenjie on 2017/4/18.
 */
public abstract class UIWrapper<T extends BasicWrapper> extends BasicWrapper<T> {

    public static final String KEY_INPUT_STATUS_COLOR = "KEY_INPUT_STATUS_COLOR";
    public static final String KEY_INPUT_TOOLBAR_COLOR = "KEY_INPUT_TOOLBAR_COLOR";
    public static final String KEY_INPUT_NAVIGATION_COLOR = "KEY_INPUT_NAVIGATION_COLOR";
    public static final String KEY_INPUT_CHECKED_LIST = "KEY_INPUT_CHECKED_LIST";

    UIWrapper(Object o, Intent intent, int function, String sourceName) {
        super(o, intent, function, sourceName);
    }

    /**
     * Set the StatusBar color.
     *
     * @param color color.
     * @return a subclass of {@link BasicWrapper}.
     */
    public abstract T statusBarColor(@ColorInt int color);

    /**
     * Set the ToolBar color.
     *
     * @param color color.
     * @return a subclass of {@link BasicWrapper}.
     */
    public abstract T toolBarColor(@ColorInt int color);

    /**
     * Set the NavigationBar color.
     *
     * @param color color.
     * @return a subclass of {@link BasicWrapper}.
     */
    public abstract T navigationBarColor(@ColorInt int color);

    /**
     * Sets the list of selected files.
     *
     * @param pathList path list.
     * @return a subclass of {@link BasicWrapper}.
     */
    public abstract T checkedList(@NonNull ArrayList<String> pathList);

}

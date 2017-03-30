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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * <p>Album basic wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public abstract class BasicWrapper<T extends BasicWrapper> {

    protected static final String KEY_INPUT_REQUEST_CODE = "KEY_INPUT_REQUEST_CODE";

    public static final String KEY_INPUT_FRAMEWORK_FUNCTION = "KEY_INPUT_FRAMEWORK_FUNCTION";
    public static final int VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM = 0;
    public static final int VALUE_INPUT_FRAMEWORK_FUNCTION_GALLERY = 1;

    public static final String KEY_INPUT_STATUS_COLOR = "KEY_INPUT_STATUS_COLOR";
    public static final String KEY_INPUT_TOOLBAR_COLOR = "KEY_INPUT_TOOLBAR_COLOR";
    public static final String KEY_INPUT_NAVIGATION_COLOR = "KEY_INPUT_NAVIGATION_COLOR";
    public static final String KEY_INPUT_CHECKED_LIST = "KEY_INPUT_CHECKED_LIST";

    private Object o;
    private Intent intent;

    protected BasicWrapper(Object o, Intent intent) {
        this.o = o;
        this.intent = intent;
    }

    /**
     * Start the Album.
     */
    public final void start() {
        try {
            Method method = o.getClass().getMethod("startActivityForResult", Intent.class, int.class);
            if (!method.isAccessible()) method.setAccessible(true);
            method.invoke(o, intent, intent.getIntExtra(KEY_INPUT_REQUEST_CODE, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Request code, callback to {@code onActivityResult()};
     *
     * @param requestCode int.
     * @return a subclass of A.
     */
    public abstract T requestCode(int requestCode);

    /**
     * Set the StatusBar color.
     *
     * @param color color.
     * @return a subclass of A.
     */
    public abstract T statusBarColor(@ColorInt int color);

    /**
     * Set the ToolBar color.
     *
     * @param color color.
     * @return a subclass of A.
     */
    public abstract T toolBarColor(@ColorInt int color);

    /**
     * Set the NavigationBar color.
     *
     * @param color color.
     * @return a subclass of A.
     */
    public abstract T navigationBarColor(@ColorInt int color);

    /**
     * Sets the list of selected files.
     *
     * @param pathList path list.
     * @return a subclass of A.
     */
    public abstract T checkedList(ArrayList<String> pathList);

    /**
     * Get the context.
     *
     * @param o {@link Fragment}, {@link android.app.Fragment}, {@link Activity}.
     * @return context.
     */
    protected static
    @NonNull
    Context getContext(Object o) {
        if (o instanceof Activity) return (Context) o;
        else if (o instanceof Fragment) return ((Fragment) o).getContext();
        else if (o instanceof android.app.Fragment) ((android.app.Fragment) o).getActivity();
        throw new IllegalArgumentException(o.getClass() + " is not supported.");
    }

}

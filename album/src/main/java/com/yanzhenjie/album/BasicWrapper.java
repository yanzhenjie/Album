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
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.lang.reflect.Method;

import static com.yanzhenjie.album.util.AlbumConstant.REQUEST_CODE_ALBUM;
import static com.yanzhenjie.album.util.AlbumConstant.REQUEST_CODE_CAMERA;
import static com.yanzhenjie.album.util.AlbumConstant.REQUEST_CODE_GALLERY;

/**
 * <p>Album basic wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
abstract class BasicWrapper<T extends BasicWrapper> {

    static final String KEY_INPUT_REQUEST_CODE = "KEY_INPUT_REQUEST_CODE";

    static final String KEY_INPUT_FRAMEWORK_FUNCTION = "KEY_INPUT_FRAMEWORK_FUNCTION";
    static final int VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM = 0;
    static final int VALUE_INPUT_FRAMEWORK_FUNCTION_GALLERY = 1;
    static final int VALUE_INPUT_FRAMEWORK_FUNCTION_CAMERA = 2;

    private Object o;
    private Intent intent;
    private int requestCode;

    BasicWrapper(Object o, Intent intent, int function, String sourceName) {
        this.o = o;
        this.intent = intent;
        this.intent.putExtra(KEY_INPUT_FRAMEWORK_FUNCTION, function);
        if (sourceName.equals(AlbumWrapper.class.getName())) {
            requestCode = REQUEST_CODE_ALBUM;
        } else if (sourceName.equals(GalleryWrapper.class.getName())) {
            requestCode = REQUEST_CODE_GALLERY;
        } else if (sourceName.equals(CameraWrapper.class.getName())) {
            requestCode = REQUEST_CODE_CAMERA;
        }
    }

    /**
     * Start the Album.
     */
    public final void start() {
        try {
            Method method = o.getClass().getMethod("startActivityForResult", Intent.class, int.class);
            if (!method.isAccessible()) method.setAccessible(true);
            method.invoke(o, intent, intent.getIntExtra(KEY_INPUT_REQUEST_CODE, requestCode));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Request code, callback to {@code onActivityResult()}.
     *
     * @param requestCode int.
     * @return a subclass of {@link BasicWrapper}.
     */
    public abstract T requestCode(int requestCode);

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

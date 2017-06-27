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

/**
 * <p>Camera wrapper.</p>
 * Created by Yan Zhenjie on 2017/4/18.
 */
public class CameraWrapper extends BasicWrapper<CameraWrapper> {

    public static final String KEY_INPUT_IMAGE_PATH = "KEY_INPUT_IMAGE_PATH";

    private Intent mIntent;
    private int requestCode;

    CameraWrapper(Object o) {
        super(o, VALUE_INPUT_FRAMEWORK_FUNCTION_CAMERA);
        this.mIntent = getIntent();
    }

    /**
     * Request code, callback to {@code onActivityResult()}.
     *
     * @param requestCode int.
     * @return a subclass of {@link BasicWrapper}.
     * @deprecated use {@link #start(int)} instead.
     */
    @Deprecated
    public CameraWrapper requestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    /**
     * Set the image storage path.
     *
     * @param imagePath image path.
     * @return {@link CameraWrapper}.
     */
    public CameraWrapper imagePath(String imagePath) {
        mIntent.putExtra(KEY_INPUT_IMAGE_PATH, imagePath);
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

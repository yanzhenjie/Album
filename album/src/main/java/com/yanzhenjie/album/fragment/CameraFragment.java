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
package com.yanzhenjie.album.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.yanzhenjie.album.CameraWrapper;
import com.yanzhenjie.album.impl.CameraCallback;

/**
 * <p>Call will take pictures immediately.</p>
 * Created by Yan Zhenjie on 2017/4/18.
 */
public class CameraFragment extends BasicCameraFragment {

    private CameraCallback mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mCallback = (CameraCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle argument = getArguments();
        String imagePath = argument.getString(CameraWrapper.KEY_INPUT_IMAGE_PATH);
        imagePath = TextUtils.isEmpty(imagePath) ? randomJPGPath() : imagePath;
        cameraUnKnowPermission(imagePath);
    }

    @Override
    protected void onCameraBack(String imagePath) {
        mCallback.onCameraResult(imagePath);
    }

    @Override
    protected void onUserCamera() {
        mCallback.onCameraCancel();
    }
}

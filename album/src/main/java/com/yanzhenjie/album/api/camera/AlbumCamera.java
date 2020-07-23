/*
 * Copyright 2017 Yan Zhenjie.
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
package com.yanzhenjie.album.api.camera;

import android.content.Context;

import com.yanzhenjie.album.api.ImageCameraWrapper;
import com.yanzhenjie.album.api.VideoCameraWrapper;

import androidx.annotation.NonNull;

/**
 * Created by YanZhenjie on 2017/8/18.
 */
public class AlbumCamera implements Camera<ImageCameraWrapper, VideoCameraWrapper> {

    private Context mContext;

    public AlbumCamera(@NonNull Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ImageCameraWrapper image() {
        return new ImageCameraWrapper(mContext);
    }

    @NonNull
    @Override
    public VideoCameraWrapper video() {
        return new VideoCameraWrapper(mContext);
    }

}
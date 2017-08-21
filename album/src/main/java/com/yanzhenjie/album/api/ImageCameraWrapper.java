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
package com.yanzhenjie.album.api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.ui.CameraActivity;

/**
 * <p>Camera wrapper.</p>
 * Created by Yan Zhenjie on 2017/4/18.
 */
public class ImageCameraWrapper extends BasicCameraWrapper<ImageCameraWrapper> {

    public ImageCameraWrapper(@NonNull Context context) {
        super(context);
    }

    public void start() {
        CameraActivity.sAlbumListener = mAlbumListener;
        Intent intent = new Intent(mContext, CameraActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);

        intent.putExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CAMERA_IMAGE);
        intent.putExtra(Album.KEY_INPUT_FILE_PATH, mFilePath);
        mContext.startActivity(intent);
    }
}

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yanzhenjie.album.AlbumListener;

/**
 * Created by YanZhenjie on 2017/8/18.
 */
public abstract class BasicCameraWrapper<T extends BasicCameraWrapper> {

    @NonNull
    final Context mContext;
    AlbumListener<String> mAlbumListener;
    int mRequestCode;
    @Nullable
    String mFilePath;

    public BasicCameraWrapper(@NonNull Context context) {
        this.mContext = context;
    }

    /**
     * Set the results to listener.
     */
    public T listener(AlbumListener<String> albumListener) {
        this.mAlbumListener = albumListener;
        return (T) this;
    }

    /**
     * Request tag.
     */
    public T requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return (T) this;
    }

    /**
     * Set the image storage path.
     */
    public T filePath(@Nullable String filePath) {
        this.mFilePath = filePath;
        return (T) this;
    }

    /**
     * Start up.
     */
    public abstract void start();

}

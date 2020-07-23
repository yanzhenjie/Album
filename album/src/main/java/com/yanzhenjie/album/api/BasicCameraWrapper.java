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
package com.yanzhenjie.album.api;

import android.content.Context;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumCameraFile;

import androidx.annotation.Nullable;

/**
 * Created by YanZhenjie on 2017/8/18.
 */
public abstract class BasicCameraWrapper<Returner extends BasicCameraWrapper> {
    
    Context mContext;
    Action<AlbumCameraFile> mResult;
    Action<String> mCancel;
    String mFilePath;
    
    public BasicCameraWrapper(Context context) {
        this.mContext = context;
    }
    
    /**
     * Set the action when result.
     *
     * @param result action when producing result.
     */
    public final Returner onResult(Action<AlbumCameraFile> result) {
        this.mResult = result;
        return (Returner) this;
    }
    
    /**
     * Set the action when canceling.
     *
     * @param cancel action when canceled.
     */
    public final Returner onCancel(Action<String> cancel) {
        this.mCancel = cancel;
        return (Returner) this;
    }
    
    /**
     * Set the image storage path.
     *
     * @param filePath storage path.
     */
    public Returner filePath(@Nullable String filePath) {
        this.mFilePath = filePath;
        return (Returner) this;
    }
    
    /**
     * Start up.
     */
    public abstract void start();
    
}
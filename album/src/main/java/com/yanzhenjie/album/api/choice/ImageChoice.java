/*
 * Copyright © 2017 Yan Zhenjie.
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
package com.yanzhenjie.album.api.choice;

import android.content.Context;

import com.yanzhenjie.album.api.ImageMultipleWrapper;
import com.yanzhenjie.album.api.ImageSingleWrapper;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public final class ImageChoice implements Choice<ImageMultipleWrapper, ImageSingleWrapper> {

    private Context mContext;

    public ImageChoice(Context context) {
        mContext = context;
    }

    @Override
    public ImageMultipleWrapper multipleChoice() {
        return new ImageMultipleWrapper(mContext);
    }

    @Override
    public ImageSingleWrapper singleChoice() {
        return new ImageSingleWrapper(mContext);
    }

}
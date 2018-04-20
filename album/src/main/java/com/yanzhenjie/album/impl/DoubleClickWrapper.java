/*
 * Copyright 2018 Yan Zhenjie.
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
package com.yanzhenjie.album.impl;

import android.view.View;

/**
 * Created by YanZhenjie on 2018/4/20.
 */
public class DoubleClickWrapper implements View.OnClickListener {

    private final View.OnClickListener mOnClickListener;
    private long mFirstTime;

    public DoubleClickWrapper(View.OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {
        long now = System.currentTimeMillis();
        if (now - mFirstTime <= 500) {
            mOnClickListener.onClick(v);
        }
        mFirstTime = now;
    }
}
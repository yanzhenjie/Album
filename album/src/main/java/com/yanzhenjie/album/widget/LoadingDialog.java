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
package com.yanzhenjie.album.widget;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import android.widget.TextView;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;

/**
 * Created by YanZhenjie on 2018/4/10.
 */
public class LoadingDialog extends Dialog {

    private ColorProgressBar mProgressBar;
    private TextView mTvMessage;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.Album_Dialog);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        setContentView(R.layout.album_dialog_loading);
        mProgressBar = findViewById(R.id.progress_bar);
        mTvMessage = findViewById(R.id.tv_message);
    }

    /**
     * Set some properties of the view.
     *
     * @param widget widget.
     */
    public void setupViews(Widget widget) {
        if (widget.getUiStyle() == Widget.STYLE_LIGHT) {
            int color = ContextCompat.getColor(getContext(), R.color.albumLoadingDark);
            mProgressBar.setColorFilter(color);
        } else {
            mProgressBar.setColorFilter(widget.getToolBarColor());
        }
    }

    /**
     * Set the message.
     *
     * @param message message resource id.
     */
    public void setMessage(@StringRes int message) {
        mTvMessage.setText(message);
    }

    /**
     * Set the message.
     *
     * @param message message.
     */
    public void setMessage(String message) {
        mTvMessage.setText(message);
    }

}
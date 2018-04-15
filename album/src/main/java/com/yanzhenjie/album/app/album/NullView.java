/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.album.app.album;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.app.Contract;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.SystemBar;

/**
 * Created by YanZhenjie on 2018/4/7.
 */
class NullView extends Contract.NullView implements View.OnClickListener {

    private Activity mActivity;

    private Toolbar mToolbar;
    private TextView mTvMessage;
    private AppCompatButton mBtnTakeImage;
    private AppCompatButton mBtnTakeVideo;

    public NullView(Activity activity, Contract.NullPresenter presenter) {
        super(activity, presenter);
        this.mActivity = activity;
        this.mToolbar = activity.findViewById(R.id.toolbar);
        this.mTvMessage = activity.findViewById(R.id.tv_message);
        this.mBtnTakeImage = activity.findViewById(R.id.btn_camera_image);
        this.mBtnTakeVideo = activity.findViewById(R.id.btn_camera_video);

        this.mBtnTakeImage.setOnClickListener(this);
        this.mBtnTakeVideo.setOnClickListener(this);
    }

    @Override
    public void setupViews(Widget widget) {
        mToolbar.setBackgroundColor(widget.getToolBarColor());

        int statusBarColor = widget.getStatusBarColor();
        Drawable navigationIcon = getDrawable(R.drawable.album_ic_back_white);
        if (widget.getUiStyle() == Widget.STYLE_LIGHT) {
            if (SystemBar.setStatusBarDarkFont(mActivity, true)) {
                SystemBar.setStatusBarColor(mActivity, statusBarColor);
            } else {
                SystemBar.setStatusBarColor(mActivity, getColor(R.color.albumColorPrimaryBlack));
            }

            mToolbar.setTitleTextColor(getColor(R.color.albumFontDark));
            mToolbar.setSubtitleTextColor(getColor(R.color.albumFontDark));

            AlbumUtils.setDrawableTint(navigationIcon, getColor(R.color.albumIconDark));
            setHomeAsUpIndicator(navigationIcon);
        } else {
            SystemBar.setStatusBarColor(mActivity, statusBarColor);
            setHomeAsUpIndicator(navigationIcon);
        }
        SystemBar.setNavigationBarColor(mActivity, widget.getNavigationBarColor());

        Widget.ButtonStyle buttonStyle = widget.getButtonStyle();
        ColorStateList buttonSelector = buttonStyle.getButtonSelector();
        mBtnTakeImage.setSupportBackgroundTintList(buttonSelector);
        mBtnTakeVideo.setSupportBackgroundTintList(buttonSelector);
        if (buttonStyle.getUiStyle() == Widget.STYLE_LIGHT) {
            Drawable drawable = mBtnTakeImage.getCompoundDrawables()[0];
            AlbumUtils.setDrawableTint(drawable, getColor(R.color.albumIconDark));
            mBtnTakeImage.setCompoundDrawables(drawable, null, null, null);

            drawable = mBtnTakeVideo.getCompoundDrawables()[0];
            AlbumUtils.setDrawableTint(drawable, getColor(R.color.albumIconDark));
            mBtnTakeVideo.setCompoundDrawables(drawable, null, null, null);
            mBtnTakeImage.setTextColor(getColor(R.color.albumFontDark));
            mBtnTakeVideo.setTextColor(getColor(R.color.albumFontDark));
        }
    }

    @Override
    public void setMessage(int message) {
        mTvMessage.setText(message);
    }

    @Override
    public void setMakeImageDisplay(boolean display) {
        mBtnTakeImage.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setMakeVideoDisplay(boolean display) {
        mBtnTakeVideo.setVisibility(display ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_camera_image) {
            getPresenter().takePicture();
        } else if (id == R.id.btn_camera_video) {
            getPresenter().takeVideo();
        }
    }
}
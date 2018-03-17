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
package com.yanzhenjie.album.ui;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.statusview.StatusView;

/**
 * <p>Display when there is no picture in the multimedia library.</p>
 * Created by Yan Zhenjie on 2017/3/28.
 */
public class AlbumNullFragment extends NoFragment {

    private static final String KEY_OUTPUT_IMAGE_PATH = "KEY_OUTPUT_IMAGE_PATH";

    /**
     * Resolve the image path at the time of success.
     *
     * @param bundle {@link #onFragmentResult(int, int, Bundle)}.
     * @return image path.
     */
    public static String parseImagePath(Bundle bundle) {
        return bundle.getString(KEY_OUTPUT_IMAGE_PATH);
    }

    private StatusView mStatusView;
    private Toolbar mToolbar;

    private TextView mTvMessage;
    private AppCompatButton mBtnCameraImage;
    private AppCompatButton mBtnCameraVideo;

    private Widget mWidget;
    @Album.ChoiceFunction
    private int mFunction;
    private boolean mHasCamera;

    @IntRange(from = 0, to = 1)
    private int mQuality = 1;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitDuration;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitBytes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_fragment_null, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mStatusView = (StatusView) view.findViewById(R.id.status_view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        setToolbar(mToolbar);

        mTvMessage = (TextView) view.findViewById(R.id.tv_message);
        mBtnCameraImage = (AppCompatButton) view.findViewById(R.id.btn_camera_image);
        mBtnCameraVideo = (AppCompatButton) view.findViewById(R.id.btn_camera_video);

        mBtnCameraImage.setOnClickListener(mCameraClickListener);
        mBtnCameraVideo.setOnClickListener(mCameraClickListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argument = getArguments();
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        //noinspection WrongConstant
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION);
        mHasCamera = argument.getBoolean(Album.KEY_INPUT_ALLOW_CAMERA);

        mQuality = argument.getInt(Album.KEY_INPUT_CAMERA_QUALITY, 1);
        mLimitDuration = argument.getLong(Album.KEY_INPUT_CAMERA_DURATION, Long.MAX_VALUE);
        mLimitBytes = argument.getLong(Album.KEY_INPUT_CAMERA_BYTES, Long.MAX_VALUE);

        initializeWidget();
    }

    private void initializeWidget() {
        int statusBarColor = mWidget.getStatusBarColor();
        Drawable navigationIcon = ContextCompat.getDrawable(getContext(), R.drawable.album_ic_back_white);
        if (mWidget.getStyle() == Widget.STYLE_LIGHT) {
            if (((AlbumActivity) getActivity()).isSucceedLightStatus()) {
                mStatusView.setBackgroundColor(statusBarColor);
            } else {
                mStatusView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.album_ColorPrimaryBlack));
            }

            mToolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.album_FontDark));
            mToolbar.setSubtitleTextColor(ContextCompat.getColor(getContext(), R.color.album_FontDark));

            AlbumUtils.setDrawableTint(navigationIcon, ContextCompat.getColor(getContext(), R.color.album_IconDark));
            displayHomeAsUpEnabled(navigationIcon);
        } else {
            mStatusView.setBackgroundColor(statusBarColor);
            displayHomeAsUpEnabled(navigationIcon);
        }

        int toolbarColor = mWidget.getToolBarColor();
        mToolbar.setBackgroundColor(toolbarColor);
        mToolbar.setTitle(mWidget.getTitle());

        switch (mFunction) {
            case Album.FUNCTION_CHOICE_IMAGE: {
                mTvMessage.setText(R.string.album_not_found_image);
                mBtnCameraVideo.setVisibility(View.GONE);
                break;
            }
            case Album.FUNCTION_CHOICE_VIDEO: {
                mTvMessage.setText(R.string.album_not_found_video);
                mBtnCameraImage.setVisibility(View.GONE);
                break;
            }
            case Album.FUNCTION_CHOICE_ALBUM:
            default: {
                mTvMessage.setText(R.string.album_not_found_album);
                break;
            }
        }

        if (mHasCamera) {
            Widget.ButtonStyle buttonStyle = mWidget.getButtonStyle();
            ColorStateList buttonSelector = buttonStyle.getButtonSelector();
            mBtnCameraImage.setSupportBackgroundTintList(buttonSelector);
            mBtnCameraVideo.setSupportBackgroundTintList(buttonSelector);
            if (buttonStyle.getButtonStyle() == Widget.STYLE_LIGHT) {
                Drawable drawable = mBtnCameraImage.getCompoundDrawables()[0];
                AlbumUtils.setDrawableTint(drawable, ContextCompat.getColor(getContext(), R.color.album_IconDark));
                mBtnCameraImage.setCompoundDrawables(drawable, null, null, null);

                drawable = mBtnCameraVideo.getCompoundDrawables()[0];
                AlbumUtils.setDrawableTint(drawable, ContextCompat.getColor(getContext(), R.color.album_IconDark));
                mBtnCameraVideo.setCompoundDrawables(drawable, null, null, null);

                mBtnCameraImage.setTextColor(ContextCompat.getColor(getContext(), R.color.album_FontDark));
                mBtnCameraVideo.setTextColor(ContextCompat.getColor(getContext(), R.color.album_FontDark));
            }
        } else {
            mBtnCameraImage.setVisibility(View.GONE);
            mBtnCameraVideo.setVisibility(View.GONE);
        }
    }

    /**
     * Camera click.
     */
    private View.OnClickListener mCameraClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn_camera_image) {
                Album.camera(getContext())
                        .image()
                        .onResult(mCameraAction)
                        .start();
            } else if (id == R.id.btn_camera_video) {
                Album.camera(getContext())
                        .video()
                        .quality(mQuality)
                        .limitDuration(mLimitDuration)
                        .limitBytes(mLimitBytes)
                        .onResult(mCameraAction)
                        .start();
            }
        }
    };

    private Action<String> mCameraAction = new Action<String>() {
        @Override
        public void onAction(int requestCode, @NonNull String result) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_OUTPUT_IMAGE_PATH, result);
            setResult(RESULT_OK, bundle);
            finish();
        }
    };
}
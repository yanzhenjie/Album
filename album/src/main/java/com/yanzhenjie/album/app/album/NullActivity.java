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
package com.yanzhenjie.album.app.album;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumCameraFile;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.app.Contract;
import com.yanzhenjie.album.mvp.BaseActivity;

/**
 * Created by YanZhenjie on 2017/3/28.
 */
public class NullActivity extends BaseActivity implements Contract.NullPresenter {
    
    private static final String KEY_OUTPUT_IMAGE_PATH = "KEY_OUTPUT_IMAGE_PATH";
    
    public static AlbumCameraFile parseAlbumCameraFile(Intent intent) {
        return intent.getParcelableExtra(KEY_OUTPUT_IMAGE_PATH);
    }
    
    private Widget mWidget;
    private int mQuality = 1;
    private long mLimitDuration;
    private long mLimitBytes;
    
    private Contract.NullView mView;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_null);
        mView = new NullView(this, this);
        
        Bundle argument = getIntent().getExtras();
        assert argument != null;
        int function = argument.getInt(Album.KEY_INPUT_FUNCTION);
        boolean hasCamera = argument.getBoolean(Album.KEY_INPUT_ALLOW_CAMERA);
        
        mQuality = argument.getInt(Album.KEY_INPUT_CAMERA_QUALITY);
        mLimitDuration = argument.getLong(Album.KEY_INPUT_CAMERA_DURATION);
        mLimitBytes = argument.getLong(Album.KEY_INPUT_CAMERA_BYTES);
        
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        mView.setupViews(mWidget);
        mView.setTitle(mWidget.getTitle());
        
        switch (function) {
            case Album.FUNCTION_CHOICE_IMAGE: {
                mView.setMessage(R.string.album_not_found_image);
                mView.setMakeVideoDisplay(false);
                break;
            }
            case Album.FUNCTION_CHOICE_VIDEO: {
                mView.setMessage(R.string.album_not_found_video);
                mView.setMakeImageDisplay(false);
                break;
            }
            case Album.FUNCTION_CHOICE_ALBUM: {
                mView.setMessage(R.string.album_not_found_album);
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
        
        if (!hasCamera) {
            mView.setMakeImageDisplay(false);
            mView.setMakeVideoDisplay(false);
        }
    }
    
    @Override
    public void takePicture() {
        Album.camera(this)
            .image()
            .onResult(mCameraAction)
            .start();
    }
    
    @Override
    public void takeVideo() {
        Album.camera(this)
            .video()
            .quality(mQuality)
            .limitDuration(mLimitDuration)
            .limitBytes(mLimitBytes)
            .onResult(mCameraAction)
            .start();
    }
    
    private Action<AlbumCameraFile> mCameraAction = new Action<AlbumCameraFile>() {
        @Override
        public void onAction(@NonNull AlbumCameraFile result) {
            Intent intent = new Intent();
            intent.putExtra(KEY_OUTPUT_IMAGE_PATH, result);
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}
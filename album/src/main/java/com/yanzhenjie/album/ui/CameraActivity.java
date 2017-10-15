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
package com.yanzhenjie.album.ui;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.PermissionUtils;
import com.yanzhenjie.statusview.StatusUtils;

import java.io.File;
import java.util.Locale;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public class CameraActivity extends Activity {

    private static final String INSTANCE_CAMERA_FUNCTION = "INSTANCE_CAMERA_FUNCTION";
    private static final String INSTANCE_CAMERA_FILE_PATH = "INSTANCE_CAMERA_FILE_PATH";
    private static final String INSTANCE_CAMERA_REQUEST_CODE = "INSTANCE_CAMERA_REQUEST_CODE";
    private static final String INSTANCE_CAMERA_QUALITY = "INSTANCE_CAMERA_QUALITY";
    private static final String INSTANCE_CAMERA_DURATION = "INSTANCE_CAMERA_DURATION";
    private static final String INSTANCE_CAMERA_BYTES = "INSTANCE_CAMERA_BYTES";

    private static final int PERMISSION_IMAGE = 1;
    private static final int PERMISSION_VIDEO = 2;

    private static final int REQUEST_CODE_CAMERA_IMAGE = 1;
    private static final int REQUEST_CODE_CAMERA_VIDEO = 2;

    public static Action<String> sResult;
    public static Action<String> sCancel;
    private int mRequestCode;

    @Album.CameraFunction
    private int mFunction;
    private String mCameraFilePath;
    @IntRange(from = 0, to = 1)
    private int mQuality = 1;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitDuration;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitBytes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setStatusBarColor(this, Color.TRANSPARENT);
        // Language.
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);
        if (savedInstanceState != null &&
                savedInstanceState.containsKey(INSTANCE_CAMERA_FUNCTION) &&
                savedInstanceState.containsKey(INSTANCE_CAMERA_REQUEST_CODE) &&
                savedInstanceState.containsKey(INSTANCE_CAMERA_FILE_PATH)) {
            //noinspection WrongConstant
            mFunction = savedInstanceState.getInt(INSTANCE_CAMERA_FUNCTION);
            mRequestCode = savedInstanceState.getInt(INSTANCE_CAMERA_REQUEST_CODE);
            mCameraFilePath = savedInstanceState.getString(INSTANCE_CAMERA_FILE_PATH);
            mQuality = savedInstanceState.getInt(INSTANCE_CAMERA_QUALITY, 1);
            mLimitDuration = savedInstanceState.getLong(INSTANCE_CAMERA_DURATION, Long.MAX_VALUE);
            mLimitBytes = savedInstanceState.getLong(INSTANCE_CAMERA_BYTES, Long.MAX_VALUE);
        } else {
            Intent intent = getIntent();
            //noinspection WrongConstant
            mFunction = intent.getIntExtra(Album.KEY_INPUT_FUNCTION, 0);
            mRequestCode = intent.getIntExtra(Album.KEY_INPUT_REQUEST_CODE, 0);
            mCameraFilePath = intent.getStringExtra(Album.KEY_INPUT_FILE_PATH);
            mQuality = intent.getIntExtra(Album.KEY_INPUT_CAMERA_QUALITY, 1);
            mLimitDuration = intent.getLongExtra(Album.KEY_INPUT_CAMERA_DURATION, Long.MAX_VALUE);
            mLimitBytes = intent.getLongExtra(Album.KEY_INPUT_CAMERA_BYTES, Long.MAX_VALUE);

            switch (mFunction) {
                case Album.FUNCTION_CAMERA_IMAGE: {
                    if (TextUtils.isEmpty(mCameraFilePath)) mCameraFilePath = AlbumUtils.randomJPGPath();
                    requestPermission(PERMISSION_IMAGE);
                    break;
                }
                case Album.FUNCTION_CAMERA_VIDEO: {
                    if (TextUtils.isEmpty(mCameraFilePath)) mCameraFilePath = AlbumUtils.randomMP4Path();
                    requestPermission(PERMISSION_VIDEO);
                    break;
                }
                default: {
                    onCameraCancel();
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_CAMERA_FUNCTION, mFunction);
        outState.putInt(INSTANCE_CAMERA_REQUEST_CODE, mRequestCode);
        outState.putString(INSTANCE_CAMERA_FILE_PATH, mCameraFilePath);
        outState.putInt(INSTANCE_CAMERA_QUALITY, mQuality);
        outState.putLong(INSTANCE_CAMERA_DURATION, mLimitDuration);
        outState.putLong(INSTANCE_CAMERA_BYTES, mLimitBytes);
        super.onSaveInstanceState(outState);
    }

    /**
     * Scan, but unknown permissions.
     *
     * @param requestCode request code.
     */
    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions;
            if (mFunction == Album.FUNCTION_CAMERA_IMAGE) {
                permissions = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                };
            } else {
                permissions = new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                };
            }
            String[] deniedPermissions = PermissionUtils.getDeniedPermissions(this, permissions);
            if (deniedPermissions.length == 0) {
                dispatchGrantedPermission(requestCode);
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        deniedPermissions,
                        requestCode);
            }
        } else {
            dispatchGrantedPermission(requestCode);
        }
    }

    /**
     * Dispatch granted permission.
     */
    private void dispatchGrantedPermission(int requestCode) {
        switch (requestCode) {
            case PERMISSION_IMAGE: {
                AlbumUtils.imageCapture(this, REQUEST_CODE_CAMERA_IMAGE, new File(mCameraFilePath));
                break;
            }
            case PERMISSION_VIDEO: {
                AlbumUtils.videoCapture(this, REQUEST_CODE_CAMERA_VIDEO, new File(mCameraFilePath),
                        mQuality, mLimitDuration, mLimitBytes);
                break;
            }
            default: {
                onCameraCancel();
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_IMAGE:
            case PERMISSION_VIDEO: {
                if (PermissionUtils.isGrantedResult(grantResults)) dispatchGrantedPermission(requestCode);
                else albumPermissionDenied();
                break;
            }
            default: {
                onCameraCancel();
                break;
            }
        }
    }

    /**
     * The permission for Album is denied.
     */
    private void albumPermissionDenied() {
        int messageRes;
        switch (mFunction) {
            case Album.FUNCTION_CAMERA_IMAGE: {
                messageRes = R.string.album_permission_camera_image_failed_hint;
                break;
            }
            case Album.FUNCTION_CAMERA_VIDEO: {
                messageRes = R.string.album_permission_camera_video_failed_hint;
                break;
            }
            default: {
                onCameraCancel();
                return;
            }
        }
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.album_title_permission_failed)
                .setMessage(messageRes)
                .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onCameraCancel();
                    }
                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_IMAGE:
            case REQUEST_CODE_CAMERA_VIDEO: {
                if (resultCode == RESULT_OK) {
                    onCameraResult();
                } else {
                    onCameraCancel();
                }
                break;
            }
            default: {
                onCameraCancel();
                break;
            }
        }
    }

    private void onCameraResult() {
        if (sResult != null)
            sResult.onAction(mRequestCode, mCameraFilePath);
        setResult(RESULT_OK);
        finish();
    }

    private void onCameraCancel() {
        if (sCancel != null)
            sCancel.onAction(mRequestCode, "User canceled.");
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        sResult = null;
        sCancel = null;
        super.onDestroy();
    }
}

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
package com.yanzhenjie.album.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.alertdialog.AlertDialog;
import com.yanzhenjie.fragment.NoFragment;

import java.io.File;
import java.util.Random;

/**
 * <p>Fragment with camera function.</p>
 * Created by yanzhenjie on 17-3-29.
 */
abstract class BasicCameraFragment extends NoFragment {

    private static final String INSTANCE_CAMERA_FILE_PATH = "INSTANCE_CAMERA_FILE_PATH";

    private static final int PERMISSION_REQUEST_CAMERA = 300;
    private static final int REQUEST_CODE_ACTIVITY_CAMERA = 300;

    private String mCameraFilePath;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(INSTANCE_CAMERA_FILE_PATH, mCameraFilePath);
        super.onSaveInstanceState(outState);
    }

    /**
     * A random name for the image path.
     *
     * @return image path for jpg.
     */
    protected String randomJPGPath() {
        String outFileFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        int index = new Random().nextInt(1000);
        String outFilePath = AlbumUtils.getNowDateTime("yyyyMMdd_HHmmssSSS") + "_" + index + ".jpg";
        File file = new File(outFileFolder, outFilePath);
        return file.getAbsolutePath();
    }

    /**
     * Camera, but unknown permissions.
     */
    protected void cameraUnKnowPermission(String filePath) {
        this.mCameraFilePath = filePath;
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                cameraWithPermission();
            } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        } else {
            cameraWithPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA: {
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    cameraWithPermission();
                } else {
                    AlertDialog.build(getContext())
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_camera_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onUserCamera();
                                }
                            })
                            .show();
                }
                break;
            }
            default: {
                break;
            }
        }
    }


    /**
     * Camera, has been given permission.
     */
    private void cameraWithPermission() {
        AlbumUtils.startCamera(this, REQUEST_CODE_ACTIVITY_CAMERA, new File(mCameraFilePath));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_ACTIVITY_CAMERA) {
            if (resultCode == RESULT_OK) onCameraBack(mCameraFilePath);
            else onUserCamera();
        }
    }

    /**
     * After the camera is finished.
     *
     * @param imagePath file path.
     */
    protected abstract void onCameraBack(String imagePath);

    /**
     * Failed to apply for permission.
     */
    protected void onUserCamera() {
    }


}

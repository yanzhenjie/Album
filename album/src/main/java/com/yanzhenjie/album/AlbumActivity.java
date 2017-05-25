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
package com.yanzhenjie.album;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Window;
import android.view.WindowManager;

import com.yanzhenjie.album.fragment.AlbumFragment;
import com.yanzhenjie.album.fragment.CameraFragment;
import com.yanzhenjie.album.fragment.GalleryFragment;
import com.yanzhenjie.album.impl.AlbumCallback;
import com.yanzhenjie.album.impl.CameraCallback;
import com.yanzhenjie.album.impl.GalleryCallback;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.fragment.CompatActivity;
import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.mediascanner.MediaScanner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * <p>Responsible for controlling the album data and the overall logic.</p>
 * Created by Yan Zhenjie on 2016/10/17.
 */
public class AlbumActivity extends CompatActivity implements AlbumCallback, GalleryCallback, CameraCallback {

    private static final int PERMISSION_REQUEST_STORAGE_ALBUM = 200;
    private static final int PERMISSION_REQUEST_STORAGE_GALLERY = 201;

    private List<String> mCheckedPaths;
    private Bundle mArgument;

    @Override
    protected int fragmentLayoutId() {
        return R.id.album_root_frame_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.initScreen(this);
        // Language.
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);

        setContentView(R.layout.album_activity_main);

        // prepare color.
        Intent intent = getIntent();
        mArgument = intent.getExtras();

        // basic.
        int statusBarColor = intent.getIntExtra(UIWrapper.KEY_INPUT_STATUS_COLOR,
                ContextCompat.getColor(this, R.color.album_ColorPrimaryDark));
        int navigationBarColor = intent.getIntExtra(UIWrapper.KEY_INPUT_NAVIGATION_COLOR,
                ContextCompat.getColor(this, R.color.album_ColorPrimaryBlack));
        mCheckedPaths = intent.getStringArrayListExtra(UIWrapper.KEY_INPUT_CHECKED_LIST);

        setWindowBarColor(statusBarColor, navigationBarColor);

        // Function dispatch.
        final int function = intent.getIntExtra(BasicWrapper.KEY_INPUT_FRAMEWORK_FUNCTION,
                BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM);
        switch (function) {
            case BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM: {
                requestPermission(PERMISSION_REQUEST_STORAGE_ALBUM);
                break;
            }
            case BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_GALLERY: {
                if (mCheckedPaths == null || mCheckedPaths.size() == 0) finish();
                else requestPermission(PERMISSION_REQUEST_STORAGE_GALLERY);
                break;
            }
            case BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_CAMERA: {
                CameraFragment fragment = NoFragment.instantiate(this, CameraFragment.class, mArgument);
                startFragment(fragment);
                break;
            }
            default: {
                finish();
                break;
            }
        }
    }

    /**
     * Set window bar color.
     *
     * @param statusColor     status bar color.
     * @param navigationColor navigation bar color.
     */
    private void setWindowBarColor(@ColorInt int statusColor, @ColorInt int navigationColor) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(statusColor);
            window.setNavigationBarColor(navigationColor);
        }
    }

    /**
     * Scan, but unknown permissions.
     *
     * @param requestCode request code.
     */
    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            int permissionResult = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                onRequestPermissionsResult(
                        requestCode,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        new int[]{PackageManager.PERMISSION_GRANTED});
            } else {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            }
        } else {
            onRequestPermissionsResult(
                    requestCode,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new int[]{PackageManager.PERMISSION_GRANTED});
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_STORAGE_ALBUM: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AlbumFragment albumFragment = NoFragment.instantiate(this, AlbumFragment.class, mArgument);
                    startFragment(albumFragment);
                } else {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_storage_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onAlbumCancel();
                                }
                            })
                            .show();
                }
                break;
            }
            case PERMISSION_REQUEST_STORAGE_GALLERY: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GalleryFragment galleryFragment = NoFragment.instantiate(this, GalleryFragment.class, mArgument);
                    galleryFragment.bindImagePaths(mCheckedPaths);
                    startFragment(galleryFragment);
                } else {
                    onAlbumResult(new ArrayList<>(mCheckedPaths));
                }
                break;
            }
        }
    }

    @Override
    public void onAlbumResult(ArrayList<String> imagePathList) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Album.KEY_OUTPUT_IMAGE_PATH_LIST, imagePathList);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onAlbumCancel() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void onGalleryResult(ArrayList<String> imagePathList) {
        onAlbumResult(imagePathList);
    }

    @Override
    public void onGalleryCancel() {
        onAlbumCancel();
    }

    @Override
    public void onCameraResult(String imagePath) {
        // Add media library.
        new MediaScanner(this).scan(imagePath);
        ArrayList<String> pathList = new ArrayList<>();
        pathList.add(imagePath);
        onAlbumResult(pathList);
    }

    @Override
    public void onCameraCancel() {
        onAlbumCancel();
    }
}
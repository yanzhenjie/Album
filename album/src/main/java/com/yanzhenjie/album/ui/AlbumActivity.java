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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.AlbumCallback;
import com.yanzhenjie.album.task.ThumbnailBuildTask;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.util.PermissionUtils;
import com.yanzhenjie.fragment.CompatActivity;
import com.yanzhenjie.statusview.StatusUtils;

import java.util.ArrayList;
import java.util.Locale;

/**
 * <p>Responsible for controlling the album data and the overall logic.</p>
 * Created by Yan Zhenjie on 2016/10/17.
 */
public class AlbumActivity extends CompatActivity implements AlbumCallback {

    private static final int PERMISSION_STORAGE_ALBUM = 1;
    private static final int PERMISSION_STORAGE_IMAGE = 2;
    private static final int PERMISSION_STORAGE_VIDEO = 3;

    public static Action<ArrayList<AlbumFile>> sResult;
    public static Action<String> sCancel;

    private Bundle mArgument;
    private int mRequestCode;

    private boolean isSucceedLightStatus = false;

    @Override
    protected int fragmentLayoutId() {
        return R.id.album_root_frame_layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setFullToStatusBar(this);
        StatusUtils.setFullToNavigationBar(this);
        DisplayUtils.initScreen(this);
        // Language.
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);

        setContentView(R.layout.album_activity_main);

        Intent intent = getIntent();
        initializeWidget();

        mArgument = intent.getExtras();
        mRequestCode = intent.getIntExtra(Album.KEY_INPUT_REQUEST_CODE, 0);

        final int function = intent.getIntExtra(Album.KEY_INPUT_FUNCTION, Album.FUNCTION_CHOICE_ALBUM);
        switch (function) {
            case Album.FUNCTION_CHOICE_ALBUM:
            case Album.FUNCTION_CHOICE_IMAGE:
            case Album.FUNCTION_CHOICE_VIDEO: {
                requestPermission(PERMISSION_STORAGE_VIDEO);
                break;
            }
            default: {
                onAlbumCancel();
            }
        }
    }

    /**
     * Initialize widget.
     */
    private void initializeWidget() {
        Widget widget = getIntent().getParcelableExtra(Album.KEY_INPUT_WIDGET);
        int navigationColor = widget.getNavigationBarColor();

        if (widget.getStyle() == Widget.STYLE_LIGHT) {
            if (StatusUtils.setStatusBarDarkFont(this, true)) {
                isSucceedLightStatus = true;
            }
        }

        StatusUtils.setNavigationBarColor(this, navigationColor);
    }

    /**
     * Successfully set the status bar to light background.
     */
    public boolean isSucceedLightStatus() {
        return isSucceedLightStatus;
    }

    /**
     * Scan, but unknown permissions.
     *
     * @param requestCode request code.
     */
    private void requestPermission(int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permission = new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            String[] deniedPermissions = PermissionUtils.getDeniedPermissions(this, permission);

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
            case PERMISSION_STORAGE_ALBUM:
            case PERMISSION_STORAGE_IMAGE:
            case PERMISSION_STORAGE_VIDEO: {
                AlbumFragment albumFragment = fragment(AlbumFragment.class, mArgument);
                startFragment(albumFragment);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE_ALBUM:
            case PERMISSION_STORAGE_IMAGE:
            case PERMISSION_STORAGE_VIDEO: {
                if (PermissionUtils.isGrantedResult(grantResults)) dispatchGrantedPermission(requestCode);
                else albumPermissionDenied();
                break;
            }
        }
    }

    /**
     * The permission for Album is denied.
     */
    private void albumPermissionDenied() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(R.string.album_title_permission_failed)
                .setMessage(R.string.album_permission_storage_failed_hint)
                .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onAlbumCancel();
                    }
                })
                .show();
    }

    public void onBackPressed() {
        if (!this.onBackStackFragment()) {
            onAlbumCancel();
        }
    }

    @Override
    public void onAlbumResult(ArrayList<AlbumFile> albumFiles) {
        new ThumbnailBuildTask(this, albumFiles, new ThumbnailBuildTask.Callback() {
            @Override
            public void onThumbnailCallback(ArrayList<AlbumFile> albumFiles) {
                if (sResult != null)
                    sResult.onAction(mRequestCode, albumFiles);
                setResult(RESULT_OK);
                finish();
            }
        }).execute();
    }

    @Override
    public void onAlbumCancel() {
        if (sCancel != null)
            sCancel.onAction(mRequestCode, "User canceled.");
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onDestroy() {
        sResult = null;
        super.onDestroy();
    }
}
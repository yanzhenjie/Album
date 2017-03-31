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
import android.widget.Toast;

import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.fragment.AlbumFragment;
import com.yanzhenjie.album.fragment.AlbumNullFragment;
import com.yanzhenjie.album.fragment.AlbumPreviewFragment;
import com.yanzhenjie.album.fragment.Callback;
import com.yanzhenjie.album.fragment.CameraCallback;
import com.yanzhenjie.album.fragment.GalleryFragment;
import com.yanzhenjie.album.task.ScanTask;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.fragment.CompatActivity;
import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.mediascanner.MediaScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Responsible for controlling the album data and the overall logic.</p>
 * Created by Yan Zhenjie on 2016/10/17.
 */
public class AlbumActivity extends CompatActivity implements
        Callback,
        CameraCallback,
        AlbumFragment.Callback,
        ScanTask.Callback,
        GalleryFragment.Callback {

    private static final int PERMISSION_REQUEST_STORAGE_ALBUM = 200;
    private static final int PERMISSION_REQUEST_STORAGE_GALLERY = 201;

    private ScanTask mScanTask;
    private List<AlbumFolder> mAlbumFolders;
    private List<AlbumImage> mCheckedImages = new ArrayList<>(1);
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
        setContentView(R.layout.album_activity_main);

        // prepare color.
        Intent intent = getIntent();
        mArgument = intent.getExtras();

        // basic.
        int statusBarColor = intent.getIntExtra(BasicWrapper.KEY_INPUT_STATUS_COLOR,
                ContextCompat.getColor(this, R.color.albumColorPrimaryDark));
        int navigationBarColor = intent.getIntExtra(BasicWrapper.KEY_INPUT_NAVIGATION_COLOR,
                ContextCompat.getColor(this, R.color.albumColorPrimaryBlack));
        mCheckedPaths = intent.getStringArrayListExtra(BasicWrapper.KEY_INPUT_CHECKED_LIST);

        setWindowBarColor(statusBarColor, navigationBarColor);

        // Function dispatch.
        final int function = intent.getIntExtra(BasicWrapper.KEY_INPUT_FRAMEWORK_FUNCTION,
                BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM);
        switch (function) {
            case BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM: {
                int limitCount = intent.getIntExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, 1);

                // Prepare constraint.
                if (mCheckedPaths != null && mCheckedPaths.size() > limitCount)
                    mCheckedPaths = mCheckedPaths.subList(0, limitCount - 1);

                mScanTask = new ScanTask(this, this, mCheckedImages);
                requestPermission(PERMISSION_REQUEST_STORAGE_ALBUM);
                break;
            }
            case BasicWrapper.VALUE_INPUT_FRAMEWORK_FUNCTION_GALLERY: {
                if (mCheckedPaths == null || mCheckedPaths.size() == 0) finish();
                else requestPermission(PERMISSION_REQUEST_STORAGE_GALLERY);
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
            } else if (permissionResult == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
                int permissionResult = grantResults[0];
                if (permissionResult == PackageManager.PERMISSION_GRANTED) {
                    scanWithPermission();
                } else {
                    new AlertDialog.Builder(this)
                            .setCancelable(false)
                            .setTitle(R.string.album_dialog_permission_failed)
                            .setMessage(R.string.album_permission_storage_failed_hint)
                            .setPositiveButton(R.string.album_dialog_sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doResult(false);
                                }
                            })
                            .show();
                }
                break;
            }
            case PERMISSION_REQUEST_STORAGE_GALLERY: {
                GalleryFragment galleryFragment = NoFragment.instantiate(this, GalleryFragment.class, mArgument);
                galleryFragment.bindImagePaths(mCheckedPaths);
                startFragment(galleryFragment);
                break;
            }
        }
    }

    /**
     * Scan, has been given permission.
     */
    private void scanWithPermission() {
        //noinspection unchecked
        mScanTask.execute(mCheckedPaths);
    }

    @Override
    public void onScanCallback(List<AlbumFolder> albumFolders) {
        this.mAlbumFolders = albumFolders;
        if (mAlbumFolders.get(0).getImages().size() == 0) {
            AlbumNullFragment nullFragment = NoFragment.instantiate(this, AlbumNullFragment.class, mArgument);
            startFragment(nullFragment);
        } else {
            AlbumFragment albumFragment = NoFragment.instantiate(this, AlbumFragment.class, mArgument);
            albumFragment.bindAlbumFolders(mAlbumFolders);
            startFragment(albumFragment);
        }
    }

    @Override
    public void onGalleryCallback(ArrayList<String> imagePaths) {
        Intent intent = new Intent();
        intent.putStringArrayListExtra(Album.KEY_OUTPUT_IMAGE_PATH_LIST, imagePaths);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void doResult(boolean ok) {
        if (ok) {
            int allSize = mAlbumFolders.get(0).getImages().size();
            int checkSize = mCheckedImages.size();
            if (allSize > 0 && checkSize == 0) {
                Toast.makeText(this, R.string.album_check_little, Toast.LENGTH_LONG).show();
            } else if (checkSize == 0) {
                setResult(RESULT_CANCELED);
                finish();
            } else {
                ArrayList<String> pathList = new ArrayList<>();
                for (AlbumImage albumImage : mCheckedImages) {
                    pathList.add(albumImage.getPath());
                }
                onGalleryCallback(pathList);
            }
        } else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }

    @Override
    public void onCheckedChanged(AlbumImage image, boolean isChecked) {
        if (isChecked && !mCheckedImages.contains(image)) mCheckedImages.add(image);
        else if (mCheckedImages.contains(image)) mCheckedImages.remove(image);
    }

    @Override
    public int getCheckedCount() {
        return mCheckedImages.size();
    }

    @Override
    public void onCameraBack(String imagePath) {
        // Add media library.
        new MediaScanner(this).scan(imagePath);
        ArrayList<String> pathList = new ArrayList<>();
        if (mCheckedImages.size() > 0)
            for (AlbumImage albumImage : mCheckedImages) {
                pathList.add(albumImage.getPath());
            }
        pathList.add(imagePath);
        onGalleryCallback(pathList);
    }

    @Override
    public void onPreviewChecked() {
        if (mCheckedImages.size() <= 0) return;
        AlbumPreviewFragment previewFragment = NoFragment.instantiate(this, AlbumPreviewFragment.class, mArgument);
        previewFragment.bindAlbumImages(mCheckedImages, 0);
        startFragment(previewFragment);
    }

    @Override
    public void onPreviewFolder(int folderPosition, int itemPosition) {
        List<AlbumImage> albumImages = mAlbumFolders.get(folderPosition).getImages();
        AlbumPreviewFragment previewFragment = NoFragment.instantiate(this, AlbumPreviewFragment.class, mArgument);
        previewFragment.bindAlbumImages(albumImages, itemPosition);
        startFragment(previewFragment);
    }

}
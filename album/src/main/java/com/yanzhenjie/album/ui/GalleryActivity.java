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
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.ui.adapter.BasicPreviewAdapter;
import com.yanzhenjie.album.ui.adapter.PathPreviewAdapter;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.PermissionUtils;
import com.yanzhenjie.statusview.StatusUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public class GalleryActivity extends AppCompatActivity {

    private static final String TAG = "AlbumGallery";
    private static final int PERMISSION_STORAGE = 1;

    public static Action<ArrayList<String>> sResult;
    public static Action<String> sCancel;

    private Toolbar mToolbar;
    private MenuItem mFinishMenuItem;

    private ViewPager mViewPager;
    private AppCompatCheckBox mCheckBox;

    private int mRequestCode;
    @NonNull
    private Widget mWidget;
    private List<String> mPathList;
    private Map<String, Boolean> mCheckedMap;
    private int mCurrentItemPosition;
    private boolean mCheckable;
    private int mNavigationAlpha;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setFullToStatusBar(this);
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);
        setContentView(R.layout.album_activity_preview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_album_check);

        Intent intent = getIntent();
        mRequestCode = intent.getIntExtra(Album.KEY_INPUT_REQUEST_CODE, 0);
        mWidget = intent.getParcelableExtra(Album.KEY_INPUT_WIDGET);
        mPathList = intent.getStringArrayListExtra(Album.KEY_INPUT_CHECKED_LIST);
        mCurrentItemPosition = intent.getIntExtra(Album.KEY_INPUT_CURRENT_POSITION, 0);
        mCheckable = intent.getBooleanExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, true);
        mNavigationAlpha = intent.getIntExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, 80);

        if (mPathList == null) {
            Log.e(TAG, "Parameter error.",
                    new IllegalArgumentException("The checkedList can be null."));
            onGalleryCancel();
        } else if (mPathList.size() == 0 || mCurrentItemPosition == mPathList.size()) {
            Log.e(TAG, "Parameter error.",
                    new IllegalArgumentException("The currentPosition is " + mCurrentItemPosition + ","
                            + " the checkedList.size() is " + mPathList.size()));
            onGalleryCancel();
        } else {
            mCheckedMap = new HashMap<>();
            for (String path : mPathList) {
                mCheckedMap.put(path, true);
            }
            initializeWidget();
            initializePager();

            requestPermission(PERMISSION_STORAGE);
        }
    }

    /**
     * Initialize widget.
     */
    private void initializeWidget() {
        int navigationColor = mWidget.getNavigationBarColor();
        navigationColor = AlbumUtils.getAlphaColor(navigationColor, mNavigationAlpha);
        StatusUtils.setFullToNavigationBar(this);
        StatusUtils.setNavigationBarColor(this, navigationColor);

        setTitle(mWidget.getTitle());

        if (!mCheckable) {
            findViewById(R.id.bottom_root).setVisibility(View.GONE);
        } else {
            ColorStateList itemSelector = mWidget.getMediaItemCheckSelector();
            mCheckBox.setSupportButtonTintList(itemSelector);

            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isChecked = mCheckBox.isChecked();
                    mCheckedMap.put(mPathList.get(mCurrentItemPosition), isChecked);
                    setCheckedCountUI(getCheckCount());
                }
            });
        }
    }

    /**
     * Initialize ViewPager.
     */
    private void initializePager() {
        if (mPathList != null) {
            if (mPathList.size() > 3)
                mViewPager.setOffscreenPageLimit(3);
            else if (mPathList.size() > 2)
                mViewPager.setOffscreenPageLimit(2);
        }
        mViewPager.addOnPageChangeListener(mPageChangeListener);
    }

    /**
     * Listener of ViewPager changed.
     */
    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            mCurrentItemPosition = position;
            mCheckBox.setChecked(mCheckedMap.get(mPathList.get(mCurrentItemPosition)));
            mToolbar.setSubtitle(mCurrentItemPosition + 1 + " / " + mPathList.size());
        }
    };

    /**
     * Get check item count.
     */
    private int getCheckCount() {
        int checkedCount = 0;
        for (Map.Entry<String, Boolean> entry : mCheckedMap.entrySet()) {
            if (entry.getValue()) checkedCount += 1;
        }
        return checkedCount;
    }

    /**
     * Set the number of selected pictures.
     */
    private void setCheckedCountUI(int count) {
        String finishStr = getString(R.string.album_menu_finish);
        finishStr += "(" + count + " / " + mPathList.size() + ")";
        mFinishMenuItem.setTitle(finishStr);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu_preview, menu);
        mFinishMenuItem = menu.findItem(R.id.album_menu_finish);
        if (!mCheckable)
            mFinishMenuItem.setVisible(false);
        else
            setCheckedCountUI(getCheckCount());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.album_menu_finish) {
            onGalleryResult();
        } else if (id == android.R.id.home) {
            onGalleryCancel();
        }
        return true;
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
            case PERMISSION_STORAGE: {
                BasicPreviewAdapter previewAdapter = new PathPreviewAdapter(this, mPathList);
                mViewPager.setAdapter(previewAdapter);
                mViewPager.setCurrentItem(mCurrentItemPosition);
                mPageChangeListener.onPageSelected(mCurrentItemPosition);
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_STORAGE: {
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
                        onGalleryCancel();
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        onGalleryCancel();
    }

    private void onGalleryResult() {
        if (sResult != null) {
            ArrayList<String> checkedList = new ArrayList<>();
            for (Map.Entry<String, Boolean> entry : mCheckedMap.entrySet()) {
                if (entry.getValue()) checkedList.add(entry.getKey());
            }
            sResult.onAction(mRequestCode, checkedList);
        }
        setResult(RESULT_OK);
        finish();
    }

    private void onGalleryCancel() {
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
/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;

import com.yanzhenjie.album.AlbumActivity;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.adapter.PreviewAdapter;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.impl.OnCompatCompoundCheckListener;
import com.yanzhenjie.album.task.Poster;
import com.yanzhenjie.album.util.SelectorUtils;

import java.util.List;


/**
 * <p>用户点击某个图片，浏览这个图片所在文件夹内的大图。</p>
 * Created by Yan Zhenjie on 2016/10/19.
 */
public class AlbumPreviewDialog extends AppCompatDialog {

    private AlbumActivity mAlbumActivity;

    private Toolbar mToolbar;
    private MenuItem mFinishMenuItem;

    private AppCompatCheckBox mCheckBox;
    private OnCompatCompoundCheckListener mCheckListener;
    private int mCheckedImagePosition;

    private ViewPager mViewPager;
    private List<AlbumImage> mAlbumImages;

    private boolean isOpen = true;

    public AlbumPreviewDialog(AlbumActivity albumActivity, int toolbarColor, List<AlbumImage> albumImages, OnCompatCompoundCheckListener checkListener, int clickItemPosition, int contentHeight) {
        super(albumActivity, R.style.AlbumDialogStyle_Preview);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.album_dialog_album_preview);

        this.mAlbumActivity = albumActivity;
        this.mCheckListener = checkListener;
        this.mAlbumImages = albumImages;

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCheckBox = (AppCompatCheckBox) findViewById(R.id.cb_album_check);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        initializeToolbar(toolbarColor);
        initializeCheckBox(toolbarColor);
        initializeViewPager(clickItemPosition, contentHeight);
        setMenuItemTitle();
    }

    private void initializeToolbar(int toolbarColor) {
        mToolbar.setBackgroundColor(toolbarColor);
        mToolbar.getBackground().mutate().setAlpha(200);

        mToolbar.inflateMenu(R.menu.menu_dialog_album);
        mFinishMenuItem = mToolbar.getMenu().findItem(R.id.menu_gallery_finish);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mAlbumActivity.toResult(false);
                return true;
            }
        });
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    isOpen = false;
                    Poster.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dismiss();
                            isOpen = true;
                        }
                    }, 200);
                }
            }
        });
    }

    private void initializeCheckBox(int checkColor) {
        mCheckBox.setSupportButtonTintList(SelectorUtils.createColorStateList(Color.WHITE, checkColor));
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCheckListener.onCheck(buttonView, mCheckedImagePosition, isChecked);
                setMenuItemTitle();
            }
        });
    }

    private void initializeViewPager(int currentItem, int contentHeight) {
        if (mAlbumImages.size() > 2)
            mViewPager.setOffscreenPageLimit(2);

        PreviewAdapter previewAdapter = new PreviewAdapter(mAlbumImages, contentHeight);
        mViewPager.setAdapter(previewAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCheckedImagePosition = position;
                AlbumImage albumImage = mAlbumImages.get(mCheckedImagePosition);
                mCheckBox.setChecked(albumImage.isChecked());
                mToolbar.setTitle(mCheckedImagePosition + 1 + " / " + mAlbumImages.size());
            }
        };
        mViewPager.addOnPageChangeListener(pageChangeListener);
        pageChangeListener.onPageSelected(0);
        mViewPager.setCurrentItem(currentItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(-1, -2);
    }

    /**
     * 设置Menu的选中文字。
     */
    private void setMenuItemTitle() {
        String finishStr = mAlbumActivity.getString(R.string.album_menu_finish);
        finishStr += "(" + mAlbumActivity.getCheckedImagesSize() + " / " + mAlbumActivity.getAllowCheckCount() + ")";
        mFinishMenuItem.setTitle(finishStr);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
        }
        return true;
    }
}
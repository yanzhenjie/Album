/*
 * Copyright © 2016 Yan Zhenjie.
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

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.ui.adapter.AlbumFolderAdapter;

import java.util.List;

/**
 * <p>Folder preview.</p>
 * Created by Yan Zhenjie on 2016/10/18.
 */
public class AlbumFolderDialog extends BottomSheetDialog {

    private AlbumFolderAdapter mFolderAdapter;
    private List<AlbumFolder> mAlbumFolders;

    private int mCurrentPosition = 0;
    private BottomSheetBehavior bottomSheetBehavior;
    private OnItemClickListener mItemClickListener;

    public AlbumFolderDialog(@NonNull Context context,
                             Widget widget,
                             @Nullable List<AlbumFolder> albumFolders,
                             @Nullable OnItemClickListener itemClickListener) {
        super(context, R.style.album_DialogStyle_Folder);
        setContentView(R.layout.album_dialog_floder);

        setWindowBarColor(widget.getStatusBarColor(), widget.getNavigationBarColor());
        fixRestart();

        this.mAlbumFolders = albumFolders;
        this.mItemClickListener = itemClickListener;

        RecyclerView recyclerView = getDelegate().findViewById(R.id.rv_content_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFolderAdapter = new AlbumFolderAdapter(context, mAlbumFolders, widget.getBucketItemCheckSelector());
        mFolderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                behaviorHide();
                if (mCurrentPosition != position) {
                    mAlbumFolders.get(mCurrentPosition).setChecked(false);
                    mFolderAdapter.notifyItemChanged(mCurrentPosition);

                    mCurrentPosition = position;
                    mAlbumFolders.get(mCurrentPosition).setChecked(true);
                    mFolderAdapter.notifyItemChanged(mCurrentPosition);

                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, position);
                    }
                }
            }
        });
        recyclerView.setAdapter(mFolderAdapter);
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
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(statusColor);
                window.setNavigationBarColor(navigationColor);
            }
        }
    }

    /**
     * Fix reshow.
     */
    private void fixRestart() {
        View view = findViewById(android.support.design.R.id.design_bottom_sheet);
        if (view == null) return;
        bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    /**
     * Dismiss dialog.
     */
    public void behaviorHide() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}

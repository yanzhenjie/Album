/*
 * Copyright 2016 Yan Zhenjie.
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

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.DisplayHelper;

import java.util.List;

/**
 * <p>Folder preview.</p>
 * Created by Yan Zhenjie on 2016/10/18.
 */
public class FolderDialog extends BottomSheetDialog {

    private Widget mWidget;
    private FolderAdapter mFolderAdapter;
    private List<AlbumFolder> mAlbumFolders;

    private int mCurrentPosition = 0;
    private OnItemClickListener mItemClickListener;

    private BottomSheetBehavior mBehavior;

    public FolderDialog(Context context, Widget widget, List<AlbumFolder> albumFolders, OnItemClickListener itemClickListener) {
        super(context, R.style.Album_Dialog_Folder);
        setContentView(R.layout.album_dialog_floder);
        this.mWidget = widget;
        this.mAlbumFolders = albumFolders;
        this.mItemClickListener = itemClickListener;

        defineBehavior();

        RecyclerView recyclerView = getDelegate().findViewById(R.id.rv_content_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFolderAdapter = new FolderAdapter(context, mAlbumFolders, widget.getBucketItemCheckSelector());
        mFolderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                mBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(DisplayHelper.getInstance().getMinSize(), -1);
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(mWidget.getNavigationBarColor());
            }
        }
    }

    private void defineBehavior() {
        setCanceledOnTouchOutside(false);
        FrameLayout bottomSheet = findViewById(android.support.design.R.id.design_bottom_sheet);
        assert bottomSheet != null;
        mBehavior = BottomSheetBehavior.from(bottomSheet);
        mBehavior.setHideable(true);
    }
}
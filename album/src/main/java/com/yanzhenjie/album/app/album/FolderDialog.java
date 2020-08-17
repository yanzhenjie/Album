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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;

import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;

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

    public FolderDialog(Context context, Widget widget, List<AlbumFolder> albumFolders, OnItemClickListener itemClickListener) {
        super(context, R.style.Album_Dialog_Folder);
        setContentView(R.layout.album_dialog_floder);
        this.mWidget = widget;
        this.mAlbumFolders = albumFolders;
        this.mItemClickListener = itemClickListener;

        RecyclerView recyclerView = getDelegate().findViewById(R.id.rv_content_list);
        assert recyclerView != null;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mFolderAdapter = new FolderAdapter(context, mAlbumFolders, widget.getBucketItemCheckSelector());
        mFolderAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
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
                dismiss();
            }
        });
        recyclerView.setAdapter(mFolderAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            if (Build.VERSION.SDK_INT >= 17) display.getRealMetrics(metrics);
            else display.getMetrics(metrics);
            int minSize = Math.min(metrics.widthPixels, metrics.heightPixels);
            window.setLayout(minSize, -1);
            if (Build.VERSION.SDK_INT >= 21) {
                window.setStatusBarColor(Color.TRANSPARENT);
                window.setNavigationBarColor(mWidget.getNavigationBarColor());
            }
        }
    }
}
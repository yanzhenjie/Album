/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.dialog;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.adapter.DialogFolderAdapter;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.impl.OnCompatItemClickListener;
import com.yanzhenjie.album.task.Poster;
import com.yanzhenjie.album.util.SelectorUtils;

import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/10/18.
 */
public class AlbumFolderDialog extends BottomSheetDialog {

    private int checkPosition = 0;
    private BottomSheetBehavior bottomSheetBehavior;
    private OnCompatItemClickListener mItemClickListener;

    private boolean isOpen = true;

    public AlbumFolderDialog(@NonNull Context context, @ColorInt int toolbarColor, @Nullable List<AlbumFolder> albumFolders, @Nullable OnCompatItemClickListener itemClickListener) {
        super(context, R.style.AlbumDialogStyle_Folder);
        setContentView(R.layout.album_dialog_album_floder);
        mItemClickListener = itemClickListener;

        fixRestart();

        RecyclerView rvContentList = (RecyclerView) findViewById(R.id.rv_content_list);
        rvContentList.setHasFixedSize(true);
        rvContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContentList.setAdapter(new DialogFolderAdapter(SelectorUtils.createColorStateList(ContextCompat.getColor(context, R.color.albumPrimaryBlack), toolbarColor), albumFolders, new OnCompatItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if (isOpen) { // 反应太快，按钮点击效果出不来，故加延迟。
                    isOpen = false;
                    Poster.getInstance().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            behaviorHide();
                            if (mItemClickListener != null && checkPosition != position) {
                                checkPosition = position;
                                mItemClickListener.onItemClick(view, position);
                            }
                            isOpen = true;
                        }
                    }, 200);
                }
            }
        }));
        setStatusBarColor(toolbarColor);
    }

    private void setStatusBarColor(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            final Window window = getWindow();
            if (window != null) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(color);
                window.setNavigationBarColor(ContextCompat.getColor(getContext(), R.color.albumPrimaryBlack));
            }
        }
    }

    /**
     * 修复不能重新显示的bug。
     */
    private void fixRestart() {
        View view = findViewById(android.support.design.R.id.design_bottom_sheet);
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
     * 关闭dialog。
     */
    public void behaviorHide() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
}

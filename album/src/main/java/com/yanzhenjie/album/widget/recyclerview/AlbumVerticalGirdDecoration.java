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
package com.yanzhenjie.album.widget.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * <p>RecyclerView Decoration.</p>
 * Created by Yan Zhenjie on 2016/9/23.
 */
public class AlbumVerticalGirdDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public AlbumVerticalGirdDecoration(Drawable drawable) {
        mDivider = drawable;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        boolean lastRaw = isLastRaw(position, spanCount, childCount);
        boolean lastColumn = isLastColumn(position, spanCount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            boolean isFirstRaw = isFirstRaw(position, spanCount);
            if (isFirstRaw && lastColumn) { // draw left, top, right, bottom
                outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(), mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
            } else if (isFirstRaw) { // draw left, top, bottom
                outRect.set(mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight(), 0, mDivider.getIntrinsicHeight());
            } else if (lastRaw && lastColumn) { // draw left, right.
                outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
            } else if (lastRaw) { // draw left.
                outRect.set(mDivider.getIntrinsicWidth(), 0, 0, mDivider.getIntrinsicHeight());
            } else if (lastColumn) { // draw left, right, bottom.
                outRect.set(mDivider.getIntrinsicWidth(), 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
            } else { // draw left, bottom.
                outRect.set(mDivider.getIntrinsicWidth(), 0, 0, mDivider.getIntrinsicHeight());
            }
        } else {
            if (lastRaw && lastColumn) { // nothing.
                outRect.set(0, 0, 0, 0);
            } else if (lastRaw) { // draw right.
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            } else if (lastColumn) { // draw bottom.
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else { // draw right, bottom.
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
            }
        }
    }

    private int getSpanCount(RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return 1;
    }

    private boolean isFirstRaw(int position, int spanCount) {
        return position < spanCount;
    }

    private boolean isLastRaw(int position, int spanCount, int childCount) {
        if (spanCount == 1) return position == childCount;
        else {
            int lastRawItemCount = childCount % spanCount;
            int rawCount = (childCount - lastRawItemCount) / spanCount + (lastRawItemCount > 0 ? 1 : 0);

            int positionRawCount = (position + 1) % spanCount;
            if (positionRawCount == 0) {
                return rawCount == (position + 1) / spanCount;
            } else {
                return rawCount == (position + 1 - positionRawCount) / spanCount + 1;
            }
        }
    }

    private boolean isLastColumn(int position, int spanCount) {
        return spanCount == 1 || (position + 1) % spanCount == 0;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getLeft();
            final int top = child.getBottom();
            final int right = child.getRight();
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getRight();
            final int top = child.getTop();
            final int right = left + mDivider.getIntrinsicWidth();
            final int bottom = child.getBottom();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

}
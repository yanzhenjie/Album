/*
 * Copyright 2017 Yan Zhenjie
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
package com.yanzhenjie.album.widget.divider;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by YanZhenjie on 2017/8/14.
 */
public class Api21ItemDivider extends Divider {

    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;

    /**
     * @param color divider line color.
     */
    public Api21ItemDivider(@ColorInt int color) {
        this(color, 4, 4);
    }

    /**
     * @param color         line color.
     * @param dividerWidth  line width.
     * @param dividerHeight line height.
     */
    public Api21ItemDivider(@ColorInt int color, int dividerWidth, int dividerHeight) {
        mDivider = new ColorDrawable(color);
        mDividerWidth = dividerWidth;
        mDividerHeight = dividerHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        int columnCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();

        boolean firstRaw = isFirstRaw(position, columnCount);
        boolean lastRaw = isLastRaw(position, columnCount, childCount);
        boolean firstColumn = isFirstColumn(position, columnCount);
        boolean lastColumn = isLastColumn(position, columnCount);

        if (columnCount == 1) {
            if (firstRaw) {
                outRect.set(mDividerWidth, mDividerHeight, mDividerWidth, mDividerHeight / 2);
            } else if (lastRaw) {
                outRect.set(mDividerWidth, mDividerHeight / 2, mDividerWidth, mDividerHeight);
            } else {
                outRect.set(mDividerWidth, mDividerHeight / 2, mDividerWidth, mDividerHeight / 2);
            }
        } else {
            if (firstRaw && firstColumn) {
                outRect.set(mDividerWidth, mDividerHeight, mDividerWidth / 2, mDividerHeight / 2);
            } else if (firstRaw && lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight, mDividerWidth, mDividerHeight / 2);
            } else if (firstRaw) {
                outRect.set(mDividerWidth / 2, mDividerHeight, mDividerWidth / 2, mDividerHeight / 2);
            } else if (lastRaw && firstColumn) {
                outRect.set(mDividerWidth, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight);
            } else if (lastRaw && lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth, mDividerHeight);
            } else if (lastRaw) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight);
            } else if (firstColumn) {
                outRect.set(mDividerWidth, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight / 2);
            } else if (lastColumn) {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth, mDividerHeight / 2);
            } else {
                outRect.set(mDividerWidth / 2, mDividerHeight / 2, mDividerWidth / 2, mDividerHeight / 2);
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

    private boolean isFirstRaw(int position, int columnCount) {
        return position < columnCount;
    }

    private boolean isLastRaw(int position, int columnCount, int childCount) {
        if (columnCount == 1)
            return position + 1 == childCount;
        else {
            int lastRawItemCount = childCount % columnCount;
            int rawCount = (childCount - lastRawItemCount) / columnCount + (lastRawItemCount > 0 ? 1 : 0);

            int rawPositionJudge = (position + 1) % columnCount;
            if (rawPositionJudge == 0) {
                int rawPosition = (position + 1) / columnCount;
                return rawCount == rawPosition;
            } else {
                int rawPosition = (position + 1 - rawPositionJudge) / columnCount + 1;
                return rawCount == rawPosition;
            }
        }
    }

    private boolean isFirstColumn(int position, int columnCount) {
        if (columnCount == 1)
            return true;
        return position % columnCount == 0;
    }

    private boolean isLastColumn(int position, int columnCount) {
        if (columnCount == 1)
            return true;
        return (position + 1) % columnCount == 0;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        c.save();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getLeft();
            final int top = child.getBottom();
            final int right = child.getRight();
            final int bottom = top + mDividerHeight;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        c.save();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final int left = child.getRight();
            final int top = child.getTop();
            final int right = left + mDividerWidth;
            final int bottom = child.getBottom();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
        c.restore();
    }

    @Override
    public int getHeight() {
        return mDividerHeight;
    }

    @Override
    public int getWidth() {
        return mDividerWidth;
    }
}

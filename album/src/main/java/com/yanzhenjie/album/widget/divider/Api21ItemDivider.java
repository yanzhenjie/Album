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
import androidx.annotation.ColorInt;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * <p>The implementation of divider adds dividers around the list.</p>
 * Created by YanZhenjie on 2017/8/14.
 */
public class Api21ItemDivider extends Divider {

    private final int mWidth;
    private final int mHeight;
    private final Drawer mDrawer;

    /**
     * @param color divider line color.
     */
    public Api21ItemDivider(@ColorInt int color) {
        this(color, 4, 4);
    }

    /**
     * @param color  line color.
     * @param width  line width.
     * @param height line height.
     */
    public Api21ItemDivider(@ColorInt int color, int width, int height) {
        this.mWidth = Math.round(width / 2F);
        this.mHeight = Math.round(height / 2F);
        this.mDrawer = new ColorDrawer(color, mWidth, mHeight);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(mWidth, mHeight, mWidth, mHeight);
    }

    @Override
    public void onDraw(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        canvas.save();
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int childCount = layoutManager.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View view = layoutManager.getChildAt(i);
            mDrawer.drawLeft(view, canvas);
            mDrawer.drawTop(view, canvas);
            mDrawer.drawRight(view, canvas);
            mDrawer.drawBottom(view, canvas);
        }
        canvas.restore();
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }
}
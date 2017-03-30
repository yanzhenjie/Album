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
package com.yanzhenjie.album.widget.loading;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

/**
 * <p>Animation Drawable.</p>
 * Created by yanzhenjie on 17-3-27.
 */
public class LoadingDrawable extends Drawable implements Animatable {
    private final LoadingRenderer mLoadingRender;

    private final Callback mCallback = new Callback() {
        @Override
        public void invalidateDrawable(Drawable d) {
            invalidateSelf();
        }

        @Override
        public void scheduleDrawable(Drawable d, Runnable what, long when) {
            scheduleSelf(what, when);
        }

        @Override
        public void unscheduleDrawable(Drawable d, Runnable what) {
            unscheduleSelf(what);
        }
    };

    public LoadingDrawable(LoadingRenderer loadingRender) {
        this.mLoadingRender = loadingRender;
        this.mLoadingRender.setCallback(mCallback);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mLoadingRender.setBounds(bounds);
    }

    @Override
    public void draw(Canvas canvas) {
        if (!getBounds().isEmpty()) {
            this.mLoadingRender.draw(canvas);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        this.mLoadingRender.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        this.mLoadingRender.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void start() {
        this.mLoadingRender.start();
    }

    @Override
    public void stop() {
        this.mLoadingRender.stop();
    }

    @Override
    public boolean isRunning() {
        return this.mLoadingRender.isRunning();
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) this.mLoadingRender.mHeight;
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) this.mLoadingRender.mWidth;
    }
}

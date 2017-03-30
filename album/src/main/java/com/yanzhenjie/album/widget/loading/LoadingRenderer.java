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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import com.yanzhenjie.album.util.DisplayUtils;

/**
 * <p>Base Renderer.</p>
 * Created by yanzhenjie on 17-3-27.
 */
public abstract class LoadingRenderer {
    private static final long ANIMATION_DURATION = 1333;
    private static final float DEFAULT_SIZE = 56.0f;

    private final ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            computeRender((float) animation.getAnimatedValue());
            invalidateSelf();
        }
    };

    /**
     * Whenever {@link LoadingDrawable} boundary changes mBounds will be updated.
     * More details you can see {@link LoadingDrawable#onBoundsChange(Rect)}
     */
    protected final Rect mBounds = new Rect();

    private Drawable.Callback mCallback;
    private ValueAnimator mRenderAnimator;

    protected long mDuration;

    protected float mWidth;
    protected float mHeight;

    public LoadingRenderer(Context context) {
        mWidth = DisplayUtils.dip2px(DEFAULT_SIZE);
        mHeight = DisplayUtils.dip2px(DEFAULT_SIZE);

        mDuration = ANIMATION_DURATION;

        setupAnimators();
    }

    @Deprecated
    protected void draw(Canvas canvas, Rect bounds) {
    }

    protected void draw(Canvas canvas) {
        draw(canvas, mBounds);
    }

    protected abstract void computeRender(float renderProgress);

    protected abstract void setAlpha(int alpha);

    protected abstract void setColorFilter(ColorFilter cf);

    protected abstract void reset();

    protected void addRenderListener(Animator.AnimatorListener animatorListener) {
        mRenderAnimator.addListener(animatorListener);
    }

    void start() {
        reset();
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);

        mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRenderAnimator.setDuration(mDuration);
        mRenderAnimator.start();
    }

    void stop() {
        mRenderAnimator.removeUpdateListener(mAnimatorUpdateListener);
        mRenderAnimator.setRepeatCount(0);
        mRenderAnimator.setDuration(0);
        mRenderAnimator.end();
    }

    boolean isRunning() {
        return mRenderAnimator.isRunning();
    }

    void setCallback(Drawable.Callback callback) {
        this.mCallback = callback;
    }

    void setBounds(Rect bounds) {
        mBounds.set(bounds);
    }

    private void setupAnimators() {
        mRenderAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        mRenderAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mRenderAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRenderAnimator.setDuration(mDuration);
        mRenderAnimator.setInterpolator(new LinearInterpolator());
        mRenderAnimator.addUpdateListener(mAnimatorUpdateListener);
    }

    private void invalidateSelf() {
        mCallback.invalidateDrawable(null);
    }

}

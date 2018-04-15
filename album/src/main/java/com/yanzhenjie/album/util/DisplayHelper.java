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
package com.yanzhenjie.album.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import android.util.DisplayMetrics;
import android.view.Display;

import static android.support.annotation.RestrictTo.Scope.LIBRARY_GROUP;

/**
 * <p>Display helper.</p>
 * Created by Yan Zhenjie on 2016/7/7.
 */
@RestrictTo(LIBRARY_GROUP)
public class DisplayHelper {

    private static DisplayHelper sInstance;

    public static DisplayHelper getInstance() {
        if (sInstance == null) {
            synchronized (DisplayHelper.class) {
                if (sInstance == null) {
                    sInstance = new DisplayHelper();
                }
            }
        }
        return sInstance;
    }

    private boolean isCreated;

    private int mOrientation;
    private int mTopSize;
    private int mLandscapeSize;
    private int mPortraitSize;

    @RestrictTo(LIBRARY_GROUP)
    public void create(Activity activity) {
        if (isCreated) return;
        this.isCreated = true;

        Resources resources = activity.getResources();
        this.mOrientation = resources.getConfiguration().orientation;

        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        this.mTopSize = resources.getDimensionPixelSize(resourceId);

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();

        if (VERSION.SDK_INT >= 17) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }

        this.mLandscapeSize = metrics.widthPixels;
        this.mPortraitSize = metrics.heightPixels;
    }

    @RestrictTo(LIBRARY_GROUP)
    public void changeConfig(Configuration config) {
        if (mOrientation != config.orientation) {
            mOrientation = config.orientation;
            int tempWidth = mLandscapeSize;
            this.mLandscapeSize = mPortraitSize;
            this.mPortraitSize = tempWidth;
        }
    }

    /**
     * Calculate the size of the Item.
     *
     * @param context     context.
     * @param dividerSize size of divider.
     * @param column      column count.
     * @return item size.
     */
    @RestrictTo(LIBRARY_GROUP)
    public int getItemSize(Context context, int dividerSize, int column) {
        switch (mOrientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                return (mLandscapeSize - dividerSize * (column + 1)) / column;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                int topSize = context.getResources().getDimensionPixelSize(android.support.v7.appcompat.R.dimen.abc_action_bar_default_height_material) + mTopSize;
                return (mPortraitSize - topSize - dividerSize * (column + 1)) / column;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
    }

    /**
     * Get smaller side lengths.
     *
     * @return size.
     */
    @RestrictTo(LIBRARY_GROUP)
    public int getMinSize() {
        return Math.min(mLandscapeSize, mPortraitSize);
    }
}
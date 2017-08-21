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
package com.yanzhenjie.album.util;

import android.app.Activity;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.view.Display;

/**
 * <p>Display helper.</p>
 * Created by Yan Zhenjie on 2016/7/7.
 */
public class DisplayUtils {

    private static boolean isInitialize = false;

    public static int sScreenWidth;
    public static int sScreenHeight;
    private static float mDensity;
    private static float mScaledDensity;

    public static void initScreen(Activity activity) {
        if (isInitialize) return;
        isInitialize = true;

        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();

        if (VERSION.SDK_INT >= 17) {
            display.getRealMetrics(metrics);
        } else {
            display.getMetrics(metrics);
        }

        sScreenWidth = metrics.widthPixels;
        sScreenHeight = metrics.heightPixels;
        mDensity = metrics.density;
        mScaledDensity = metrics.scaledDensity;
    }

    public static int px2dip(float inParam) {
        return (int) (inParam / mDensity + 0.5F);
    }

    public static int dip2px(float inParam) {
        return (int) (inParam * mDensity + 0.5F);
    }

    public static int px2sp(float inParam) {
        return (int) (inParam / mScaledDensity + 0.5F);
    }

    public static int sp2px(float inParam) {
        return (int) (inParam * mScaledDensity + 0.5F);
    }
}
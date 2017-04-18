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
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.yanzhenjie.album.provider.CameraFileProvider;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <p>Helper for camera.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class AlbumUtils {

    /**
     * Start the camera.
     *
     * @param fragment    fragment.
     * @param requestCode code.
     * @param outPath     file path.
     */
    public static void startCamera(Fragment fragment, int requestCode, File outPath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = getUri(fragment.getContext(), outPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Start the camera.
     *
     * @param fragment    fragment.
     * @param requestCode code.
     * @param outPath     file path.
     */
    public static void startCamera(android.app.Fragment fragment, int requestCode, File outPath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = getUri(fragment.getActivity(), outPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Start the camera.
     *
     * @param activity    activity.
     * @param requestCode code.
     * @param outPath     file path.
     */
    public static void startCamera(Activity activity, int requestCode, File outPath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = getUri(activity, outPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Setting {@link Locale} for {@link Context}.
     */
    public static Context applyLanguageForContext(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }

    private static Uri getUri(Context context, File outPath) {
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(outPath);
        } else {
            uri = CameraFileProvider.getUriForFile(context, CameraFileProvider.getFileProviderName(context), outPath);
        }
        return uri;
    }

    /**
     * Format the current time in the specified format.
     *
     * @param format format.
     * @return time string.
     */
    public static String getNowDateTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

}

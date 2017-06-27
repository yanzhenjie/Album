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
package com.yanzhenjie.album;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.album.task.LocalImageLoader;

import java.util.ArrayList;
import java.util.Locale;

/**
 * <p>Entrance.</p>
 * Created by Yan Zhenjie on 2016/10/23.
 */
public final class Album {

    public static String KEY_OUTPUT_IMAGE_PATH_LIST = "KEY_OUTPUT_IMAGE_PATH_LIST";

    private static AlbumConfig sAlbumConfig;

    /**
     * Initialize Album.
     *
     * @param albumConfig {@link AlbumConfig}.
     */
    public static void initialize(AlbumConfig albumConfig) {
        sAlbumConfig = albumConfig;
    }

    /**
     * Get the album configuration.
     */
    public static AlbumConfig getAlbumConfig() {
        if (sAlbumConfig == null) {
            initialize(AlbumConfig.newBuilder(null)
                    .setImageLoader(new LocalImageLoader())
                    .setLocale(Locale.getDefault())
                    .build()
            );
        }
        return sAlbumConfig;
    }

    /**
     * In multi-check mode, open the album.
     */
    public static AlbumWrapper album(Activity activity) {
        return new AlbumWrapper(activity);
    }

    /**
     * In multi-check mode, open the album.
     */
    public static AlbumWrapper album(Fragment fragment) {
        return new AlbumWrapper(fragment);
    }

    /**
     * In multi-check mode, open the album.
     */
    public static AlbumWrapper album(android.app.Fragment fragment) {
        return new AlbumWrapper(fragment);
    }

    /**
     * Open the album in radio mode.
     */
    public static RadioAlbumWrapper albumRadio(Activity activity) {
        return new RadioAlbumWrapper(activity);
    }

    /**
     * Open the album in radio mode.
     */
    public static RadioAlbumWrapper albumRadio(Fragment fragment) {
        return new RadioAlbumWrapper(fragment);
    }

    /**
     * Open the album in radio mode.
     */
    public static RadioAlbumWrapper albumRadio(android.app.Fragment fragment) {
        return new RadioAlbumWrapper(fragment);
    }

    /**
     * Open the gallery from the activity.
     */
    public static GalleryWrapper gallery(Activity activity) {
        return new GalleryWrapper(activity);
    }

    /**
     * Open the gallery from the {@link Fragment}.
     */
    public static GalleryWrapper gallery(Fragment fragment) {
        return new GalleryWrapper(fragment);
    }

    /**
     * Open the gallery from the {@link android.app.Fragment}.
     */
    public static GalleryWrapper gallery(android.app.Fragment fragment) {
        return new GalleryWrapper(fragment);
    }

    /**
     * Open the camera from the activity.
     */
    public static CameraWrapper camera(Activity activity) {
        return new CameraWrapper(activity);
    }

    /**
     * Open the camera from the {@link Fragment}.
     */
    public static CameraWrapper camera(Fragment fragment) {
        return new CameraWrapper(fragment);
    }

    /**
     * Open the camera from the {@link android.app.Fragment}.
     */
    public static CameraWrapper camera(android.app.Fragment fragment) {
        return new CameraWrapper(fragment);
    }

    /**
     * Resolve the selected photo path list.
     *
     * @param intent {@code Intent} from {@code onActivityResult(int, int, Intent)}.
     * @return {@code ArrayList<String>}.
     */
    public static
    @NonNull
    ArrayList<String> parseResult(Intent intent) {
        ArrayList<String> pathList = intent.getStringArrayListExtra(KEY_OUTPUT_IMAGE_PATH_LIST);
        if (pathList == null)
            pathList = new ArrayList<>();
        return pathList;
    }

    /**
     * @deprecated user {@link #album(Activity)} instead.
     */
    @Deprecated
    public static void startAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @deprecated user {@link #album(Activity)} instead.
     */
    @Deprecated
    public static void startAlbum(Activity activity, int requestCode, int limitCount) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, limitCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @deprecated user {@link #album(Activity)} instead.
     */
    @Deprecated
    public static void startAlbum(Activity activity, int requestCode, int limitCount, @ColorInt int toolbarColor, @ColorInt int
            statusBarColor) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, limitCount);
        intent.putExtra(AlbumWrapper.KEY_INPUT_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(AlbumWrapper.KEY_INPUT_STATUS_COLOR, statusBarColor);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * @deprecated user {@link #album(Fragment)} instead.
     */
    @Deprecated
    public static void startAlbum(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @deprecated user {@link #album(Fragment)} instead.
     */
    @Deprecated
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        intent.putExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, limitCount);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * @deprecated user {@link #album(Fragment)} instead.
     */
    @Deprecated
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount, @ColorInt int toolbarColor, @ColorInt int
            statusBarColor) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        intent.putExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, limitCount);
        intent.putExtra(AlbumWrapper.KEY_INPUT_TOOLBAR_COLOR, toolbarColor);
        intent.putExtra(AlbumWrapper.KEY_INPUT_STATUS_COLOR, statusBarColor);
        fragment.startActivityForResult(intent, requestCode);
    }

}

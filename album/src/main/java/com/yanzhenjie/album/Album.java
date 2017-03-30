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

import java.util.ArrayList;

/**
 * <p>Entrance.</p>
 * Created by Yan Zhenjie on 2016/10/23.
 */
public final class Album {

    public static String KEY_OUTPUT_IMAGE_PATH_LIST = "KEY_OUTPUT_IMAGE_PATH_LIST";

    /**
     * Open the album from the activity.
     *
     * @param activity {@link Activity}.
     * @return {@link AlbumWrapper}.
     */
    public static AlbumWrapper album(Activity activity) {
        return new AlbumWrapper(activity);
    }

    /**
     * Open the album from the {@link Fragment}.
     *
     * @param fragment {@link Fragment}.
     * @return {@link AlbumWrapper}.
     */
    public static AlbumWrapper album(Fragment fragment) {
        return new AlbumWrapper(fragment);
    }

    /**
     * Open the album from the {@link android.app.Fragment}.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link AlbumWrapper}.
     */
    public static AlbumWrapper album(android.app.Fragment fragment) {
        return new AlbumWrapper(fragment);
    }

    /**
     * Open the gallery from the activity.
     *
     * @param activity {@link Activity}.
     * @return {@link GalleryWrapper}.
     */
    public static GalleryWrapper gallery(Activity activity) {
        return new GalleryWrapper(activity);
    }

    /**
     * Open the gallery from the {@link Fragment}.
     *
     * @param fragment {@link Fragment}.
     * @return {@link GalleryWrapper}.
     */
    public static GalleryWrapper gallery(Fragment fragment) {
        return new GalleryWrapper(fragment);
    }

    /**
     * Open the gallery from the {@link android.app.Fragment}.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link GalleryWrapper}.
     */
    public static GalleryWrapper gallery(android.app.Fragment fragment) {
        return new GalleryWrapper(fragment);
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
     * Open the album from the {@link Activity}.
     *
     * @param activity    {@link Activity}.
     * @param requestCode request code.
     * @see #album(Activity)
     * @see #album(Fragment)
     * @see #album(android.app.Fragment)
     * @deprecated user {@link #album(Activity)} instead.
     */
    @Deprecated
    public static void startAlbum(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Open the album from the {@link Activity}.
     *
     * @param activity    {@link Activity}.
     * @param requestCode request code.
     * @param limitCount  number of photos to select.
     * @see #album(Activity)
     * @see #album(Fragment)
     * @see #album(android.app.Fragment)
     * @deprecated user {@link #album(Activity)} instead.
     */
    @Deprecated
    public static void startAlbum(Activity activity, int requestCode, int limitCount) {
        Intent intent = new Intent(activity, AlbumActivity.class);
        intent.putExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, limitCount);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * Open the album from the {@link Activity}.
     *
     * @param activity       {@link Activity}.
     * @param requestCode    request code.
     * @param limitCount     number of photos to select.
     * @param toolbarColor   Toolbar color.
     * @param statusBarColor StatusBar color.
     * @see #album(Activity)
     * @see #album(Fragment)
     * @see #album(android.app.Fragment)
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
     * Open the album from the {@link Fragment}.
     *
     * @param fragment    {@link Fragment}.
     * @param requestCode request code.
     * @see #album(Activity)
     * @see #album(Fragment)
     * @see #album(android.app.Fragment)
     * @deprecated user {@link #album(Fragment)} instead.
     */
    @Deprecated
    public static void startAlbum(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Open the album from the {@link Fragment}.
     *
     * @param fragment    {@link Fragment}.
     * @param requestCode request code.
     * @param limitCount  number of photos to select.
     * @see #album(Activity)
     * @see #album(Fragment)
     * @see #album(android.app.Fragment)
     * @deprecated user {@link #album(Fragment)} instead.
     */
    @Deprecated
    public static void startAlbum(Fragment fragment, int requestCode, int limitCount) {
        Intent intent = new Intent(fragment.getContext(), AlbumActivity.class);
        intent.putExtra(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, limitCount);
        fragment.startActivityForResult(intent, requestCode);
    }

    /**
     * Open the album from the {@link Fragment}.
     *
     * @param fragment       {@link Fragment}.
     * @param requestCode    request code.
     * @param limitCount     number of photos to select.
     * @param toolbarColor   Toolbar color.
     * @param statusBarColor StatusBar color.
     * @see #album(Activity)
     * @see #album(Fragment)
     * @see #album(android.app.Fragment)
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

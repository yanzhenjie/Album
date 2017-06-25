package com.yanzhenjie.album;

import android.content.Intent;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by YanZhenjie on 2017/6/25.
 */
public class RadioAlbumWrapper<T extends RadioAlbumWrapper> extends UIWrapper<T> {

    public static final String KEY_INPUT_TITLE = "KEY_INPUT_TITLE";
    public static final String KEY_INPUT_COLUMN_COUNT = "KEY_INPUT_COLUMN_COUNT";
    public static final String KEY_INPUT_LIMIT_COUNT = "KEY_INPUT_LIMIT_COUNT";
    public static final String KEY_INPUT_ALLOW_CAMERA = "KEY_INPUT_ALLOW_CAMERA";

    private Intent mIntent;

    RadioAlbumWrapper(Object o) {
        super(o, VALUE_INPUT_FRAMEWORK_FUNCTION_ALBUM_RADIO);
        mIntent = getIntent();
        mIntent.putExtra(KEY_INPUT_LIMIT_COUNT, 1);
    }

    protected RadioAlbumWrapper(Object o, int function) {
        super(o, function);
    }

    @Override
    public T statusBarColor(@ColorInt int color) {
        mIntent.putExtra(KEY_INPUT_STATUS_COLOR, color);
        return (T) this;
    }

    @Override
    public T toolBarColor(@ColorInt int color) {
        mIntent.putExtra(KEY_INPUT_TOOLBAR_COLOR, color);
        return (T) this;
    }

    @Override
    public T navigationBarColor(@ColorInt int color) {
        mIntent.putExtra(KEY_INPUT_NAVIGATION_COLOR, color);
        return (T) this;
    }

    /**
     * Set the title of ui.
     *
     * @param title string.
     * @return a subclass of {@link BasicWrapper}.
     */
    public T title(@NonNull String title) {
        mIntent.putExtra(KEY_INPUT_TITLE, title);
        return (T) this;
    }

    /**
     * Sets the number of column that the photo shows.
     *
     * @param count count.
     * @return {@link AlbumWrapper}.
     */
    public T columnCount(int count) {
        mIntent.putExtra(KEY_INPUT_COLUMN_COUNT, count);
        return (T) this;
    }

    /**
     * Allow to take pictures.
     *
     * @param camera true, other wise false.
     * @return {@link AlbumWrapper}.
     */
    public T camera(boolean camera) {
        mIntent.putExtra(KEY_INPUT_ALLOW_CAMERA, camera);
        return (T) this;
    }
}

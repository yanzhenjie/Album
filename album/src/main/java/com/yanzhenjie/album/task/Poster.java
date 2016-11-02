/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.task;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Yan Zhenjie on 2016/10/22.
 */
public class Poster extends Handler {

    private static Poster instance;

    /**
     * Get single object.
     *
     * @return {@link Poster}.
     */
    public static Poster getInstance() {
        if (instance == null)
            synchronized (Poster.class) {
                if (instance == null)
                    instance = new Poster();
            }
        return instance;
    }

    private Poster() {
        super(Looper.getMainLooper());
    }
}

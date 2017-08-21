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
package com.yanzhenjie.album.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.api.widget.Widget;

/**
 * <p>Album basic wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public abstract class BasicAlbumWrapper<T extends BasicAlbumWrapper, Result, Checked> {

    @NonNull
    final Context mContext;
    AlbumListener<Result> mAlbumListener;
    int mRequestCode;
    @Nullable
    Widget mWidget;
    @Nullable
    Checked mChecked;

    BasicAlbumWrapper(@NonNull Context context) {
        this.mContext = context;
        mWidget = Widget.getDefaultWidget(context);
    }

    /**
     * Set the results to listener.
     */
    public final T listener(AlbumListener<Result> albumListener) {
        this.mAlbumListener = albumListener;
        return (T) this;
    }

    /**
     * Request tag.
     */
    public final T requestCode(int requestCode) {
        this.mRequestCode = requestCode;
        return (T) this;
    }

    /**
     * Set the widget property.
     */
    public final T widget(@Nullable Widget widget) {
        this.mWidget = widget;
        return (T) this;
    }

    /**
     * Start up.
     */
    public abstract void start();
}

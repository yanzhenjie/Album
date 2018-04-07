/*
 * Copyright © 2017 Yan Zhenjie.
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
import android.support.annotation.Nullable;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.api.widget.Widget;

/**
 * <p>Album basic wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public abstract class BasicAlbumWrapper<Returner extends BasicAlbumWrapper, Result, Cancel, Checked> {

    final Context mContext;
    Action<Result> mResult;
    Action<Cancel> mCancel;
    Widget mWidget;
    Checked mChecked;

    BasicAlbumWrapper(Context context) {
        this.mContext = context;
        mWidget = Widget.getDefaultWidget(context);
    }

    /**
     * Set the action when result.
     */
    public final Returner onResult(Action<Result> result) {
        this.mResult = result;
        return (Returner) this;
    }

    /**
     * Set the action when canceling.
     */
    public final Returner onCancel(Action<Cancel> cancel) {
        this.mCancel = cancel;
        return (Returner) this;
    }

    /**
     * Set the widget property.
     */
    public final Returner widget(@Nullable Widget widget) {
        this.mWidget = widget;
        return (Returner) this;
    }

    /**
     * Start up.
     */
    public abstract void start();
}

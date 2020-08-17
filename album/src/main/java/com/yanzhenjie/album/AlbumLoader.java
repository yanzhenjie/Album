/*
 * Copyright 2017 Yan Zhenjie.
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

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * <p>Used to load the preview, it should be customized.</p>
 * Created by Yan Zhenjie on 2017/3/31.
 */
public interface AlbumLoader {

    AlbumLoader DEFAULT = new AlbumLoader() {
        @Override
        public void load(@NonNull ImageView imageView, @NonNull AlbumFile albumFile) {
        }

        @Override
        public void load(@NonNull ImageView imageView, @NonNull Uri url) {
        }

        @NonNull
        @Override
        public View getPreviewView(@NonNull Context context,
                                   @NonNull AlbumFile albumFile,
                                   View.OnClickListener onClickListener,
                                   View.OnClickListener longClickListener) {
            return new ImageView(context);
        }

        @NonNull
        @Override
        public View getPreviewView(@NonNull Context context,
                                   @NonNull Uri uri,
                                   View.OnClickListener onClickListener,
                                   View.OnClickListener longClickListener) {
            return new ImageView(context);
        }

    };

    /**
     * Load a preview of the album file.
     *
     * @param imageView {@link ImageView}.
     * @param albumFile the media object may be a picture or video.
     */
    void load(@NonNull ImageView imageView, @NonNull AlbumFile albumFile);

    /**
     * Load thumbnails of pictures or videos, either local file or remote file.
     *
     * @param imageView {@link ImageView}.
     * @param uri       The uri of the file, local path or remote path.
     */
    void load(@NonNull ImageView imageView, @NonNull Uri uri);

    /**
     * 获取大图预览的 View，开发者可以自由使用自己加载大图的组件
     *
     * @return 加载大图的组件
     */
    @NonNull
    View getPreviewView(@NonNull Context context,
                        @NonNull AlbumFile albumFile,
                        @Nullable View.OnClickListener onClickListener,
                        @Nullable View.OnClickListener longClickListener);

    /**
     * 获取大图预览的 View，开发者可以自由使用自己加载大图的组件
     *
     * @return 加载大图的组件
     */
    @NonNull
    View getPreviewView(@NonNull Context context,
                        @NonNull Uri uri,
                        @Nullable View.OnClickListener onClickListener,
                        @Nullable View.OnClickListener longClickListener);

}
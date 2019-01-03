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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

/**
 * <p>Used to load the preview, it should be customized.</p>
 * Created by Yan Zhenjie on 2017/3/31.
 */
public interface AlbumLoader {

    AlbumLoader DEFAULT = new AlbumLoader() {
        @Override
        public void load(ImageView imageView, AlbumFile albumFile) {
        }

        @Override
        public void load(ImageView imageView, String url) {
        }
        
        @NonNull
        @Override
        public View getPreviewView(Context context, AlbumFile albumFile, View.OnClickListener onClickListener, View.OnClickListener longClickListener) {
            return new ImageView(context);
        }
        
        @NonNull
        @Override
        public View getPreviewView(Context context, String url, View.OnClickListener onClickListener, View.OnClickListener longClickListener) {
            return new ImageView(context);
        }
        
    };

    /**
     * Load a preview of the album file.
     *
     * @param imageView {@link ImageView}.
     * @param albumFile the media object may be a picture or video.
     */
    void load(ImageView imageView, AlbumFile albumFile);

    /**
     * Load thumbnails of pictures or videos, either local file or remote file.
     *
     * @param imageView {@link ImageView}.
     * @param url       The url of the file, local path or remote path.
     */
    void load(ImageView imageView, String url);
    
    @NonNull
    View getPreviewView(Context context, AlbumFile albumFile, @Nullable View.OnClickListener onClickListener, @Nullable View.OnClickListener longClickListener);
    
    @NonNull
    View getPreviewView(Context context, String url, @Nullable View.OnClickListener onClickListener, @Nullable View.OnClickListener longClickListener);
    
}
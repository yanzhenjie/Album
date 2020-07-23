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
package com.yanzhenjie.album.sample;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;
import com.yanzhenjie.album.sample.photoview.AttacherImageView;
import com.yanzhenjie.album.sample.photoview.PhotoViewAttacher;

/**
 * Created by Yan Zhenjie on 2017/3/31.
 */
public class MediaLoader implements AlbumLoader {
    
    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getUri());
    }
    
    @Override
    public void load(ImageView imageView, Uri uri) {
        Glide.with(imageView.getContext())
            .load(uri)
            .error(R.drawable.placeholder)
            .placeholder(R.drawable.placeholder)
            .crossFade()
            .into(imageView);
    }
    
    @NonNull
    @Override
    public View getPreviewView(Context context, AlbumFile albumFile, final View.OnClickListener onClickListener, final View.OnClickListener longClickListener) {
        return getPreviewView(context, albumFile.getUri(), onClickListener, longClickListener);
    }
    
    @NonNull
    @Override
    public View getPreviewView(Context context, Uri uri, @Nullable final View.OnClickListener onClickListener, @Nullable final View.OnClickListener longClickListener) {
        final AttacherImageView view = new AttacherImageView(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        load(view, uri);
        PhotoViewAttacher attacher = new PhotoViewAttacher(view);
        if (onClickListener != null) {
            attacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View v, float x, float y) {
                    onClickListener.onClick(v);
                }
            });
        }
        if (longClickListener != null) {
            attacher.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onClick(v);
                    return true;
                }
            });
        }
        view.setAttacher(attacher);
        return view;
    }
    
}
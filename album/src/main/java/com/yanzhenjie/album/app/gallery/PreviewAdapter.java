/*
 * Copyright 2016 Yan Zhenjie.
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
package com.yanzhenjie.album.app.gallery;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.widget.photoview.AttacherImageView;
import com.yanzhenjie.album.widget.photoview.PhotoViewAttacher;

import java.util.List;

/**
 * <p>Adapter of preview the big picture.</p>
 * Created by Yan Zhenjie on 2016/10/19.
 */
public abstract class PreviewAdapter<T> extends PagerAdapter
        implements View.OnClickListener, View.OnLongClickListener {

    private Context mContext;
    private List<T> mPreviewList;

    private OnItemClickListener mItemClickListener;
    private OnItemClickListener mItemLongClickListener;

    public PreviewAdapter(Context context, List<T> previewList) {
        this.mContext = context;
        this.mPreviewList = previewList;
    }

    /**
     * Set item click listener.
     *
     * @param onClickListener listener.
     */
    public void setItemClickListener(OnItemClickListener onClickListener) {
        this.mItemClickListener = onClickListener;
    }

    /**
     * Set item long click listener.
     *
     * @param longClickListener listener.
     */
    public void setItemLongClickListener(OnItemClickListener longClickListener) {
        this.mItemLongClickListener = longClickListener;
    }

    @Override
    public int getCount() {
        return mPreviewList == null ? 0 : mPreviewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        AttacherImageView imageView = new AttacherImageView(mContext);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        imageView.setAttacher(attacher);
        T t = mPreviewList.get(position);
        loadPreview(imageView, t, position);

        imageView.setId(position);
        if (mItemClickListener != null) {
            imageView.setOnClickListener(this);
        }
        if (mItemLongClickListener != null) {
            imageView.setOnLongClickListener(this);
        }

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(((View) object));
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.onItemClick(v, v.getId());
    }

    @Override
    public boolean onLongClick(View v) {
        mItemLongClickListener.onItemClick(v, v.getId());
        return true;
    }

    protected abstract void loadPreview(ImageView imageView, T item, int position);
}
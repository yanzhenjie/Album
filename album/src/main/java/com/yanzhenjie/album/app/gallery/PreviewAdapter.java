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

import java.util.List;

/**
 * <p>Adapter of preview the big picture.</p>
 * Created by Yan Zhenjie on 2016/10/19.
 */
public abstract class PreviewAdapter<T> extends PagerAdapter implements View.OnLongClickListener {

    private Context mContext;
    private List<T> mPreviewList;

    private OnItemClickListener mLongClickListener;

    public PreviewAdapter(Context context, List<T> previewList) {
        this.mContext = context;
        this.mPreviewList = previewList;
    }

    /**
     * Set Item long press listen.
     *
     * @param longClickListener listener.
     */
    public void setLongClickListener(OnItemClickListener longClickListener) {
        this.mLongClickListener = longClickListener;
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
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        T t = mPreviewList.get(position);
        loadPreview(imageView, t, position);

        if (mLongClickListener != null) {
            imageView.setId(position);
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
    public boolean onLongClick(View v) {
        mLongClickListener.onItemClick(v, v.getId());
        return true;
    }

    protected abstract void loadPreview(ImageView imageView, T item, int position);
}
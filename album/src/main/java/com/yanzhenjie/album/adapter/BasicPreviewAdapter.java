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
package com.yanzhenjie.album.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanzhenjie.album.task.ImageLocalLoader;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.photoview.PhotoViewAttacher;

import java.util.List;

/**
 * <p>Adapter of preview the big picture.</p>
 * Created by Yan Zhenjie on 2016/10/19.
 */
public abstract class BasicPreviewAdapter<T> extends PagerAdapter {

    private List<T> mPreviewList;

    public BasicPreviewAdapter(List<T> previewList) {
        this.mPreviewList = previewList;
    }

    @Override
    public int getCount() {
        return mPreviewList == null ? 0 : mPreviewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    protected abstract String getImagePath(T t);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        container.addView(imageView);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

        ImageLocalLoader.getInstance().loadImage(
                imageView,
                getImagePath(mPreviewList.get(position)),
                DisplayUtils.screenWidth,
                DisplayUtils.screenHeight,
                new ImageLocalLoader.LoadListener() {
                    @Override
                    public void onLoadFinish(Bitmap bitmap, ImageView imageView, String imagePath) {
                        imageView.setImageBitmap(bitmap);
                        attacher.update();

                        int height = bitmap.getHeight();
                        int width = bitmap.getWidth();
                        int bitmapSize = height / width;
                        int contentSize = DisplayUtils.screenHeight / DisplayUtils.screenWidth;

                        if (height > width && bitmapSize >= contentSize) {
                            attacher.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        } else {
                            attacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        }
                    }
                });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }
}

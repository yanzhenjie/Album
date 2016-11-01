/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.task.ImageLocalLoader;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.PhotoViewAttacher;

import java.util.List;

/**
 * Created by Yan Zhenjie on 2016/10/19.
 */
public class PreviewAdapter extends PagerAdapter {

    private List<AlbumImage> mAlbumImages;
    private int contentHeight;

    public PreviewAdapter(List<AlbumImage> mAlbumImages, int contentHeight) {
        this.mAlbumImages = mAlbumImages;
        this.contentHeight = contentHeight;
    }

    @Override
    public int getCount() {
        return mAlbumImages == null ? 0 : mAlbumImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        container.addView(imageView);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);

        ImageLocalLoader.getInstance().loadImage(imageView, mAlbumImages.get(position).getPath(), DisplayUtils.screenWidth, contentHeight, new ImageLocalLoader.LoadListener() {
            @Override
            public void onLoadFinish(Bitmap bitmap, ImageView imageView, String imagePath) {
                imageView.setImageBitmap(bitmap);
                attacher.update();

                int height = bitmap.getHeight();
                int width = bitmap.getWidth();
                int bitmapSize = height / width;
                int contentSize = contentHeight / DisplayUtils.screenWidth;

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

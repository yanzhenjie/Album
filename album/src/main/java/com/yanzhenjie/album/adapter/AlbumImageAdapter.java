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

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.impl.OnCompatItemClickListener;
import com.yanzhenjie.album.impl.OnCompoundItemCheckListener;
import com.yanzhenjie.album.task.ImageLocalLoader;
import com.yanzhenjie.album.util.SelectorUtils;

import java.util.List;

/**
 * <p>Picture list display adapter.</p>
 * Created by Yan Zhenjie on 2016/10/18.
 */
public class AlbumImageAdapter extends RecyclerView.Adapter<AlbumImageAdapter.ItemViewHolder> {

    private static final int TYPE_BUTTON = 1;
    private static final int TYPE_IMAGE = 2;

    private boolean hasCamera;

    private LayoutInflater mInflater;

    private ColorStateList mColorStateList;

    private List<AlbumImage> mAlbumImages;

    private OnCompatItemClickListener mAddPhotoClickListener;

    private OnCompatItemClickListener mItemClickListener;

    private OnCompoundItemCheckListener mOnCompatCheckListener;

    private int itemSize;

    public AlbumImageAdapter(Context context, boolean hasCamera, int itemSize, @ColorInt int normalColor, @ColorInt int checkColor) {
        this.mInflater = LayoutInflater.from(context);
        this.hasCamera = hasCamera;
        this.itemSize = itemSize;
        this.mColorStateList = SelectorUtils.createColorStateList(normalColor, checkColor);
    }

    public void notifyDataSetChanged(List<AlbumImage> albumImages) {
        this.mAlbumImages = albumImages;
        super.notifyDataSetChanged();
    }

    public void setAddPhotoClickListener(OnCompatItemClickListener addPhotoClickListener) {
        this.mAddPhotoClickListener = addPhotoClickListener;
    }

    public void setItemClickListener(OnCompatItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        int camera = hasCamera ? 1 : 0;
        return mAlbumImages == null ? camera : mAlbumImages.size() + camera;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return hasCamera ? TYPE_BUTTON : TYPE_IMAGE;
            default:
                return TYPE_IMAGE;
        }
    }

    public void setOnCheckListener(OnCompoundItemCheckListener checkListener) {
        this.mOnCompatCheckListener = checkListener;
    }

    @Override
    public AlbumImageAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder viewHolder;
        switch (viewType) {
            case TYPE_BUTTON:
                viewHolder = new ItemViewHolder(
                        mInflater.inflate(R.layout.album_item_content_button, parent, false), hasCamera, itemSize);
                break;
            default:
                viewHolder = new ImageHolder(
                        mInflater.inflate(R.layout.album_item_content_image, parent, false), hasCamera, itemSize);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlbumImageAdapter.ItemViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BUTTON: {
                holder.mItemClickListener = mAddPhotoClickListener;
                break;
            }
            default: {
                int camera = hasCamera ? 1 : 0;
                int imagePosition = holder.getAdapterPosition() - camera;
                AlbumImage albumImage = mAlbumImages.get(imagePosition);

                ImageHolder imageHolder = (ImageHolder) holder;
                imageHolder.mItemClickListener = mItemClickListener;
                imageHolder.mOnCompatCheckListener = mOnCompatCheckListener;
                imageHolder.setButtonTint(mColorStateList);
                imageHolder.setData(albumImage);
                break;
            }
        }
    }

    private static class ImageHolder extends ItemViewHolder implements View.OnClickListener {

        private ImageView mIvImage;
        private AppCompatCheckBox mCbChecked;

        private OnCompoundItemCheckListener mOnCompatCheckListener;

        public ImageHolder(View itemView, boolean hasCamera, int itemSize) {
            super(itemView, hasCamera, itemSize);
            mIvImage = (ImageView) itemView.findViewById(R.id.iv_album_content_image);
            mCbChecked = (AppCompatCheckBox) itemView.findViewById(R.id.cb_album_check);
            mCbChecked.setOnClickListener(this);
        }

        public void setButtonTint(ColorStateList colorStateList) {
            //noinspection RestrictedApi
            mCbChecked.setSupportButtonTintList(colorStateList);
        }

        public void setData(AlbumImage albumImage) {
            mCbChecked.setChecked(albumImage.isChecked());
            ImageLocalLoader.getInstance().loadImage(mIvImage, albumImage.getPath(), itemSize, itemSize);
        }


        @Override
        public void onClick(View v) {
            if (mOnCompatCheckListener != null && v == mCbChecked) {
                boolean isChecked = mCbChecked.isChecked();
                int camera = hasCamera ? 1 : 0;
                mOnCompatCheckListener.onCheckedChanged(mCbChecked, getAdapterPosition() - camera, isChecked);
            }
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        final boolean hasCamera;
        final int itemSize;
        OnCompatItemClickListener mItemClickListener;

        public ItemViewHolder(View itemView, boolean hasCamera, int itemSize) {
            super(itemView);
            this.hasCamera = hasCamera;
            this.itemSize = itemSize;
            itemView.getLayoutParams().height = itemSize;
            itemView.setOnClickListener(mClickListener);
        }

        private final View.OnClickListener mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null && v == itemView) {
                    int camera = hasCamera ? 1 : 0;
                    mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                }
            }
        };
    }
}

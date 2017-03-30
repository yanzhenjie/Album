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

import android.content.res.ColorStateList;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.impl.OnCompatItemClickListener;
import com.yanzhenjie.album.task.ImageLocalLoader;
import com.yanzhenjie.album.util.DisplayUtils;

import java.util.List;

/**
 * <p>BottomSheet dialog adapter, show all folder.</p>
 * Created by Yan Zhenjie on 2016/10/18.
 */
public class AlbumFolderAdapter extends RecyclerView.Adapter<AlbumFolderAdapter.FolderViewHolder> {

    private ColorStateList mButtonTint;

    private List<AlbumFolder> mAlbumFolders;

    private OnCompatItemClickListener mItemClickListener;

    private int checkPosition = 0;

    private static int size = DisplayUtils.dip2px(100);

    public AlbumFolderAdapter(ColorStateList buttonTint, List<AlbumFolder> mAlbumFolders, OnCompatItemClickListener mItemClickListener) {
        this.mButtonTint = buttonTint;
        this.mAlbumFolders = mAlbumFolders;
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FolderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item_dialog_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        final int newPosition = holder.getAdapterPosition();
        holder.setButtonTint(mButtonTint);
        holder.setData(mAlbumFolders.get(newPosition));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumFolder albumFolder = mAlbumFolders.get(newPosition);

                if (mItemClickListener != null)
                    mItemClickListener.onItemClick(v, newPosition);

                if (!albumFolder.isChecked()) {
                    albumFolder.setChecked(true);
                    mAlbumFolders.get(checkPosition).setChecked(false);
                    notifyItemChanged(checkPosition);
                    notifyItemChanged(newPosition);
                    checkPosition = newPosition;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbumFolders == null ? 0 : mAlbumFolders.size();
    }

    static class FolderViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIvImage;
        private TextView mTvTitle;
        private AppCompatRadioButton mRbCheck;

        public FolderViewHolder(View itemView) {
            super(itemView);

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_gallery_preview_image);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_gallery_preview_title);
            mRbCheck = (AppCompatRadioButton) itemView.findViewById(R.id.rb_gallery_preview_check);
        }

        public void setButtonTint(ColorStateList colorStateList) {
            //noinspection RestrictedApi
            mRbCheck.setSupportButtonTintList(colorStateList);
        }

        public void setData(AlbumFolder albumFolder) {
            List<AlbumImage> albumImages = albumFolder.getImages();
            mTvTitle.setText("(" + albumImages.size() + ") " + albumFolder.getName());
            mRbCheck.setChecked(albumFolder.isChecked());

            if (albumImages.size() > 0) {
                ImageLocalLoader.getInstance().loadImage(mIvImage, albumImages.get(0).getPath(), size, size);
            } else {
                mIvImage.setImageDrawable(ImageLocalLoader.DEFAULT_DRAWABLE);
            }
        }

    }

}

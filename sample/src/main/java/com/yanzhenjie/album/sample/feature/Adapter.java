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
package com.yanzhenjie.album.sample.feature;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.sample.R;
import com.yanzhenjie.album.util.AlbumUtils;

import java.util.List;

/**
 * <p>Image adapter.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mInflater;
    private int itemSize;
    private OnItemClickListener mItemClickListener;

    private List<AlbumFile> mAlbumFiles;

    public Adapter(Context context, int itemSize, OnItemClickListener itemClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.itemSize = itemSize;
        this.mItemClickListener = itemClickListener;
    }

    public void notifyDataSetChanged(List<AlbumFile> imagePathList) {
        this.mAlbumFiles = imagePathList;
        super.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        AlbumFile albumFile = mAlbumFiles.get(position);
        if (albumFile.getMediaType() == AlbumFile.TYPE_IMAGE) {
            return AlbumFile.TYPE_IMAGE;
        } else {
            return AlbumFile.TYPE_VIDEO;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case AlbumFile.TYPE_IMAGE: {
                return new ImageViewHolder(mInflater.inflate(R.layout.item_content_image, parent, false), itemSize, mItemClickListener);
            }
            case AlbumFile.TYPE_VIDEO:
            default: {
                return new VideoViewHolder(mInflater.inflate(R.layout.item_content_video, parent, false), itemSize, mItemClickListener);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case AlbumFile.TYPE_IMAGE: {
                ((ImageViewHolder) holder).setData(mAlbumFiles.get(position));
                break;
            }
            case AlbumFile.TYPE_VIDEO: {
                ((VideoViewHolder) holder).setData(mAlbumFiles.get(position));
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAlbumFiles == null ? 0 : mAlbumFiles.size();
    }

    private static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final int itemSize;
        private final OnItemClickListener mItemClickListener;

        private ImageView mIvImage;

        ImageViewHolder(View itemView, int itemSize, OnItemClickListener itemClickListener) {
            super(itemView);
            itemView.getLayoutParams().height = itemSize;

            this.itemSize = itemSize;
            this.mItemClickListener = itemClickListener;

            mIvImage = (ImageView) itemView.findViewById(R.id.iv_album_content_image);

            itemView.setOnClickListener(this);
        }

        public void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    loadAlbumFile(mIvImage, albumFile, itemSize, itemSize);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    private static class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final int itemSize;
        private final OnItemClickListener mItemClickListener;

        private ImageView mIvImage;
        private TextView mTvDuration;

        VideoViewHolder(View itemView, int itemSize, OnItemClickListener itemClickListener) {
            super(itemView);
            itemView.getLayoutParams().height = itemSize;

            this.itemSize = itemSize;
            this.mItemClickListener = itemClickListener;

            mIvImage = (ImageView) itemView.findViewById(com.yanzhenjie.album.R.id.iv_album_content_image);
            mTvDuration = (TextView) itemView.findViewById(com.yanzhenjie.album.R.id.tv_duration);

            itemView.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    loadAlbumFile(mIvImage, albumFile, itemSize, itemSize);
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

}

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
package com.yanzhenjie.album.app.album;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.impl.OnCheckedClickListener;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayHelper;

import java.util.List;

/**
 * <p>Picture list display adapter.</p>
 * Created by Yan Zhenjie on 2016/10/18.
 */
public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_BUTTON = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_VIDEO = 3;

    private final LayoutInflater mInflater;
    private final boolean hasCamera;
    private final int mChoiceMode;
    private final ColorStateList mSelector;

    private List<AlbumFile> mAlbumFiles;

    private OnItemClickListener mAddPhotoClickListener;
    private OnItemClickListener mItemClickListener;
    private OnCheckedClickListener mCheckedClickListener;

    private int mItemSize;

    public AlbumAdapter(Context context, boolean hasCamera, int choiceMode, ColorStateList selector) {
        this.mInflater = LayoutInflater.from(context);
        this.hasCamera = hasCamera;
        this.mChoiceMode = choiceMode;
        this.mSelector = selector;
    }

    public void setAlbumFiles(List<AlbumFile> albumFiles) {
        this.mAlbumFiles = albumFiles;
    }

    public void setAddClickListener(OnItemClickListener addPhotoClickListener) {
        this.mAddPhotoClickListener = addPhotoClickListener;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public void setCheckedClickListener(OnCheckedClickListener checkedClickListener) {
        this.mCheckedClickListener = checkedClickListener;
    }

    public void setItemSize(int dividerSize, int column) {
        this.mItemSize = DisplayHelper.getInstance().getItemSize(mInflater.getContext(), dividerSize, column);
    }

    @Override
    public int getItemCount() {
        int camera = hasCamera ? 1 : 0;
        return mAlbumFiles == null ? camera : mAlbumFiles.size() + camera;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0: {
                return hasCamera ? TYPE_BUTTON : TYPE_IMAGE;
            }
            default: {
                position = hasCamera ? position - 1 : position;
                AlbumFile albumFile = mAlbumFiles.get(position);
                return albumFile.getMediaType() == AlbumFile.TYPE_VIDEO ? TYPE_VIDEO : TYPE_IMAGE;
            }
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case TYPE_BUTTON: {
                viewHolder = new ButtonViewHolder(mInflater.inflate(R.layout.album_item_content_button, parent, false),
                        mAddPhotoClickListener);
                break;
            }
            case TYPE_IMAGE: {
                ImageHolder imageViewHolder = new ImageHolder(mInflater.inflate(R.layout.album_item_content_image, parent, false),
                        hasCamera, mChoiceMode,
                        mItemClickListener,
                        mCheckedClickListener);
                if (mChoiceMode == Album.MODE_MULTIPLE) {
                    imageViewHolder.mCheckBox.setVisibility(View.VISIBLE);
                    imageViewHolder.mCheckBox.setSupportButtonTintList(mSelector);
                    imageViewHolder.mCheckBox.setTextColor(mSelector);
                } else {
                    imageViewHolder.mCheckBox.setVisibility(View.GONE);
                }
                viewHolder = imageViewHolder;
                break;
            }
            case TYPE_VIDEO: {
                VideoHolder videoViewHolder = new VideoHolder(mInflater.inflate(R.layout.album_item_content_video, parent, false),
                        hasCamera, mChoiceMode,
                        mItemClickListener,
                        mCheckedClickListener);
                if (mChoiceMode == Album.MODE_MULTIPLE) {
                    videoViewHolder.mCheckBox.setVisibility(View.VISIBLE);
                    videoViewHolder.mCheckBox.setSupportButtonTintList(mSelector);
                    videoViewHolder.mCheckBox.setTextColor(mSelector);
                } else {
                    videoViewHolder.mCheckBox.setVisibility(View.GONE);
                }
                viewHolder = videoViewHolder;
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
        Configuration config = parent.getResources().getConfiguration();
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        switch (config.orientation) {
            case Configuration.ORIENTATION_PORTRAIT: {
                layoutParams.width = -1;
                layoutParams.height = mItemSize;
                break;
            }
            case Configuration.ORIENTATION_LANDSCAPE: {
                layoutParams.width = mItemSize;
                layoutParams.height = -1;
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_BUTTON: {
                // Nothing.
                break;
            }
            case TYPE_IMAGE: {
                ImageHolder imageHolder = (ImageHolder) holder;
                int camera = hasCamera ? 1 : 0;
                position = holder.getAdapterPosition() - camera;
                AlbumFile albumFile = mAlbumFiles.get(position);
                imageHolder.setData(albumFile);
                break;
            }
            case TYPE_VIDEO: {
                VideoHolder videoHolder = (VideoHolder) holder;
                int camera = hasCamera ? 1 : 0;
                position = holder.getAdapterPosition() - camera;
                AlbumFile albumFile = mAlbumFiles.get(position);
                videoHolder.setData(albumFile);
                break;
            }
        }
    }

    private static class ButtonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemClickListener mItemClickListener;

        ButtonViewHolder(View itemView, OnItemClickListener itemClickListener) {
            super(itemView);
            this.mItemClickListener = itemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null && v == itemView) {
                mItemClickListener.onItemClick(v, 0);
            }
        }
    }

    private static class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final boolean hasCamera;
        private final int mChoiceMode;

        private final OnItemClickListener mItemClickListener;
        private final OnCheckedClickListener mCheckedClickListener;

        private ImageView mIvImage;
        private AppCompatCheckBox mCheckBox;

        private FrameLayout mLayoutLayer;

        ImageHolder(View itemView, boolean hasCamera, int choiceMode,
                    OnItemClickListener itemClickListener, OnCheckedClickListener checkedClickListener) {
            super(itemView);
            this.hasCamera = hasCamera;
            this.mChoiceMode = choiceMode;
            this.mItemClickListener = itemClickListener;
            this.mCheckedClickListener = checkedClickListener;

            mIvImage = itemView.findViewById(R.id.iv_album_content_image);
            mCheckBox = itemView.findViewById(R.id.check_box);
            mLayoutLayer = itemView.findViewById(R.id.layout_layer);

            itemView.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
            mLayoutLayer.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            mCheckBox.setChecked(albumFile.isChecked());
            Album.getAlbumConfig()
                    .getAlbumLoader()
                    .load(mIvImage, albumFile);

            mLayoutLayer.setVisibility(albumFile.isDisable() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                switch (mChoiceMode) {
                    case Album.MODE_SINGLE: {
                        int camera = hasCamera ? 1 : 0;
                        mCheckedClickListener.onCheckedClick(mCheckBox, getAdapterPosition() - camera);
                        break;
                    }
                    case Album.MODE_MULTIPLE: {
                        int camera = hasCamera ? 1 : 0;
                        mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                        break;
                    }
                }
            } else if (v == mCheckBox) {
                int camera = hasCamera ? 1 : 0;
                mCheckedClickListener.onCheckedClick(mCheckBox, getAdapterPosition() - camera);
            } else if (v == mLayoutLayer) {
                int camera = hasCamera ? 1 : 0;
                mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
            }
        }
    }

    private static class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final boolean hasCamera;
        private final int mChoiceMode;

        private final OnItemClickListener mItemClickListener;
        private final OnCheckedClickListener mCheckedClickListener;

        private ImageView mIvImage;
        private AppCompatCheckBox mCheckBox;
        private TextView mTvDuration;

        private FrameLayout mLayoutLayer;

        VideoHolder(View itemView, boolean hasCamera, int choiceMode,
                    OnItemClickListener itemClickListener, OnCheckedClickListener checkedClickListener) {
            super(itemView);
            this.hasCamera = hasCamera;
            this.mChoiceMode = choiceMode;
            this.mItemClickListener = itemClickListener;
            this.mCheckedClickListener = checkedClickListener;

            mIvImage = itemView.findViewById(R.id.iv_album_content_image);
            mCheckBox = itemView.findViewById(R.id.check_box);
            mTvDuration = itemView.findViewById(R.id.tv_duration);
            mLayoutLayer = itemView.findViewById(R.id.layout_layer);

            itemView.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);
            mLayoutLayer.setOnClickListener(this);
        }

        void setData(AlbumFile albumFile) {
            Album.getAlbumConfig().getAlbumLoader().load(mIvImage, albumFile);
            mCheckBox.setChecked(albumFile.isChecked());
            mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));

            mLayoutLayer.setVisibility(albumFile.isDisable() ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                switch (mChoiceMode) {
                    case Album.MODE_SINGLE: {
                        int camera = hasCamera ? 1 : 0;
                        mCheckedClickListener.onCheckedClick(mCheckBox, getAdapterPosition() - camera);
                        break;
                    }
                    case Album.MODE_MULTIPLE: {
                        int camera = hasCamera ? 1 : 0;
                        mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                        break;
                    }
                }
            } else if (v == mCheckBox) {
                int camera = hasCamera ? 1 : 0;
                mCheckedClickListener.onCheckedClick(mCheckBox, getAdapterPosition() - camera);
            } else if (v == mLayoutLayer) {
                if (mItemClickListener != null) {
                    int camera = hasCamera ? 1 : 0;
                    mItemClickListener.onItemClick(v, getAdapterPosition() - camera);
                }
            }
        }
    }
}
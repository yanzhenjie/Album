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
package com.yanzhenjie.album.api;

import android.content.Context;
import android.content.Intent;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.app.gallery.GalleryActivity;
import com.yanzhenjie.album.app.gallery.GalleryAlbumActivity;

/**
 * <p>Gallery wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class GalleryAlbumWrapper extends BasicGalleryWrapper<GalleryAlbumWrapper, AlbumFile, String, AlbumFile> {

    public GalleryAlbumWrapper(Context context) {
        super(context);
    }

    @Override
    public void start() {
        GalleryAlbumActivity.sResult = mResult;
        GalleryAlbumActivity.sCancel = mCancel;
        GalleryAlbumActivity.sClick = mItemClick;
        GalleryAlbumActivity.sLongClick = mItemLongClick;
        Intent intent = new Intent(mContext, GalleryAlbumActivity.class);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
        intent.putParcelableArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked);
        intent.putExtra(Album.KEY_INPUT_CURRENT_POSITION, mCurrentPosition);
        intent.putExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, mCheckable);
        mContext.startActivity(intent);
    }
}
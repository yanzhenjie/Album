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
package com.yanzhenjie.album.api;

import android.content.Context;
import android.content.Intent;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.ui.GalleryActivity;

/**
 * <p>Gallery wrapper.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class GalleryWrapper extends BasicGalleryWrapper<GalleryWrapper, String, String> {

    public GalleryWrapper(Context context) {
        super(context);
    }

    @Override
    public void start() {
        GalleryActivity.sAlbumListener = mAlbumListener;
        Intent intent = new Intent(mContext, GalleryActivity.class);
        intent.putExtra(Album.KEY_INPUT_REQUEST_CODE, mRequestCode);
        intent.putExtra(Album.KEY_INPUT_WIDGET, mWidget);
        intent.putStringArrayListExtra(Album.KEY_INPUT_CHECKED_LIST, mChecked);
        intent.putExtra(Album.KEY_INPUT_CURRENT_POSITION, mCurrentPosition);
        intent.putExtra(Album.KEY_INPUT_GALLERY_CHECKABLE, mCheckable);
        intent.putExtra(Album.KEY_INPUT_NAVIGATION_ALPHA, mNavigationAlpha);
        mContext.startActivity(intent);
    }
}

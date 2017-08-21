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
package com.yanzhenjie.album.ui.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.util.DisplayUtils;

import java.util.List;

/**
 * <p>Adapter of preview the big picture.</p>
 * Created by yanzhenjie on 17-3-29.
 */
public class AlbumFilePreviewAdapter extends BasicPreviewAdapter<AlbumFile> {

    public AlbumFilePreviewAdapter(Context context, List<AlbumFile> previewList) {
        super(context, previewList);
    }

    @Override
    protected boolean loadPreview(ImageView imageView, AlbumFile albumFile, int position) {
        Album.getAlbumConfig()
                .getAlbumLoader()
                .loadAlbumFile(imageView, albumFile, DisplayUtils.sScreenWidth, DisplayUtils.sScreenHeight);
        return true;
    }
}

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
package com.yanzhenjie.album.sample.load;

import android.webkit.URLUtil;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumLoader;

import java.io.File;

/**
 * Created by Yan Zhenjie on 2017/3/31.
 */
public class PicassoImageLoader implements AlbumLoader {

    @Override
    public void loadAlbumFile(ImageView imageView, AlbumFile albumFile, int viewWidth, int viewHeight) {
        @AlbumFile.MediaType int mediaType = albumFile.getMediaType();
        if (mediaType == AlbumFile.TYPE_IMAGE) {
            Picasso.with(imageView.getContext())
                    .load(new File(albumFile.getPath()))
                    .centerCrop()
                    .resize(viewWidth, viewHeight)
                    .into(imageView);
        } else {
            Album.getAlbumConfig().
                    getAlbumLoader().
                    loadAlbumFile(imageView, albumFile, viewWidth, viewHeight);
        }
    }

    @Override
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        if (URLUtil.isNetworkUrl(imagePath)) {
            Picasso.with(imageView.getContext())
                    .load(imagePath)
                    .centerCrop()
                    .resize(width, height)
                    .into(imageView);
        } else {
            Picasso.with(imageView.getContext())
                    .load(new File(imagePath))
                    .centerCrop()
                    .resize(width, height)
                    .into(imageView);
        }
    }

}

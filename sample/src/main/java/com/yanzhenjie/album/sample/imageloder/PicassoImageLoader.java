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
package com.yanzhenjie.album.sample.imageloder;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.yanzhenjie.album.impl.AlbumImageLoader;

import java.io.File;

/**
 * Created by Yan Zhenjie on 2017/3/31.
 */
public class PicassoImageLoader implements AlbumImageLoader {

    @Override
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        Picasso.with(imageView.getContext())
                .load(new File(imagePath))
                .centerCrop()
                .resize(width, height)
                .into(imageView);
    }

}

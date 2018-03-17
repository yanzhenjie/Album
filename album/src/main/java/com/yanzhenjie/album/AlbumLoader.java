/*
 * Copyright © 2017 Yan Zhenjie.
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
package com.yanzhenjie.album;

import android.widget.ImageView;

/**
 * <p>
 *      Customizing the Album Loader, you can do some optimization work.
 * </p>
 * Created by Yan Zhenjie on 2017/3/31.
 */
public interface AlbumLoader {

    /**
     * Load a preview of the album file.
     *
     * @param imageView  {@link ImageView}.
     * @param albumFile  the media object may be a picture or video.
     * @param viewWidth  the width fo target view.
     * @param viewHeight the width fo target view.
     */
    void loadAlbumFile(ImageView imageView, AlbumFile albumFile, int viewWidth, int viewHeight);

    /**
     * Load a preview of the picture.
     *
     * @param imageView  {@link ImageView}.
     * @param imagePath  file path, which may be a local path or a network path.
     * @param viewWidth  the width fo target view.
     * @param viewHeight the width fo target view.
     */
    void loadImage(ImageView imageView, String imagePath, int viewWidth, int viewHeight);

}

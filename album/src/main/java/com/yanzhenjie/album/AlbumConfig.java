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
package com.yanzhenjie.album;

import com.yanzhenjie.album.impl.AlbumImageLoader;

/**
 * <p>Album config.</p>
 * Created by Yan Zhenjie on 2017/3/31.
 */
public class AlbumConfig {

    private AlbumImageLoader mImageLoader;

    private AlbumConfig(Build build) {
        this.mImageLoader = build.mLoader;
    }

    /**
     * Get {@link AlbumImageLoader}.
     *
     * @return {@link AlbumImageLoader}.
     */
    public AlbumImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static final class Build {

        private AlbumImageLoader mLoader;

        public Build() {
        }

        /**
         * Set image loader.
         *
         * @param loader {@link AlbumImageLoader}.
         */
        public Build setImageLoader(AlbumImageLoader loader) {
            this.mLoader = loader;
            return this;
        }

        /**
         * Create AlbumConfig.
         *
         * @return {@link AlbumConfig}.
         */
        public AlbumConfig build() {
            return new AlbumConfig(this);
        }
    }

}

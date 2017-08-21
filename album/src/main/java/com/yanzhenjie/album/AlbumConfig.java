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

import android.content.Context;

import com.yanzhenjie.album.task.DefaultAlbumLoader;

import java.util.Locale;

/**
 * <p>Album config.</p>
 * Created by Yan Zhenjie on 2017/3/31.
 */
public class AlbumConfig {

    /**
     * Create a new builder.
     */
    public static Builder newBuilder(Context context) {
        return new Builder(context);
    }

    private AlbumLoader mLoader;
    private Locale mLocale;

    private AlbumConfig(Builder builder) {
        this.mLoader = builder.mLoader == null ?
                DefaultAlbumLoader.getInstance() : builder.mLoader;
        this.mLocale = builder.mLocale == null ?
                Locale.getDefault() : builder.mLocale;
    }

    /**
     * Get {@link AlbumLoader}.
     *
     * @return {@link AlbumLoader}.
     */
    public AlbumLoader getAlbumLoader() {
        return mLoader;
    }

    /**
     * Get {@link Locale}.
     *
     * @return {@link Locale}.
     */
    public Locale getLocale() {
        return mLocale;
    }

    public static final class Builder {

        private AlbumLoader mLoader;
        private Locale mLocale;

        private Builder(Context context) {
        }

        /**
         * Set album loader.
         *
         * @param loader {@link AlbumLoader}.
         * @return {@link Builder}.
         */
        public Builder setAlbumLoader(AlbumLoader loader) {
            this.mLoader = loader;
            return this;
        }

        /**
         * Set locale for language.
         *
         * @param locale {@link Locale}.
         * @return {@link Builder}.
         */
        public Builder setLocale(Locale locale) {
            this.mLocale = locale;
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

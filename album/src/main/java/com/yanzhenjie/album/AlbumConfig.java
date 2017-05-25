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

import com.yanzhenjie.album.impl.AlbumImageLoader;

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

    private AlbumImageLoader mImageLoader;
    private Locale mLocale;

    private AlbumConfig(Builder build) {
        this.mImageLoader = build.mLoader;
        this.mLocale = build.mLocale;
    }

    /**
     * @deprecated use {@link #AlbumConfig(Builder)} instead.
     */
    @Deprecated
    private AlbumConfig(Build build) {
        this.mImageLoader = build.mLoader;
        this.mLocale = build.mLocale;
    }

    /**
     * Get {@link AlbumImageLoader}.
     *
     * @return {@link AlbumImageLoader}.
     */
    public AlbumImageLoader getImageLoader() {
        return mImageLoader;
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

        private AlbumImageLoader mLoader;
        private Locale mLocale;

        private Builder(Context context) {
        }

        /**
         * Set image loader.
         *
         * @param loader {@link AlbumImageLoader}.
         * @return {@link Builder}.
         */
        public Builder setImageLoader(AlbumImageLoader loader) {
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

    /**
     * @deprecated use {@link Builder} instead.
     */
    @Deprecated
    public static final class Build {

        private AlbumImageLoader mLoader;
        private Locale mLocale;

        /**
         * @deprecated use {@link AlbumConfig#newBuilder(Context)} instead.
         */
        @Deprecated
        public Build() {
        }

        /**
         * Set image loader.
         *
         * @param loader {@link AlbumImageLoader}.
         * @return {@link Build}.
         * @deprecated use {@link Builder#setImageLoader(AlbumImageLoader)} instead.
         */
        @Deprecated
        public Build setImageLoader(AlbumImageLoader loader) {
            this.mLoader = loader;
            return this;
        }

        /**
         * Set locale for language.
         *
         * @param locale {@link Locale}.
         * @return {@link Build}.
         * @deprecated use {@link Builder#setLocale(Locale)} instead.
         */
        @Deprecated
        public Build setLocale(Locale locale) {
            this.mLocale = locale;
            return this;
        }

        /**
         * Create AlbumConfig.
         *
         * @return {@link AlbumConfig}.
         * @deprecated use {@link Builder#build()} instead.
         */
        @Deprecated
        public AlbumConfig build() {
            return new AlbumConfig(this);
        }
    }

}

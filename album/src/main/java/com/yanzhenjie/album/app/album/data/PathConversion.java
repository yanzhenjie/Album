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
package com.yanzhenjie.album.app.album.data;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.album.util.AlbumUtils;

import java.io.File;

/**
 * Created by YanZhenjie on 2017/10/18.
 */
public class PathConversion {

    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;

    public PathConversion(Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter) {
        this.mSizeFilter = sizeFilter;
        this.mMimeFilter = mimeFilter;
        this.mDurationFilter = durationFilter;
    }

    @WorkerThread
    @NonNull
    public AlbumFile convert(String filePath) {
        File file = new File(filePath);

        AlbumFile albumFile = new AlbumFile();
        albumFile.setPath(filePath);

        File parentFile = file.getParentFile();
        albumFile.setBucketName(parentFile.getName());

        String mimeType = AlbumUtils.getMimeType(filePath);
        albumFile.setMimeType(mimeType);
        long nowTime = System.currentTimeMillis();
        albumFile.setAddDate(nowTime);
        albumFile.setSize(file.length());
        int mediaType = 0;
        if (!TextUtils.isEmpty(mimeType)) {
            if (mimeType.contains("video"))
                mediaType = AlbumFile.TYPE_VIDEO;
            if (mimeType.contains("image"))
                mediaType = AlbumFile.TYPE_IMAGE;
        }
        albumFile.setMediaType(mediaType);

        if (mSizeFilter != null && mSizeFilter.filter(file.length())) {
            albumFile.setDisable(true);
        }
        if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
            albumFile.setDisable(true);
        }

        if (mediaType == AlbumFile.TYPE_VIDEO) {
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(filePath);
                player.prepare();
                albumFile.setDuration(player.getDuration());
            } catch (Exception ignored) {
            } finally {
                player.release();
            }

            if (mDurationFilter != null && mDurationFilter.filter(albumFile.getDuration())) {
                albumFile.setDisable(true);
            }
        }
        return albumFile;
    }

}
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
import android.support.annotation.IntRange;

/**
 * Created by YanZhenjie on 2017/11/8.
 */
public abstract class BasicChoiceVideoWrapper<Returner extends BasicChoiceVideoWrapper, Result, Cancel, Checked> extends BasicChoiceWrapper<Returner, Result, Cancel, Checked> {

    BasicChoiceVideoWrapper(Context context) {
        super(context);
    }

    int mQuality = 1;
    long mLimitDuration = Integer.MAX_VALUE;
    long mLimitBytes = Integer.MAX_VALUE;

    /**
     * Set the quality when taking video, should be 0 or 1. Currently value 0 means low quality, and value 1 means high quality.
     *
     * @param quality should be 0 or 1.
     */
    public Returner quality(@IntRange(from = 0, to = 1) int quality) {
        this.mQuality = quality;
        return (Returner) this;
    }

    /**
     * Specify the maximum allowed recording duration in seconds.
     *
     * @param duration the maximum number of seconds.
     */
    public Returner limitDuration(@IntRange(from = 1) long duration) {
        this.mLimitDuration = duration;
        return (Returner) this;
    }

    /**
     * Specify the maximum allowed size.
     *
     * @param bytes the size of the byte.
     */
    public Returner limitBytes(@IntRange(from = 1) long bytes) {
        this.mLimitBytes = bytes;
        return (Returner) this;
    }
}
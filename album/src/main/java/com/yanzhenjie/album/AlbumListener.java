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

import android.support.annotation.NonNull;

/**
 * <p>
 *     All the results callback.
 * </p>
 * Created by YanZhenjie on 2017/8/16.
 */
public interface AlbumListener<T> {

    /**
     * The results callback, only when the success of the callback.
     *
     * @param requestCode requestCode.
     * @param result      it might be a AlbumFile list or a String, depending on what function you use.
     */
    void onAlbumResult(int requestCode, @NonNull T result);

    /**
     * Callback when operation is canceled.
     *
     * @param requestCode requestCode.
     */
    void onAlbumCancel(int requestCode);

}

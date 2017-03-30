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
package com.yanzhenjie.album.fragment;

import com.yanzhenjie.album.entity.AlbumImage;

/**
 * <p>Fragment data source change, callback activity.</p>
 * Created by yanzhenjie on 17-3-28.
 */
public interface Callback {

    /**
     * Generate the result.
     *
     * @param ok Returns true if the error returns false.
     */
    void doResult(boolean ok);

    /**
     * When the checked state of a picture changes.
     *
     * @param image     {@link AlbumImage}.
     * @param isChecked checked.
     */
    void onCheckedChanged(AlbumImage image, boolean isChecked);

    /**
     * Get check image count.
     *
     * @return check image count.
     */
    int getCheckedCount();
}
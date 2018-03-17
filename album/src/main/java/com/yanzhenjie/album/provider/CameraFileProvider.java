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
package com.yanzhenjie.album.provider;

import android.content.Context;
import android.support.v4.content.FileProvider;

/**
 * <p>Defined for the camera.</p>
 * Created by Yan Zhenjie on 2017/3/31.
 */
public class CameraFileProvider extends FileProvider {

    /**
     * Get the provider of the external file path.
     *
     * @param context context.
     * @return provider.
     */
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".album.camera.provider";
    }

}

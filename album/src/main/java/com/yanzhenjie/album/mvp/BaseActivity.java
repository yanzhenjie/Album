/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.album.mvp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.util.AlbumUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by YanZhenjie on 2018/4/6.
 */
public class BaseActivity extends AppCompatActivity implements Bye {
    
    public static final String[] PERMISSION_TAKE_PICTURE = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    
    public static final String[] PERMISSION_TAKE_PICTURE_29 = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_MEDIA_LOCATION"};
    
    
    public static final String[] PERMISSION_TAKE_VIDEO = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    
    public static final String[] PERMISSION_TAKE_VIDEO_29 = {"android.permission.CAMERA", "android.permission.RECORD_AUDIO", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.ACCESS_MEDIA_LOCATION"};
    
    public static final String[] PERMISSION_STORAGE = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Locale locale = Album.getAlbumConfig().getLocale();
        AlbumUtils.applyLanguageForContext(this, locale);
    }
    
    /**
     * Request permission.
     */
    protected void requestPermission(String[] permissions, int code) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> deniedPermissions = getDeniedPermissions(this, permissions);
            if (deniedPermissions.isEmpty()) {
                onPermissionGranted(code);
            } else {
                permissions = new String[deniedPermissions.size()];
                deniedPermissions.toArray(permissions);
                ActivityCompat.requestPermissions(this, permissions, code);
            }
        } else {
            onPermissionGranted(code);
        }
    }
    
    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (isGrantedResult(grantResults)) {
            onPermissionGranted(requestCode);
        } else {
            onPermissionDenied(requestCode);
        }
    }
    
    protected void onPermissionGranted(int code) {
    }
    
    protected void onPermissionDenied(int code) {
    }
    
    @Override
    public void bye() {
        onBackPressed();
    }
    
    private static List<String> getDeniedPermissions(Context context, String... permissions) {
        List<String> deniedList = new ArrayList<>(2);
        for (String permission : permissions) {
            if (PermissionChecker.checkSelfPermission(context, permission) != PermissionChecker.PERMISSION_GRANTED) {
                deniedList.add(permission);
            }
        }
        return deniedList;
    }
    
    private static boolean isGrantedResult(int... grantResults) {
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }
}
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
package com.yanzhenjie.album.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yanzhenjie.album.sample.feature.AlbumActivity;
import com.yanzhenjie.album.sample.feature.AlbumFilterActivity;
import com.yanzhenjie.album.sample.feature.AlbumUIActivity;
import com.yanzhenjie.album.sample.feature.CameraActivity;
import com.yanzhenjie.album.sample.feature.GalleryActivity;
import com.yanzhenjie.album.sample.feature.ImageActivity;
import com.yanzhenjie.album.sample.feature.VideoActivity;
import com.yanzhenjie.album.util.DisplayUtils;

/**
 * <p>Album sample.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayUtils.initScreen(this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_image).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);
        findViewById(R.id.btn_album).setOnClickListener(this);
        findViewById(R.id.btn_gallery).setOnClickListener(this);
        findViewById(R.id.btn_chang_ui).setOnClickListener(this);
        findViewById(R.id.btn_filter).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_camera: {
                startActivity(new Intent(this, CameraActivity.class));
                break;
            }
            case R.id.btn_image: {
                startActivity(new Intent(this, ImageActivity.class));
                break;
            }
            case R.id.btn_video: {
                startActivity(new Intent(this, VideoActivity.class));
                break;
            }
            case R.id.btn_album: {
                startActivity(new Intent(this, AlbumActivity.class));
                break;
            }
            case R.id.btn_gallery: {
                startActivity(new Intent(this, GalleryActivity.class));
                break;
            }
            case R.id.btn_chang_ui: {
                startActivity(new Intent(this, AlbumUIActivity.class));
                break;
            }
            case R.id.btn_filter: {
                startActivity(new Intent(this, AlbumFilterActivity.class));
                break;
            }
        }
    }
}

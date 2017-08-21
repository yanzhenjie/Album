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
package com.yanzhenjie.album.sample.feature;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.sample.R;
import com.yanzhenjie.album.task.DefaultAlbumLoader;

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class CameraActivity extends AppCompatActivity {

    TextView mTextView;
    private ImageView mImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTextView = (TextView) findViewById(R.id.tv_message);
        mImageView = (ImageView) findViewById(R.id.image_view);
    }

    private void takePicture() {
        Album.camera(this)
                .image()
//                .filePath()
                .requestCode(2)
                .listener(new AlbumListener<String>() {
                    @Override
                    public void onAlbumResult(int requestCode, @NonNull String result) {
                        mTextView.setText(result);

                        DefaultAlbumLoader.getInstance()
                                .loadImage(mImageView, result, 720, 1280);
                    }

                    @Override
                    public void onAlbumCancel(int requestCode) {
                        Toast.makeText(CameraActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    private void recordVideo() {
        Album.camera(this)
                .video()
//                .filePath()
                .requestCode(3)
                .quality(1)
                .limitDuration(Long.MAX_VALUE)
                .limitBytes(Long.MAX_VALUE)
                .listener(new AlbumListener<String>() {
                    @Override
                    public void onAlbumResult(int requestCode, @NonNull String result) {
                        mTextView.setText(result);

                        DefaultAlbumLoader.getInstance()
                                .loadVideo(mImageView, result, 720, 1280);
                    }

                    @Override
                    public void onAlbumCancel(int requestCode) {
                        Toast.makeText(CameraActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_camera, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
            case R.id.menu_image_capture: {
                takePicture();
                break;
            }
            case R.id.menu_video_capture: {
                recordVideo();
                break;
            }
        }
        return true;
    }
}

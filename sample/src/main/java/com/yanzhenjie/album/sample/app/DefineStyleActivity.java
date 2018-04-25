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
package com.yanzhenjie.album.sample.app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.sample.R;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class DefineStyleActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView mTvMessage;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_ui);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTvMessage = findViewById(R.id.tv_message);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 10, 10);
        recyclerView.addItemDecoration(divider);

        mAdapter = new Adapter(this, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewAlbum(position);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    /**
     * Select picture, from album.
     */
    private void selectAlbum() {
        Album.album(this)
                .multipleChoice()
                .selectCount(6)
                .camera(true)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newLightBuilder(this)
                                .title(mToolbar.getTitle().toString())
                                .statusBarColor(Color.WHITE)
                                .toolBarColor(Color.WHITE)
                                .mediaItemCheckSelector(Color.BLUE, Color.GREEN)
                                .bucketItemCheckSelector(Color.RED, Color.YELLOW)
                                .buttonStyle(
                                        Widget.ButtonStyle.newLightBuilder(this)
                                                .setButtonSelector(Color.WHITE, Color.WHITE)
                                                .build()
                                )
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);
                        mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(DefineStyleActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to album.
     */
    private void previewAlbum(int position) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            Album.galleryAlbum(this)
                    .checkable(true)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newLightBuilder(this)
                                    .toolBarColor(Color.WHITE)
                                    .statusBarColor(Color.WHITE)
                                    .mediaItemCheckSelector(Color.GREEN, Color.RED)
                                    .bucketItemCheckSelector(Color.GREEN, Color.RED)
                                    .buttonStyle(
                                            Widget.ButtonStyle.newLightBuilder(this)
                                                    .setButtonSelector(Color.WHITE, Color.GRAY)
                                                    .build()
                                    )
                                    .build()
                    )
                    .onResult(new Action<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAction(@NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mAdapter.notifyDataSetChanged(mAlbumFiles);
                            mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        }
                    })
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
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
            case R.id.menu_eye: {
                previewAlbum(0);
                break;
            }
            case R.id.menu_album: {
                selectAlbum();
                break;
            }
        }
        return true;
    }

}
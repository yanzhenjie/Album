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

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumListener;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.sample.R;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.divider.Divider;
import com.yanzhenjie.statusview.StatusUtils;

import java.util.ArrayList;

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class AlbumUIActivity extends AppCompatActivity {

    private TextView mTvMessage;

    private Adapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusUtils.setStatusBarColor(this, Color.WHITE);
        setContentView(R.layout.activity_album_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTvMessage = (TextView) findViewById(R.id.tv_message);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        Divider divider = AlbumUtils.getDivider(Color.WHITE);
        recyclerView.addItemDecoration(divider);

        int itemSize = (DisplayUtils.sScreenWidth - (divider.getWidth() * 4)) / 3;
        mAdapter = new Adapter(this, itemSize, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewAlbum(position);
            }
        });
        recyclerView.setAdapter(mAdapter);

        //noinspection ConstantConditions
        AlbumUtils.setDrawableTint(toolbar.getNavigationIcon(), ContextCompat.getColor(this, R.color.colorPrimaryBlack));
    }

    /**
     * Select picture, from album.
     */
    private void selectAlbum() {
        Album.album(this)
                .multipleChoice()
                .requestCode(200)
                .selectCount(6)
                .camera(true)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newLightBuilder(this)
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
                .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);
                        mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }

                    @Override
                    public void onAlbumCancel(int requestCode) {
                        Toast.makeText(AlbumUIActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
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
                    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
                        @Override
                        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
                            mAlbumFiles = result;
                            mAdapter.notifyDataSetChanged(mAlbumFiles);
                            mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                        }

                        @Override
                        public void onAlbumCancel(int requestCode) {
                        }
                    })
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        MenuItem eyeMenuItem = menu.findItem(R.id.menu_eye);
        AlbumUtils.setDrawableTint(eyeMenuItem.getIcon(), ContextCompat.getColor(this, R.color.colorPrimaryBlack));

        MenuItem albumMenuItem = menu.findItem(R.id.menu_album);
        AlbumUtils.setDrawableTint(albumMenuItem.getIcon(), ContextCompat.getColor(this, R.color.colorPrimaryBlack));
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
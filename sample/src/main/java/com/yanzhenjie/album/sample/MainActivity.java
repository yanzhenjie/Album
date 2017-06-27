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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.sample.util.FileUtils;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.recyclerview.AlbumVerticalGirdDecoration;
import com.yanzhenjie.durban.Durban;
import com.yanzhenjie.mediascanner.MediaScanner;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Album sample.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class MainActivity extends AppCompatActivity {

    private static final int ACTIVITY_REQUEST_SELECT_RADIO = 100;
    private static final int ACTIVITY_REQUEST_SELECT_CROP_RADIO = 101;
    private static final int ACTIVITY_REQUEST_SELECT_PHOTO = 102;
    private static final int ACTIVITY_REQUEST_TAKE_PICTURE = 103;
    private static final int ACTIVITY_REQUEST_PREVIEW_PHOTO = 104;

    private View noneView;

    private RecyclerView mRecyclerView;
    private GridAdapter mGridAdapter;
    private ArrayList<String> mImageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayUtils.initScreen(this);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noneView = findViewById(R.id.none_view);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.decoration_white, null);
        mRecyclerView.addItemDecoration(new AlbumVerticalGirdDecoration(drawable));

        assert drawable != null;
        int itemSize = (DisplayUtils.screenWidth - (drawable.getIntrinsicWidth() * 4)) / 3;
        mGridAdapter = new GridAdapter(this, new OnCompatItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                previewImage(position);
            }
        }, itemSize);
        mRecyclerView.setAdapter(mGridAdapter);

        mImageList = new ArrayList<>();
    }

    /**
     * Select image from fromAlbum.
     */
    private void fromAlbum() {
        Album.album(this)
                .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary)) // Toolbar color.
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar color.
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // NavigationBar color.
                .selectCount(9) // select count.
                .columnCount(2) // span count.
                .camera(true) // has fromCamera function.
                .checkedList(mImageList) // The picture has been selected for anti-election.
                .start(ACTIVITY_REQUEST_SELECT_PHOTO);
    }

    /**
     * Radio mode selection picture.
     */
    private void fromAlbumRadio() {
        Album.albumRadio(this)
                .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary)) // Toolbar color.
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar color.
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // NavigationBar color.
                .columnCount(2) // span count.
                .camera(true) // has fromCamera function.
                .start(ACTIVITY_REQUEST_SELECT_RADIO);
    }

    /**
     * Take a picture from fromCamera.
     */
    private void fromCamera() {
        Album.camera(this)
//                .imagePath() // Specify the image path, optional.
                .start(ACTIVITY_REQUEST_TAKE_PICTURE);
    }

    /**
     * Preview image.
     *
     * @param position current position.
     */
    private void previewImage(int position) {
        Album.gallery(this)
                .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary)) // Toolbar color.
                .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar color.
                .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // NavigationBar color.
                .checkedList(mImageList) // Image list.
                .currentPosition(position) // Preview first to show the first few.
                .checkFunction(true) // Does the user have an anti-selection when previewing.
                .start(ACTIVITY_REQUEST_PREVIEW_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTIVITY_REQUEST_SELECT_RADIO: {
                if (resultCode == RESULT_OK) {
                    // Image crop: https://github.com/yanzhenjie/Durban
                    Durban.with(this)
                            .toolBarColor(ContextCompat.getColor(this, R.color.colorPrimary)) // Toolbar color.
                            .statusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)) // StatusBar color.
                            .navigationBarColor(ActivityCompat.getColor(this, R.color.colorPrimaryBlack)) // NavigationBar color.
                            .inputImagePaths(Album.parseResult(data))
                            .outputDirectory(FileUtils.getRootPath(this).getAbsolutePath())
                            .aspectRatio(1, 1)
                            .maxWidthHeight(500, 500)
                            .requestCode(ACTIVITY_REQUEST_SELECT_CROP_RADIO)
                            .start();
                } else if (resultCode == RESULT_CANCELED) { // User canceled.
                    Snackbar.make(noneView, R.string.cancel_select_photo_hint, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
            case ACTIVITY_REQUEST_SELECT_CROP_RADIO: {
                if (resultCode == RESULT_OK) { // Successfully.
                    List<String> imageList = Durban.parseResult(data); // Parse path.
                    for (String s : imageList) {
                        Log.i("TAG", s);
                    }
                    new MediaScanner(this).scan(imageList); // Scan to system: https://github.com/yanzhenjie/MediaScanner
                    mImageList.addAll(imageList);
                    refreshImage();
                } else if (resultCode == RESULT_CANCELED) { // User canceled.
                    Snackbar.make(noneView, R.string.cancel_select_photo_hint, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
            case ACTIVITY_REQUEST_SELECT_PHOTO: {
                if (resultCode == RESULT_OK) { // Successfully.
                    mImageList = Album.parseResult(data); // Parse select result.
                    refreshImage();
                } else if (resultCode == RESULT_CANCELED) { // User canceled.
                    Snackbar.make(noneView, R.string.cancel_select_photo_hint, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
            case ACTIVITY_REQUEST_TAKE_PICTURE: {
                if (resultCode == RESULT_OK) { // Successfully.
                    List<String> imageList = Album.parseResult(data); // Parse path.
                    mImageList.addAll(imageList);
                    refreshImage();
                } else if (resultCode == RESULT_CANCELED) { // User canceled.
                    Snackbar.make(noneView, R.string.cancel_select_photo_hint, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
            case ACTIVITY_REQUEST_PREVIEW_PHOTO: {
                if (resultCode == RESULT_OK) { // Successfully.
                    mImageList = Album.parseResult(data); // Parse select result.
                    refreshImage();
                }
                break;
            }
        }
    }

    /**
     * Process selection results.
     */
    private void refreshImage() {
        mGridAdapter.notifyDataSetChanged(mImageList);
        if (mImageList == null || mImageList.size() == 0) {
            noneView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        } else {
            noneView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_check_image_face: {
                fromAlbumRadio();
                break;
            }
            case R.id.action_check_image: {
                fromAlbum();
                break;
            }
            case R.id.action_camera_picture: {
                fromCamera();
                break;
            }
        }
        return true;
    }
}

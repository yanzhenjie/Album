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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.sample.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class GalleryActivity extends AppCompatActivity {

    private static final String[] IMAGE_PATH_LIST = {
            "http://pic1.win4000.com/mobile/1/520d9410ebc79.jpg", "http://pic1.win4000.com/mobile/1/520d941218b04.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925tn4a5sz3at2.jpg", "http://wapfile.desktx.com/7681280/0925/160925ruuqfaqwuvx.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925a0x5knyt3ue.jpg", "http://wapfile.desktx.com/7681280/0925/160925jrfl5dnvln1.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925csx5pu2ysam.jpg", "http://wapfile.desktx.com/7681280/0925/160925m4lgeuakuc5.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925gcmbg5gycqw.jpg", "http://wapfile.desktx.com/7681280/0925/160925o0zf2pbozw1.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925danhsef1uzu.jpg", "http://wapfile.desktx.com/7681280/0925/160925y5lmcrqslm4.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925njt3dm2hlw0.jpg", "http://wapfile.desktx.com/7681280/0925/160925xgesspjxztt.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925a5dg2zsddtx.jpg", "http://wapfile.desktx.com/7681280/0925/1609252fupmrdasrp.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925lasaphesxkd.jpg", "http://wapfile.desktx.com/7681280/0925/160925ti2kged45bp.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925dsx3uyapoen.jpg", "http://wapfile.desktx.com/7681280/0925/160925largrb3p2l4.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925l0vaht45uy1.jpg", "http://wapfile.desktx.com/7681280/0925/160925054jrhaerro.jpg",
            "http://wapfile.desktx.com/7681280/0925/1609255mlt2so5yp2.jpg", "http://wapfile.desktx.com/7681280/0925/160925bm3jmwdptie.jpg",
            "http://wapfile.desktx.com/7681280/0925/1609251brfaryoizu.jpg", "http://wapfile.desktx.com/7681280/0925/1609254qfjkbwjlox.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925memb5c4pcnt.jpg", "http://wapfile.desktx.com/7681280/0925/160925c5i4dbhh2rh.jpg",
            "http://wapfile.desktx.com/7681280/0925/1609254pmjqhppmah.jpg", "http://wapfile.desktx.com/7681280/0925/16092505gvkuoampz.jpg",
            "http://wapfile.desktx.com/7681280/0925/1609254dxk3vpsrvj.jpg", "http://wapfile.desktx.com/7681280/0925/160925bgyxuik3j4y.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925jbhhtendosg.jpg", "http://wapfile.desktx.com/7681280/0925/160925knnv51uglia.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925vajxn2aqfkc.jpg", "http://wapfile.desktx.com/7681280/0925/160925xehnehy5frf.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925e2rajc4zi4k.jpg", "http://wapfile.desktx.com/7681280/0925/160925dfxg2wlarga.jpg",
            "http://wapfile.desktx.com/7681280/0925/160925vuifhcqactt.jpg", "http://wapfile.desktx.com/7681280/0925/160925rcxiez3ikcg.jpg"
    };

    private Toolbar mToolbar;
    private CheckBox mCheckBox;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCheckBox = findViewById(R.id.checkbox);
        findViewById(R.id.btn_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previewImages();
            }
        });
    }

    private void previewImages() {
        ArrayList<String> imageList = new ArrayList<>();
        Collections.addAll(imageList, IMAGE_PATH_LIST);

        Album.gallery(this)
                .checkedList(imageList)
                .checkable(mCheckBox.isChecked())
                .widget(
                        Widget.newDarkBuilder(this)
                                .title(mToolbar.getTitle().toString())
                                .build()
                )
                .onResult(new Action<ArrayList<String>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<String> result) {
                        // TODO If it is optional, here you can accept the results of user selection.
                    }
                })
                .start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

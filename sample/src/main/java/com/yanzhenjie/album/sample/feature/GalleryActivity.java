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
            "http://i3.bbs.fd.zol-img.com.cn/t_s800x5000/g3/M04/07/0E/Cg-4V1GUurCIEoj2AAskZb1av5gAAIe1QFhxFgACyR9799.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_bd08f826a59b7424e98eB1ij8y9YAWID.jpg",
            "http://i2.bbs.fd.zol-img.com.cn/t_s800x5000/g3/M04/07/0E/Cg-4WFGUusyIfe_-AAnBY9FU2tsAAIe1QLiUuQACcF7472.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_dc874eab81e679ac49b7evQ5QDh5ZAJN.jpg",
            "http://i4.bbs.fd.zol-img.com.cn/t_s800x5000/g3/M04/07/0E/Cg-4WFGUus-IP0JPAAmdjBDYP3gAAIe1QMXheEACZ2k634.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_8d6ea1ccfaa12e923383181z2NJoFjUU.jpg",
            "http://i1.bbs.fd.zol-img.com.cn/t_s800x5000/g3/M05/07/0E/Cg-4WFGUutiIaJ-zAArt-GjXe2MAAIe1gAuIlYACu4Q927.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_465ae973fe6a63d25fc795O9L3L3Mdl8.jpg",
            "http://i4.bbs.fd.zol-img.com.cn/t_s800x5000/g4/M07/07/0E/Cg-4WVGUuuOIIoUFABDK84KlTV8AAIe8ABL3NYAEMsL854.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_7ef133d562957e27be33aOLlEAEC31RM.jpg",
            "http://i2.bbs.fd.zol-img.com.cn/t_s800x5000/g4/M07/07/0E/Cg-4WlGUuuiIIbzsAAoUT9aU-aIAAIe8AC2hJ4AChRn222.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_26ff8033492435ed0ad6Q1ZzKq79H2oQ.jpg",
            "http://i5.bbs.fd.zol-img.com.cn/t_s800x5000/g4/M07/07/0E/Cg-4WVGUuuyIXN_MAAfgzhGTg_QAAIe8AELPZAAB-Dm065.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_9c701ead392800c28a7fE8Uu7iTUUukC.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_ee824127f5f6784addf5vVu6JE6vJQnP.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_6e69ca04cab3beda6248VmPZ242k9T6h.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_71cc2e187d7820710d10bz3bj36as75j.jpg",
            "http://attimg.dospy.com/img/day_150423/20150423_e6977d9cff8f545b7022iUwTjc591KI9.jpg"
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
                .requestCode(2)
                .checkedList(imageList)
                .navigationAlpha(80)
                .checkable(mCheckBox.isChecked())
                .widget(
                        Widget.newDarkBuilder(this)
                                .title(mToolbar.getTitle().toString())
                                .build()
                )
                .onResult(new Action<ArrayList<String>>() {
                    @Override
                    public void onAction(int requestCode, @NonNull ArrayList<String> result) {
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

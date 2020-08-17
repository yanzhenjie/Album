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
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.sample.R;

import java.util.ArrayList;
import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Created by YanZhenjie on 2017/8/17.
 */
public class GalleryActivity extends AppCompatActivity {

    private static final String[] IMAGE_PATH_LIST = {
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511268196&di=a17c805213846a55eb067565cda0ae0f&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170314%2F20170314093020_61f5e1c760157917576b50b62bc59c80_7.jpeg", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298118&di=263456f22d8b03397cc467c82c9cd195&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181003%2F0f8307fe3de6468d8b51c53b276e9e1b.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298118&di=b2944fc7eef07d274d629ba9a2eff5be&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fd%2F574bd264dc04f.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298118&di=f6e60ae16352a14b848dba216af784f4&imgtype=0&src=http%3A%2F%2Fp0.qhimg.com%2Ft01dc11f29c01424c6d.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298114&di=156b200f20eb702f2ad3ba8fc5b55ba2&imgtype=0&src=http%3A%2F%2Fpic.feizl.com%2Fupload%2Fallimg%2F170614%2F1410251g5-0.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298114&di=2194e443572fcac6b86e8ca9a3eb9acc&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20170303%2F20170303140548_7cb2d40a62600c2ee10f9c826dedfb8b_9.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298112&di=69a2ac37a9b763829f3c26195a7fcbf0&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn18%2F410%2Fw1470h2140%2F20180525%2F59be-haysviy8786664.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298111&di=40e9330ad50aafb884d6699801f1d2d2&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F2%2F48%2F542c1208181.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298110&di=039ff5882950f28189cea1ad6f1a25e4&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2Fb%2F5680fcc0d31b2.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298110&di=ed3cf8b466ca36997ce94201f392d8c7&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2Fa%2F64%2F79121317058.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298109&di=c95f19699712aa1c51b3bff4f0f6a0cc&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170421%2F20170421142504_aa430cd7552a22e19a91054d15ae0905_14.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298109&di=8ce9291fd03a7e225cfe0f4cf4524769&imgtype=0&src=http%3A%2F%2Fimage.hnol.net%2Fc%2F2013-03%2F20%2F19%2F20130320191211951-2089977.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298256&di=51e80d7ccbda7e72856a9db4d0961afa&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201707%2F17%2F20170717230736_RWUmT.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298255&di=8ec2d086beaa20b3414eb357c4bd2286&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1405%2F28%2Fc3%2F34712742_34712742_1401260899709.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298256&di=d2a6ce2938738d40ae62119e68573b18&imgtype=0&src=http%3A%2F%2Fimg4.cache.netease.com%2Fphoto%2F0026%2F2013-07-08%2F938RKTEP43AJ0026.JPG",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298255&di=fa5fbffe31186ea20df334ab4063725c&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170306%2F20170306111324_d2b7a6d928ad827f4db19f355b9550d4_5.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298255&di=da54af6be9cce91a57426f9cb684d49f&imgtype=0&src=http%3A%2F%2Fi3.17173.itc.cn%2F2008%2Fjr%2F2008%2F11%2F20%2F8.JPG",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298255&di=850c6e1979c1cac2faebe91ec910913a&imgtype=0&src=http%3A%2F%2Fimage.hnol.net%2Fc%2F2016-01%2F30%2F18%2F201601301826324531-3356886.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298254&di=5bc035ef1f5ac4ffb90fbf6fe62027ae&imgtype=0&src=http%3A%2F%2Fp0.ifengimg.com%2Fa%2F2016_33%2Fb9050beb18718b0_size524_w3885_h2745.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298254&di=8cb3d6fac8e62a0291aabde8aad08dab&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20190126%2F6727836cd8c84e3f9f02ecb97f4cc5cc.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298254&di=43f222be11c12cbf8cc77ca55ca08c11&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170506%2F20170506134244_50112ebd2eb624744aa8b957008a8d71_4.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511954989&di=8a4a9739fab70902712c00ff9828428f&imgtype=0&src=http%3A%2F%2Fimg4.imgtn.bdimg.com%2Fit%2Fu%3D3556124484%2C830383795%26fm%3D214%26gp%3D0.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298253&di=82c1aff5dfde5886b2f10338d7733dc0&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F2018-09-10%2F5b961ed1809f1.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298253&di=65206968f3d93a8e8ad4783356d7fd87&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20170122%2F20170122133549_02dc7a8d994ea57c5a7c11421a56eb2f_2.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298253&di=8aa1beeca9e2274fc8bc7055fd815d0f&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20170417%2F20170417154748_f05b8931f7bed94d8a76e9f96d784a8b_9.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298253&di=f5fa7bfe3aed8d636fa8716738415d90&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F7%2F07%2F26501170849.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298253&di=05e0dd05b7ea3e283bcc4a9f189970b5&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn10111%2F710%2Fw1600h2310%2F20181207%2F8102-hpinryc5855494.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298252&di=03c16c31e38886781a1ed04fe3a1f986&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F7%2Fe8%2Fa8781211260.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298252&di=efc9e9755ae73cfcbeeefc8088165ac1&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170421%2F20170421142504_aa430cd7552a22e19a91054d15ae0905_9.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298252&di=d68706f875990e682a152292beb7bbb3&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170519%2F20170519154440_0791cfd6f5d25410bcde032e77afb40d_2.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298251&di=0064201bef322ecfdc7359f7bbdabb7e&imgtype=0&src=http%3A%2F%2Fwww.deskcar.com%2Fdesktop%2Fstar%2Fchina%2F20151124212913%2F7.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298251&di=41c055afa9f969fa4096fd44e41dc094&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F5%2F39%2F58581218506.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298251&di=5ebc26be806759d833492f14887fe6bb&imgtype=0&src=http%3A%2F%2Fwww.flybridal.com%2Fhuangse%2FaHR0cDovL3BpYzEud2luNDAwMC5jb20vcGljL2YvMzMvM2I5YjMwMTQyNi5qcGc%3D.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298250&di=237cae63df3dad5ef3a8a61acf6e36b9&imgtype=0&src=http%3A%2F%2Fimg.pconline.com.cn%2Fimages%2Fupload%2Fupc%2Ftx%2Fphotoblog%2F1111%2F24%2Fc2%2F9721899_9721899_1322103707644.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298249&di=79bc09a920abfc3601420db91e3c759d&imgtype=0&src=http%3A%2F%2F00.minipic.eastday.com%2F20170407%2F20170407091825_3cbfafdd9a6fe547be7cab4ae702077e_2.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511298248&di=105a164fdb6c8192b2a8576bcc1250cb&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181125%2F5369ec03f8274b4c889537fea5b635e9.jpeg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511994779&di=99bddb40e08488550d893ebf8f0ff892&imgtype=0&src=http%3A%2F%2Fimg.improve-yourmemory.com%2Fpic%2F913f45ee741278fc4213e5d861fe2acb-4.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511994779&di=170a327f5f388d45aeafd88a5775c727&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fpic%2F0%2F29%2F163a1215183.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511994778&di=1a37f80710cd3dd09aed0e0ee3fdf772&imgtype=0&src=http%3A%2F%2Fpic1.win4000.com%2Fwallpaper%2F1%2F50655aa104436.jpg",
        "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1595511994777&di=d1feb652465b5af592a82f2b6c36a1bc&imgtype=0&src=http%3A%2F%2F01.minipic.eastday.com%2F20170308%2F20170308093953_6e555cf710ad2a15a03ef91239032c85_4.jpeg"
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

}

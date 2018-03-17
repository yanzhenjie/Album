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
package com.yanzhenjie.album.ui;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.ui.adapter.AlbumFilePreviewAdapter;
import com.yanzhenjie.album.ui.adapter.BasicPreviewAdapter;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.fragment.NoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Preview the pictures in the folder in enlarged form.</p>
 * Created by Yan Zhenjie on 2017/3/25.
 */
public class AlbumPreviewFragment extends NoFragment {

    private MenuItem mFinishMenuItem;

    private ViewPager mViewPager;
    private TextView mTvDuration;
    private AppCompatCheckBox mCheckBox;
    private FrameLayout mLayoutLayer;

    @Album.ChoiceFunction
    private int mFunction;
    private Widget mWidget;
    private int mAllowSelectCount;

    private List<AlbumFile> mAlbumFiles = new ArrayList<>(1);
    private List<AlbumFile> mCheckedFiles = new ArrayList<>(1);
    private int mCurrentItemPosition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_fragment_preview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mTvDuration = (TextView) view.findViewById(R.id.tv_duration);
        mCheckBox = (AppCompatCheckBox) view.findViewById(R.id.cb_album_check);
        mLayoutLayer = (FrameLayout) view.findViewById(R.id.layout_layer);

        setToolbar(toolbar);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        displayHomeAsUpEnabled(R.drawable.album_ic_back_white);

        Bundle argument = getArguments();
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        //noinspection WrongConstant
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION);
        mAllowSelectCount = argument.getInt(Album.KEY_INPUT_LIMIT_COUNT, Integer.MAX_VALUE);

        initializeWidget();
        initializeViewPager();

        setCheckedCountUI(mCheckedFiles.size());
    }

    private void initializeWidget() {
        ColorStateList itemSelector = mWidget.getMediaItemCheckSelector();
        mCheckBox.setSupportButtonTintList(itemSelector);

        mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = mCheckBox.isChecked();
                AlbumFile albumFile = mAlbumFiles.get(mCurrentItemPosition);
                albumFile.setChecked(isChecked);

                if (isChecked) {
                    if (mCheckedFiles.size() >= mAllowSelectCount) {
                        int messageRes;
                        switch (mFunction) {
                            case Album.FUNCTION_CHOICE_IMAGE: {
                                messageRes = R.plurals.album_check_image_limit;
                                break;
                            }
                            case Album.FUNCTION_CHOICE_VIDEO: {
                                messageRes = R.plurals.album_check_video_limit;
                                break;
                            }
                            case Album.FUNCTION_CHOICE_ALBUM:
                            default: {
                                messageRes = R.plurals.album_check_album_limit;
                                break;
                            }
                        }
                        Toast.makeText(
                                getContext(),
                                getResources().getQuantityString(messageRes, mAllowSelectCount, mAllowSelectCount),
                                Toast.LENGTH_LONG).show();

                        mCheckBox.setChecked(false);
                        albumFile.setChecked(false);
                    } else {
                        mCheckedFiles.add(albumFile);
                    }
                } else {
                    mCheckedFiles.remove(albumFile);
                }
                setCheckedCountUI(mCheckedFiles.size());
            }
        });
    }

    private void initializeViewPager() {
        if (mAlbumFiles != null) {
            if (mAlbumFiles.size() > 3)
                mViewPager.setOffscreenPageLimit(3);
            else if (mAlbumFiles.size() > 2)
                mViewPager.setOffscreenPageLimit(2);
        }

        BasicPreviewAdapter previewAdapter = new AlbumFilePreviewAdapter(getContext(), mAlbumFiles);
        mViewPager.setAdapter(previewAdapter);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentItemPosition = position;
                AlbumFile albumFile = mAlbumFiles.get(mCurrentItemPosition);
                mCheckBox.setChecked(albumFile.isChecked());
                mCheckBox.setEnabled(!albumFile.isDisable());
                setTitle(mCurrentItemPosition + 1 + " / " + mAlbumFiles.size());

                if (albumFile.getMediaType() == AlbumFile.TYPE_VIDEO) {
                    mTvDuration.setText(AlbumUtils.convertDuration(albumFile.getDuration()));
                    mTvDuration.setVisibility(View.VISIBLE);
                } else {
                    mTvDuration.setVisibility(View.GONE);
                }

                mLayoutLayer.setVisibility(albumFile.isDisable() ? View.VISIBLE : View.GONE);
            }
        };
        mViewPager.addOnPageChangeListener(pageChangeListener);
        mViewPager.setCurrentItem(mCurrentItemPosition);
        // Forced call.
        pageChangeListener.onPageSelected(mCurrentItemPosition);
    }

    /**
     * Bind the click folder.
     *
     * @param albumFiles image list.
     */
    public void bindAlbumFiles(List<AlbumFile> albumFiles, List<AlbumFile> checkedFiles, int currentItemPosition) {
        this.mAlbumFiles.addAll(albumFiles);
        this.mCheckedFiles = checkedFiles;
        this.mCurrentItemPosition = currentItemPosition;
    }

    /**
     * Set the number of selected pictures.
     *
     * @param count number.
     */
    private void setCheckedCountUI(int count) {
        String finishStr = getString(R.string.album_menu_finish);
        finishStr += "(" + count + " / " + mAllowSelectCount + ")";
        mFinishMenuItem.setTitle(finishStr);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.album_menu_preview, menu);
        mFinishMenuItem = menu.findItem(R.id.album_menu_finish);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.album_menu_finish) {
            setResult(RESULT_OK);
            finish();
        }
        return true;
    }
}

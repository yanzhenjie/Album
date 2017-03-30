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
package com.yanzhenjie.album.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.yanzhenjie.album.AlbumWrapper;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.adapter.AlbumImageAdapter;
import com.yanzhenjie.album.dialog.AlbumFolderDialog;
import com.yanzhenjie.album.entity.AlbumFolder;
import com.yanzhenjie.album.entity.AlbumImage;
import com.yanzhenjie.album.impl.OnCompatItemClickListener;
import com.yanzhenjie.album.impl.OnCompoundItemCheckListener;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.recyclerview.AlbumVerticalGirdDecoration;

import java.util.List;
import java.util.Locale;

/**
 * <p>Image list fragment.</p>
 * Created by Yan Zhenjie on 2017/3/25.
 */
public class AlbumFragment extends BasicCameraFragment {

    private Callback mCallback;

    private int mToolBarColor;
    private int mNavigationColor;

    private Button mBtnPreview;
    private Button mBtnSwitchFolder;

    private RecyclerView mRvContentList;
    private GridLayoutManager mLayoutManager;

    private AlbumImageAdapter mAlbumContentAdapter;

    private List<AlbumFolder> mAlbumFolders;
    private int mCurrentFolderPosition;

    private int mAllowSelectCount;

    private AlbumFolderDialog mAlbumFolderDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (Callback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mCallback = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.album_fragment_album, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mBtnPreview = (Button) view.findViewById(R.id.btn_preview);
        mBtnSwitchFolder = (Button) view.findViewById(R.id.btn_switch_dir);
        mRvContentList = (RecyclerView) view.findViewById(R.id.rv_content_list);

        setToolbar((Toolbar) view.findViewById(R.id.toolbar));
        displayHomeAsUpEnabled(R.drawable.album_ic_back_white);
        setTitle(R.string.album_title);

        mBtnSwitchFolder.setOnClickListener(mSwitchDirClick);
        mBtnPreview.setOnClickListener(mPreviewClick);
    }

    @Override
    public boolean onInterceptToolbarBack() {
        mCallback.doResult(false);
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle argument = getArguments();
        mToolBarColor = argument.getInt(
                AlbumWrapper.KEY_INPUT_TOOLBAR_COLOR,
                ContextCompat.getColor(getContext(), R.color.albumColorPrimary));
        mNavigationColor = argument.getInt(
                AlbumWrapper.KEY_INPUT_NAVIGATION_COLOR,
                ContextCompat.getColor(getContext(), R.color.albumColorPrimaryBlack));
        int columnCount = argument.getInt(AlbumWrapper.KEY_INPUT_COLUMN_COUNT, 2);
        mAllowSelectCount = argument.getInt(AlbumWrapper.KEY_INPUT_LIMIT_COUNT, Integer.MAX_VALUE);
        boolean hasCamera = argument.getBoolean(AlbumWrapper.KEY_INPUT_ALLOW_CAMERA, true);

        // noinspection ConstantConditions
        getToolbar().setBackgroundColor(mToolBarColor);

        mLayoutManager = new GridLayoutManager(getContext(), columnCount);
        mRvContentList.setLayoutManager(mLayoutManager);

        Drawable decoration = ContextCompat.getDrawable(getContext(), R.drawable.album_decoration_white);
        mRvContentList.addItemDecoration(new AlbumVerticalGirdDecoration(decoration));

        int itemSize = (DisplayUtils.screenWidth - decoration.getIntrinsicWidth() * (columnCount + 1)) / columnCount;
        mAlbumContentAdapter = new AlbumImageAdapter(
                getContext(),
                hasCamera,
                itemSize,
                ContextCompat.getColor(getContext(), R.color.albumWhiteGray),
                mToolBarColor);
        mAlbumContentAdapter.setAddPhotoClickListener(mAddPhotoListener);
        mAlbumContentAdapter.setOnCheckListener(mItemCheckListener);
        mAlbumContentAdapter.setItemClickListener(new OnCompatItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCallback.onPreviewFolder(mCurrentFolderPosition, position);
            }
        });
        mRvContentList.setAdapter(mAlbumContentAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        showImageFromFolder(mCurrentFolderPosition);
        setCheckedCountUI(mCallback.getCheckedCount());
    }

    /**
     * Bind the album data source.
     *
     * @param albumFolders folder list.
     */
    public void bindAlbumFolders(List<AlbumFolder> albumFolders) {
        this.mAlbumFolders = albumFolders;
    }

    /**
     * Switch folder.
     */
    private View.OnClickListener mSwitchDirClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mAlbumFolderDialog == null)
                mAlbumFolderDialog = new AlbumFolderDialog(
                        getContext(),
                        mToolBarColor,
                        mNavigationColor,
                        mAlbumFolders,
                        new OnCompatItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (mAlbumFolders.size() > position) {
                                    mCurrentFolderPosition = position;
                                    showImageFromFolder(mCurrentFolderPosition);
                                    mLayoutManager.scrollToPosition(0);
                                }
                            }
                        });
            if (!mAlbumFolderDialog.isShowing())
                mAlbumFolderDialog.show();
        }
    };

    /**
     * Update data source.
     */
    private void showImageFromFolder(int position) {
        AlbumFolder albumFolder = mAlbumFolders.get(position);
        mBtnSwitchFolder.setText(albumFolder.getName());
        mAlbumContentAdapter.notifyDataSetChanged(albumFolder.getImages());
    }

    /**
     * Camera.
     */
    private OnCompatItemClickListener mAddPhotoListener = new OnCompatItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            int hasCheckSize = mCallback.getCheckedCount();
            if (hasCheckSize == mAllowSelectCount)
                Toast.makeText(
                        getContext(),
                        String.format(Locale.getDefault(), getString(R.string.album_check_limit_camera), mAllowSelectCount),
                        Toast.LENGTH_LONG).show();
            else
                cameraUnKnowPermission();
        }
    };

    /**
     * When a picture is selected.
     */
    private OnCompoundItemCheckListener mItemCheckListener = new OnCompoundItemCheckListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, int position, boolean isChecked) {
            AlbumImage albumImage = mAlbumFolders.get(mCurrentFolderPosition).getImages().get(position);
            albumImage.setChecked(isChecked);

            mCallback.onCheckedChanged(albumImage, isChecked);

            int hasCheckSize = mCallback.getCheckedCount();
            if (hasCheckSize > mAllowSelectCount) {
                Toast.makeText(
                        getContext(),
                        String.format(Locale.getDefault(), getString(R.string.album_check_limit), mAllowSelectCount),
                        Toast.LENGTH_LONG).show();

                mCallback.onCheckedChanged(albumImage, false);
                buttonView.setChecked(false);
                albumImage.setChecked(false);
            } else {
                setCheckedCountUI(hasCheckSize);
            }
        }
    };

    @Override
    protected void onCameraBack(String imagePath) {
        mCallback.onCameraBack(imagePath);
    }

    /**
     * Set the number of selected pictures.
     *
     * @param count number.
     */
    public void setCheckedCountUI(int count) {
        mBtnPreview.setText(" (" + count + ")");
        // noinspection ConstantConditions
        getToolbar().setSubtitle(count + "/" + mAllowSelectCount);
    }

    /**
     * The preview button is clicked.
     */
    private View.OnClickListener mPreviewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCallback.onPreviewChecked();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.album_menu_album, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            mCallback.doResult(false);
        } else if (itemId == R.id.album_menu_finish) {
            mCallback.doResult(true);
        }
        return true;
    }

    public interface Callback extends CameraCallback, com.yanzhenjie.album.fragment.Callback {

        /**
         * Preview all selected images.
         */
        void onPreviewChecked();

        /**
         * Preview all pictures of a folder.
         *
         * @param folderPosition folder's index in sources.
         * @param itemPosition   image's index in folder.
         */
        void onPreviewFolder(int folderPosition, int itemPosition);

    }

}

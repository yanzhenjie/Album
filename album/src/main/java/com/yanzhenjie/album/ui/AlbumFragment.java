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
package com.yanzhenjie.album.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;
import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.Filter;
import com.yanzhenjie.album.R;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.impl.AlbumCallback;
import com.yanzhenjie.album.impl.OnItemCheckedListener;
import com.yanzhenjie.album.impl.OnItemClickListener;
import com.yanzhenjie.album.task.MediaReadTask;
import com.yanzhenjie.album.task.PathConvertTask;
import com.yanzhenjie.album.ui.adapter.AlbumFileAdapter;
import com.yanzhenjie.album.util.AlbumUtils;
import com.yanzhenjie.album.util.DisplayUtils;
import com.yanzhenjie.album.widget.divider.Divider;
import com.yanzhenjie.fragment.NoFragment;
import com.yanzhenjie.mediascanner.MediaScanner;
import com.yanzhenjie.statusview.StatusView;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Image list fragment.</p>
 * Created by Yan Zhenjie on 2017/3/25.
 */
public class AlbumFragment extends NoFragment {

    private static final int REQUEST_CODE_FRAGMENT_PREVIEW = 100;
    private static final int REQUEST_CODE_FRAGMENT_NULL = 101;

    private static final int REQUEST_CODE_CAMERA_IMAGE = 102;
    private static final int REQUEST_CODE_CAMERA_VIDEO = 103;

    private StatusView mStatusView;
    private Toolbar mToolbar;
    private MenuItem mFinishMenuItem;
    private Button mBtnPreview;
    private Button mBtnSwitchFolder;
    private AlbumFolderDialog mFolderDialog;

    private RecyclerView mRvContentList;
    private GridLayoutManager mLayoutManager;
    private AlbumFileAdapter mAlbumContentAdapter;

    private List<AlbumFolder> mAlbumFolders;
    private int mCurrentFolder;

    private PopupMenu mCameraPopupMenu;

    private Widget mWidget;
    private ArrayList<AlbumFile> mCheckedList = new ArrayList<>();
    @Album.ChoiceFunction
    private int mFunction;
    @Album.ChoiceMode
    private int mChoiceMode;
    private int mColumnCount;
    private boolean mHasCamera;
    private int mLimitCount;

    private MediaScanner mMediaScanner;
    private AlbumCallback mCallback;

    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;
    private boolean mFilterVisibility;

    @IntRange(from = 0, to = 1)
    private int mQuality = 1;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitDuration;
    @IntRange(from = 1, to = Long.MAX_VALUE)
    private long mLimitBytes;

    public void setSizeFilter(Filter<Long> sizeFilter) {
        this.mSizeFilter = sizeFilter;
    }

    public void setMimeFilter(Filter<String> mimeFilter) {
        this.mMimeFilter = mimeFilter;
    }

    public void setDurationFilter(Filter<Long> durationFilter) {
        this.mDurationFilter = durationFilter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mCallback = (AlbumCallback) context;
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
        mStatusView = (StatusView) view.findViewById(R.id.status_view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mBtnPreview = (Button) view.findViewById(R.id.btn_preview);
        mBtnSwitchFolder = (Button) view.findViewById(R.id.btn_switch_dir);
        mRvContentList = (RecyclerView) view.findViewById(R.id.rv_content_list);
        setToolbar(mToolbar);

        mBtnSwitchFolder.setOnClickListener(mSwitchDirClick);
        mBtnPreview.setOnClickListener(mPreviewClick);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.album_menu_album, menu);
        mFinishMenuItem = menu.findItem(R.id.album_menu_finish);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            mCallback.onAlbumCancel();
        } else if (itemId == R.id.album_menu_finish) {
            onAlbumResult();
        }
        return true;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Bundle argument = getArguments();
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        // noinspection WrongConstant
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION);
        // noinspection WrongConstant
        mChoiceMode = argument.getInt(Album.KEY_INPUT_CHOICE_MODE);
        mColumnCount = argument.getInt(Album.KEY_INPUT_COLUMN_COUNT);
        mHasCamera = argument.getBoolean(Album.KEY_INPUT_ALLOW_CAMERA);
        mLimitCount = argument.getInt(Album.KEY_INPUT_LIMIT_COUNT);
        mQuality = argument.getInt(Album.KEY_INPUT_CAMERA_QUALITY, 1);
        mLimitDuration = argument.getLong(Album.KEY_INPUT_CAMERA_DURATION, Long.MAX_VALUE);
        mLimitBytes = argument.getLong(Album.KEY_INPUT_CAMERA_BYTES, Long.MAX_VALUE);

        initializeWidget();

        mLayoutManager = new GridLayoutManager(getContext(), mColumnCount);
        mRvContentList.setLayoutManager(mLayoutManager);

        Divider divider = AlbumUtils.getDivider(Color.WHITE);
        mRvContentList.addItemDecoration(divider);

        int itemSize = (DisplayUtils.sScreenWidth - divider.getWidth() * (mColumnCount + 1)) / mColumnCount;
        mAlbumContentAdapter = new AlbumFileAdapter(
                getContext(),
                itemSize,
                mHasCamera,
                mChoiceMode,
                mWidget.getMediaItemCheckSelector());
        mAlbumContentAdapter.setAddClickListener(mAddPhotoListener);
        mAlbumContentAdapter.setItemCheckedListener(mItemCheckListener);
        mAlbumContentAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                List<AlbumFile> albumFiles = mAlbumFolders.get(mCurrentFolder).getAlbumFiles();
                AlbumPreviewFragment previewFragment = fragment(AlbumPreviewFragment.class, argument);
                previewFragment.bindAlbumFiles(albumFiles, mCheckedList, position);
                startFragmentForResult(previewFragment, REQUEST_CODE_FRAGMENT_PREVIEW);
            }
        });
        mRvContentList.setAdapter(mAlbumContentAdapter);

        mFilterVisibility = argument.getBoolean(Album.KEY_INPUT_FILTER_VISIBILITY, true);
        MediaReadTask scanTask = new MediaReadTask(getContext(), mFunction, mScanCallback, mCheckedList,
                mSizeFilter, mMimeFilter, mDurationFilter, mFilterVisibility);
        ArrayList<AlbumFile> checkedList = argument.getParcelableArrayList(Album.KEY_INPUT_CHECKED_LIST);
        //noinspection unchecked
        scanTask.execute(checkedList);
    }

    private void initializeWidget() {
        int statusBarColor = mWidget.getStatusBarColor();
        Drawable navigationIcon = ContextCompat.getDrawable(getContext(), R.drawable.album_ic_back_white);
        if (mWidget.getStyle() == Widget.STYLE_LIGHT) {
            if (((AlbumActivity) getActivity()).isSucceedLightStatus()) {
                mStatusView.setBackgroundColor(statusBarColor);
            } else {
                mStatusView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.album_ColorPrimaryBlack));
            }

            mToolbar.setTitleTextColor(ContextCompat.getColor(getContext(), R.color.album_FontDark));
            mToolbar.setSubtitleTextColor(ContextCompat.getColor(getContext(), R.color.album_FontDark));

            AlbumUtils.setDrawableTint(navigationIcon, ContextCompat.getColor(getContext(), R.color.album_IconDark));
            displayHomeAsUpEnabled(navigationIcon);

            AlbumUtils.setDrawableTint(mFinishMenuItem.getIcon(), ContextCompat.getColor(getContext(), R.color.album_IconDark));
            CharSequence title = mFinishMenuItem.getTitle();
            title = AlbumUtils.getColorText(title,
                    0,
                    title.length(),
                    ContextCompat.getColor(getContext(), R.color.album_FontDark));
            mFinishMenuItem.setTitle(title);
        } else {
            mStatusView.setBackgroundColor(statusBarColor);
            displayHomeAsUpEnabled(navigationIcon);
        }

        mToolbar.setBackgroundColor(mWidget.getToolBarColor());
        mToolbar.setTitle(mWidget.getTitle());
    }

    /**
     * Scan the picture result callback.
     */
    private MediaReadTask.Callback mScanCallback = new MediaReadTask.Callback() {
        @Override
        public void onScanCallback(ArrayList<AlbumFolder> folders) {
            mAlbumFolders = folders;
            if (mAlbumFolders.get(0).getAlbumFiles().size() == 0) {
                AlbumNullFragment nullFragment = fragment(AlbumNullFragment.class, getArguments());
                startFragmentForResult(nullFragment, REQUEST_CODE_FRAGMENT_NULL);
            } else showAlbumFileFromFolder(0);
            setCheckedCountUI(mCheckedList.size());
        }
    };

    /**
     * Update data source.
     */
    private void showAlbumFileFromFolder(int position) {
        AlbumFolder albumFolder = mAlbumFolders.get(position);
        mBtnSwitchFolder.setText(albumFolder.getName());
        mAlbumContentAdapter.notifyDataSetChanged(albumFolder.getAlbumFiles());
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, @Nullable Bundle result) {
        switch (requestCode) {
            case REQUEST_CODE_FRAGMENT_PREVIEW: { // Preview finish, to refresh check status.
                showAlbumFileFromFolder(mCurrentFolder);
                setCheckedCountUI(mCheckedList.size());

                if (resultCode == RESULT_OK) {
                    onAlbumResult();
                }
                break;
            }
            case REQUEST_CODE_FRAGMENT_NULL: {
                if (resultCode == RESULT_OK) {
                    String imagePath = AlbumNullFragment.parseImagePath(result);
                    String extension = MimeTypeMap.getFileExtensionFromUrl(imagePath);
                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                    if (!TextUtils.isEmpty(mimeType)) {
                        mCameraAction.onAction(mimeType.contains("image") ? REQUEST_CODE_CAMERA_IMAGE : REQUEST_CODE_CAMERA_VIDEO, imagePath);
                    }
                } else {
                    mCallback.onAlbumCancel();
                }
                break;
            }
        }
    }

    /**
     * Switch folder.
     */
    private View.OnClickListener mSwitchDirClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mFolderDialog == null)
                mFolderDialog = new AlbumFolderDialog(
                        getContext(),
                        mWidget,
                        mAlbumFolders,
                        new OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if (mAlbumFolders.size() > position) {
                                    mCurrentFolder = position;
                                    showAlbumFileFromFolder(mCurrentFolder);
                                    mLayoutManager.scrollToPosition(0);
                                }
                            }
                        });
            if (!mFolderDialog.isShowing())
                mFolderDialog.show();
        }
    };

    /**
     * Camera.
     */
    private OnItemClickListener mAddPhotoListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            int hasCheckSize = mCheckedList.size();
            if (hasCheckSize >= mLimitCount) {
                int messageRes;
                switch (mFunction) {
                    case Album.FUNCTION_CHOICE_IMAGE: {
                        messageRes = R.plurals.album_check_image_limit_camera;
                        break;
                    }
                    case Album.FUNCTION_CHOICE_VIDEO: {
                        messageRes = R.plurals.album_check_video_limit_camera;
                        break;
                    }
                    case Album.FUNCTION_CHOICE_ALBUM:
                    default: {
                        messageRes = R.plurals.album_check_album_limit_camera;
                        break;
                    }
                }
                Toast.makeText(
                        getContext(),
                        getResources().getQuantityString(messageRes, mLimitCount, mLimitCount),
                        Toast.LENGTH_LONG).show();
            } else {
                switch (mFunction) {
                    case Album.FUNCTION_CHOICE_IMAGE: {
                        Album.camera(getContext())
                                .image()
                                .requestCode(REQUEST_CODE_CAMERA_IMAGE)
                                .onResult(mCameraAction)
                                .start();
                        break;
                    }
                    case Album.FUNCTION_CHOICE_VIDEO: {
                        Album.camera(getContext())
                                .video()
                                .quality(mQuality)
                                .limitDuration(mLimitDuration)
                                .limitBytes(mLimitBytes)
                                .requestCode(REQUEST_CODE_CAMERA_VIDEO)
                                .onResult(mCameraAction)
                                .start();
                        break;
                    }
                    case Album.FUNCTION_CHOICE_ALBUM:
                    default: {
                        if (mCameraPopupMenu == null) {
                            mCameraPopupMenu = new PopupMenu(getContext(), view);
                            mCameraPopupMenu.getMenuInflater().inflate(R.menu.album_menu_item_camera, mCameraPopupMenu.getMenu());
                            mCameraPopupMenu.setOnMenuItemClickListener(mCameraItemMenuClickListener);
                        }
                        mCameraPopupMenu.show();
                    }
                }
            }
        }
    };

    /**
     * The menu's item's click event of CameraButton.
     */
    private PopupMenu.OnMenuItemClickListener mCameraItemMenuClickListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.album_menu_camera_image) {
                Album.camera(getContext())
                        .image()
                        .requestCode(REQUEST_CODE_CAMERA_IMAGE)
                        .onResult(mCameraAction)
                        .start();
            } else if (id == R.id.album_menu_camera_video) {
                Album.camera(getContext())
                        .video()
                        .requestCode(REQUEST_CODE_CAMERA_VIDEO)
                        .onResult(mCameraAction)
                        .start();
            }
            return true;
        }
    };

    private Action<String> mCameraAction = new Action<String>() {
        @Override
        public void onAction(int requestCode, @NonNull String result) {
            // Add media lib.
            if (mMediaScanner == null) {
                mMediaScanner = new MediaScanner(getContext());
            }
            mMediaScanner.scan(result);

            new PathConvertTask(getContext(), mConvertCallback, mSizeFilter, mMimeFilter, mDurationFilter).execute(result);
        }
    };

    private PathConvertTask.Callback mConvertCallback = new PathConvertTask.Callback() {
        @Override
        public void onConvertCallback(AlbumFile albumFile) {
            albumFile.setChecked(albumFile.isEnable());

            if (albumFile.isEnable()) {
                mCheckedList.add(albumFile);
                setCheckedCountUI(mCheckedList.size());
            }

            if (albumFile.isEnable()) {
                addFileToList(albumFile);
            } else {
                if (mFilterVisibility)
                    addFileToList(albumFile);
                else
                    Toast.makeText(getContext(),
                            getString(R.string.album_take_file_unavailable),
                            Toast.LENGTH_LONG).show();
            }
        }
    };

    private void addFileToList(AlbumFile albumFile) {
        List<AlbumFile> albumFiles = mAlbumFolders.get(0).getAlbumFiles();
        if (albumFiles.size() > 0) {
            albumFiles.add(0, albumFile);

            if (mCurrentFolder == 0) {
                mAlbumContentAdapter.notifyItemInserted(mHasCamera ? 1 : 0);
            } else showAlbumFileFromFolder(0);
        } else {
            albumFiles.add(albumFile);
            showAlbumFileFromFolder(0);
        }

        switch (mChoiceMode) {
            case Album.MODE_SINGLE: {
                AlbumFragment.this.onAlbumResult();
                break;
            }
            case Album.MODE_MULTIPLE: {
                // Nothing.
                break;
            }
        }
    }

    /**
     * When a picture is selected.
     */
    private OnItemCheckedListener mItemCheckListener = new OnItemCheckedListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, int position, boolean isChecked) {
            AlbumFile albumFile = mAlbumFolders.get(mCurrentFolder).getAlbumFiles().get(position);
            albumFile.setChecked(isChecked);

            if (isChecked) {
                if (mCheckedList.size() >= mLimitCount) {
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
                            getResources().getQuantityString(messageRes, mLimitCount, mLimitCount),
                            Toast.LENGTH_LONG).show();

                    buttonView.setChecked(false);
                    albumFile.setChecked(false);
                } else {
                    mCheckedList.add(albumFile);

                    switch (mChoiceMode) {
                        case Album.MODE_SINGLE: {
                            AlbumFragment.this.onAlbumResult();
                            break;
                        }
                        case Album.MODE_MULTIPLE: {
                            // Nothing.
                            break;
                        }
                    }
                }
            } else {
                mCheckedList.remove(albumFile);
            }
            setCheckedCountUI(mCheckedList.size());
        }
    };

    /**
     * Set the number of selected pictures.
     *
     * @param count number.
     */
    private void setCheckedCountUI(int count) {
        mBtnPreview.setText(" (" + count + ")");
        mToolbar.setSubtitle(count + "/" + mLimitCount);
    }

    /**
     * The preview button is clicked.
     */
    private View.OnClickListener mPreviewClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mCheckedList.size() > 0) {
                AlbumPreviewFragment previewFragment = fragment(AlbumPreviewFragment.class, getArguments());
                previewFragment.bindAlbumFiles(mCheckedList, mCheckedList, 0);
                startFragmentForResult(previewFragment, REQUEST_CODE_FRAGMENT_PREVIEW);
            }
        }
    };

    /**
     * Photo album callback selection result.
     */
    private void onAlbumResult() {
        int allSize = mAlbumFolders.get(0).getAlbumFiles().size();
        int checkSize = mCheckedList.size();
        if (allSize > 0 && checkSize == 0) {
            int messageRes;
            switch (mFunction) {
                case Album.FUNCTION_CHOICE_IMAGE: {
                    messageRes = R.string.album_check_image_little;
                    break;
                }
                case Album.FUNCTION_CHOICE_VIDEO: {
                    messageRes = R.string.album_check_video_little;
                    break;
                }
                case Album.FUNCTION_CHOICE_ALBUM:
                default: {
                    messageRes = R.string.album_check_album_little;
                    break;
                }
            }
            Toast.makeText(getContext(), messageRes, Toast.LENGTH_LONG).show();
        } else if (checkSize == 0) {
            setResult(RESULT_CANCELED);
            finish();
        } else {
            setResult(RESULT_OK);
            mCallback.onAlbumResult(mCheckedList);
        }
    }

}
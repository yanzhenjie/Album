/*
 * Copyright 2018 Yan Zhenjie
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
package com.yanzhenjie.album.app;

import android.app.Activity;
import android.content.res.Configuration;
import android.view.View;
import android.widget.CompoundButton;

import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFolder;
import com.yanzhenjie.album.api.widget.Widget;
import com.yanzhenjie.album.app.gallery.PreviewAdapter;
import com.yanzhenjie.album.mvp.BasePresenter;
import com.yanzhenjie.album.mvp.BaseView;

/**
 * Created by YanZhenjie on 2018/4/7.
 */
public final class Contract {

    public interface AlbumPresenter extends BasePresenter {

        /**
         * Click the folder switch.
         */
        void clickFolderSwitch();

        /**
         * Click camera.
         */
        void clickCamera(View v);

        /**
         * Try to check item.
         *
         * @param button   view.
         * @param position position of item.
         */
        void tryCheckItem(CompoundButton button, int position);

        /**
         * Try to preview item.
         *
         * @param position position of item.
         */
        void tryPreviewItem(int position);

        /**
         * Preview the checked items.
         */
        void tryPreviewChecked();

        /**
         * Complete.
         */
        void complete();

    }

    public static abstract class AlbumView extends BaseView<AlbumPresenter> {

        public AlbumView(Activity activity, AlbumPresenter presenter) {
            super(activity, presenter);
        }

        /**
         * Set some properties of the view.
         *
         * @param widget     {@link Widget}.
         * @param column     the count of columns.
         * @param hasCamera  the camera is enabled.
         * @param choiceMode choice mode, one of {@link Album#FUNCTION_CHOICE_ALBUM},
         *                   {@link Album#FUNCTION_CHOICE_IMAGE}
         *                   or {@link Album#FUNCTION_CHOICE_VIDEO}.
         */
        public abstract void setupViews(Widget widget, int column, boolean hasCamera, int choiceMode);

        /**
         * Set the loading visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setLoadingDisplay(boolean display);

        /**
         * Should be re-layout.
         *
         * @param newConfig config.
         */
        public abstract void onConfigurationChanged(Configuration newConfig);

        /**
         * Set the complete menu visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setCompleteDisplay(boolean display);

        /**
         * Bind folder.
         *
         * @param albumFolder {@link AlbumFolder}.
         */
        public abstract void bindAlbumFolder(AlbumFolder albumFolder);

        /**
         * Notify item was inserted.
         *
         * @param position position of item.
         */
        public abstract void notifyInsertItem(int position);

        /**
         * Notify item was changed.
         *
         * @param position position of item.
         */
        public abstract void notifyItem(int position);

        /**
         * Set checked count.
         *
         * @param count the number of items checked.
         */
        public abstract void setCheckedCount(int count);
    }

    public interface NullPresenter extends BasePresenter {
        /**
         * Take a picture.
         */
        void takePicture();

        /**
         * Take a video.
         */
        void takeVideo();
    }

    public static abstract class NullView extends BaseView<NullPresenter> {

        public NullView(Activity activity, NullPresenter presenter) {
            super(activity, presenter);
        }

        /**
         * Set some properties of the view.
         *
         * @param widget {@link Widget}.
         */
        public abstract void setupViews(Widget widget);

        /**
         * Set the message of page.
         *
         * @param message message.
         */
        public abstract void setMessage(int message);

        /**
         * Set the button visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setMakeImageDisplay(boolean display);

        /**
         * Set the button visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setMakeVideoDisplay(boolean display);
    }

    public interface GalleryPresenter extends BasePresenter {
        /**
         * Set the current position of item .
         */
        void onCurrentChanged(int position);

        /**
         * Try to check the current item.
         */
        void onCheckedChanged();

        /**
         * Complete.
         */
        void complete();
    }

    public static abstract class GalleryView<Data> extends BaseView<GalleryPresenter> {

        public GalleryView(Activity activity, GalleryPresenter presenter) {
            super(activity, presenter);
        }

        /**
         * Set some properties of the view.
         *
         * @param widget    {@link Widget}.
         * @param checkable show the checkbox.
         */
        public abstract void setupViews(Widget widget, boolean checkable);

        /**
         * Bind data.
         *
         * @param adapter data adapter.
         */
        public abstract void bindData(PreviewAdapter<Data> adapter);

        /**
         * Set the position of the item to be displayed.
         *
         * @param position position.
         */
        public abstract void setCurrentItem(int position);

        /**
         * Set duration visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setDurationDisplay(boolean display);

        /**
         * Set duration.
         *
         * @param duration duration.
         */
        public abstract void setDuration(String duration);

        /**
         * Changes the checked state of this button.
         *
         * @param checked true to check the button, false to uncheck it.
         */
        public abstract void setChecked(boolean checked);

        /**
         * Set bottom visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setBottomDisplay(boolean display);

        /**
         * Set layer visibility.
         *
         * @param display true is displayed, otherwise it is not displayed.
         */
        public abstract void setLayerDisplay(boolean display);

        /**
         * Set the complete button text.
         *
         * @param text text.
         */
        public abstract void setCompleteText(String text);
    }

}
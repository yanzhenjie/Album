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
package com.yanzhenjie.album.api.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.yanzhenjie.album.R;
import com.yanzhenjie.album.util.AlbumUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YanZhenjie on 2017/8/16.
 */
public class Widget implements Parcelable {

    public static final int STYLE_LIGHT = 1;
    public static final int STYLE_DARK = 2;

    @IntDef({STYLE_DARK, STYLE_LIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UiStyle {
    }

    /**
     * Use when the status bar and the Toolbar are dark.
     */
    public static Builder newDarkBuilder(Context context) {
        return new Builder(context, STYLE_DARK);
    }

    /**
     * Use when the status bar and the Toolbar are light.
     */
    public static Builder newLightBuilder(Context context) {
        return new Builder(context, STYLE_LIGHT);
    }

    private Context mContext;
    private int mUiStyle;
    private int mStatusBarColor;
    private int mToolBarColor;
    private int mNavigationBarColor;
    private String mTitle;
    private ColorStateList mMediaItemCheckSelector;
    private ColorStateList mBucketItemCheckSelector;
    private ButtonStyle mButtonStyle;

    private Widget(Builder builder) {
        this.mContext = builder.mContext;
        this.mUiStyle = builder.mUiStyle;
        this.mStatusBarColor = builder.mStatusBarColor == 0 ? getColor(R.color.albumColorPrimaryDark) : builder.mStatusBarColor;
        this.mToolBarColor = builder.mToolBarColor == 0 ? getColor(R.color.albumColorPrimary) : builder.mToolBarColor;
        this.mNavigationBarColor = builder.mNavigationBarColor == 0 ? getColor(R.color.albumColorPrimaryBlack) : builder.mNavigationBarColor;
        this.mTitle = TextUtils.isEmpty(builder.mTitle) ? mContext.getString(R.string.album_title) : builder.mTitle;
        this.mMediaItemCheckSelector = builder.mMediaItemCheckSelector == null ? AlbumUtils.getColorStateList(getColor(R.color.albumSelectorNormal), getColor(R.color.albumColorPrimary)) : builder.mMediaItemCheckSelector;
        this.mBucketItemCheckSelector = builder.mBucketItemCheckSelector == null ? AlbumUtils.getColorStateList(getColor(R.color.albumSelectorNormal), getColor(R.color.albumColorPrimary)) : builder.mBucketItemCheckSelector;
        this.mButtonStyle = builder.mButtonStyle == null ? ButtonStyle.newDarkBuilder(mContext).build() : builder.mButtonStyle;
    }

    @UiStyle
    public int getUiStyle() {
        return mUiStyle;
    }

    @ColorInt
    public int getStatusBarColor() {
        return mStatusBarColor;
    }

    @ColorInt
    public int getToolBarColor() {
        return mToolBarColor;
    }

    @ColorInt
    public int getNavigationBarColor() {
        return mNavigationBarColor;
    }

    public String getTitle() {
        return mTitle;
    }

    public ColorStateList getMediaItemCheckSelector() {
        return mMediaItemCheckSelector;
    }

    public ColorStateList getBucketItemCheckSelector() {
        return mBucketItemCheckSelector;
    }

    public ButtonStyle getButtonStyle() {
        return mButtonStyle;
    }

    protected Widget(Parcel in) {
        mUiStyle = in.readInt();
        mStatusBarColor = in.readInt();
        mToolBarColor = in.readInt();
        mNavigationBarColor = in.readInt();
        mTitle = in.readString();
        mMediaItemCheckSelector = in.readParcelable(ColorStateList.class.getClassLoader());
        mBucketItemCheckSelector = in.readParcelable(ColorStateList.class.getClassLoader());
        mButtonStyle = in.readParcelable(ButtonStyle.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mUiStyle);
        dest.writeInt(mStatusBarColor);
        dest.writeInt(mToolBarColor);
        dest.writeInt(mNavigationBarColor);
        dest.writeString(mTitle);
        dest.writeParcelable(mMediaItemCheckSelector, flags);
        dest.writeParcelable(mBucketItemCheckSelector, flags);
        dest.writeParcelable(mButtonStyle, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Widget> CREATOR = new Creator<Widget>() {
        @Override
        public Widget createFromParcel(Parcel in) {
            return new Widget(in);
        }

        @Override
        public Widget[] newArray(int size) {
            return new Widget[size];
        }
    };

    private int getColor(int colorId) {
        return ContextCompat.getColor(mContext, colorId);
    }

    public static class Builder {

        private Context mContext;
        private int mUiStyle;
        private int mStatusBarColor;
        private int mToolBarColor;
        private int mNavigationBarColor;
        private String mTitle;
        private ColorStateList mMediaItemCheckSelector;
        private ColorStateList mBucketItemCheckSelector;
        private ButtonStyle mButtonStyle;

        private Builder(Context context, @UiStyle int style) {
            this.mContext = context;
            this.mUiStyle = style;
        }

        /**
         * Status bar color.
         */
        public Builder statusBarColor(@ColorInt int color) {
            this.mStatusBarColor = color;
            return this;
        }

        /**
         * Toolbar color.
         */
        public Builder toolBarColor(@ColorInt int color) {
            this.mToolBarColor = color;
            return this;
        }

        /**
         * Virtual navigation bar.
         */
        public Builder navigationBarColor(@ColorInt int color) {
            this.mNavigationBarColor = color;
            return this;
        }

        /**
         * Set the title of the Toolbar.
         */
        public Builder title(@StringRes int title) {
            return title(mContext.getString(title));
        }

        /**
         * Set the title of the Toolbar.
         */
        public Builder title(String title) {
            this.mTitle = title;
            return this;
        }

        /**
         * The color of the {@code Media Item} selector.
         */
        public Builder mediaItemCheckSelector(@ColorInt int normalColor, @ColorInt int highLightColor) {
            this.mMediaItemCheckSelector = AlbumUtils.getColorStateList(normalColor, highLightColor);
            return this;
        }

        /**
         * The color of the {@code Bucket Item} selector.
         */
        public Builder bucketItemCheckSelector(@ColorInt int normalColor, @ColorInt int highLightColor) {
            this.mBucketItemCheckSelector = AlbumUtils.getColorStateList(normalColor, highLightColor);
            return this;
        }

        /**
         * Set the style of the Button.
         */
        public Builder buttonStyle(ButtonStyle buttonStyle) {
            this.mButtonStyle = buttonStyle;
            return this;
        }

        /**
         * Create target.
         */
        public Widget build() {
            return new Widget(this);
        }
    }

    public static class ButtonStyle implements Parcelable {

        /**
         * Use when the Button are dark.
         */
        public static Builder newDarkBuilder(Context context) {
            return new Builder(context, STYLE_DARK);
        }

        /**
         * Use when the Button are light.
         */
        public static Builder newLightBuilder(Context context) {
            return new Builder(context, STYLE_LIGHT);
        }

        private Context mContext;
        private int mUiStyle;
        private ColorStateList mButtonSelector;

        private ButtonStyle(Builder builder) {
            this.mContext = builder.mContext;
            this.mUiStyle = builder.mUiStyle;
            this.mButtonSelector = builder.mButtonSelector == null ?
                    AlbumUtils.getColorStateList(ContextCompat.getColor(mContext, R.color.albumColorPrimary),
                            ContextCompat.getColor(mContext, R.color.albumColorPrimaryDark)
                    ) :
                    builder.mButtonSelector;
        }

        public int getUiStyle() {
            return mUiStyle;
        }

        public ColorStateList getButtonSelector() {
            return mButtonSelector;
        }

        protected ButtonStyle(Parcel in) {
            mUiStyle = in.readInt();
            mButtonSelector = in.readParcelable(ColorStateList.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mUiStyle);
            dest.writeParcelable(mButtonSelector, flags);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ButtonStyle> CREATOR = new Creator<ButtonStyle>() {
            @Override
            public ButtonStyle createFromParcel(Parcel in) {
                return new ButtonStyle(in);
            }

            @Override
            public ButtonStyle[] newArray(int size) {
                return new ButtonStyle[size];
            }
        };

        public static class Builder {

            private Context mContext;
            private int mUiStyle;
            private ColorStateList mButtonSelector;

            private Builder(Context context, @UiStyle int style) {
                this.mContext = context;
                this.mUiStyle = style;
            }

            /**
             * Set button click effect.
             *
             * @param normalColor    normal color.
             * @param highLightColor feedback color.
             */
            public Builder setButtonSelector(@ColorInt int normalColor, @ColorInt int highLightColor) {
                mButtonSelector = AlbumUtils.getColorStateList(normalColor, highLightColor);
                return this;
            }

            public ButtonStyle build() {
                return new ButtonStyle(this);
            }
        }
    }

    /**
     * Create default widget.
     */
    public static Widget getDefaultWidget(Context context) {
        return Widget.newDarkBuilder(context)
                .statusBarColor(ContextCompat.getColor(context, R.color.albumColorPrimaryDark))
                .toolBarColor(ContextCompat.getColor(context, R.color.albumColorPrimary))
                .navigationBarColor(ContextCompat.getColor(context, R.color.albumColorPrimaryBlack))
                .title(R.string.album_title)
                .mediaItemCheckSelector(ContextCompat.getColor(context, R.color.albumSelectorNormal),
                        ContextCompat.getColor(context, R.color.albumColorPrimary))
                .bucketItemCheckSelector(ContextCompat.getColor(context, R.color.albumSelectorNormal),
                        ContextCompat.getColor(context, R.color.albumColorPrimary))
                .buttonStyle(
                        ButtonStyle.newDarkBuilder(context)
                                .setButtonSelector(ContextCompat.getColor(context, R.color.albumColorPrimary),
                                        ContextCompat.getColor(context, R.color.albumColorPrimaryDark))
                                .build()
                )
                .build();
    }
}
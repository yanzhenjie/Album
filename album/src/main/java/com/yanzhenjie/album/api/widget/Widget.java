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
package com.yanzhenjie.album.api.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
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

    @IntDef({STYLE_DARK, STYLE_LIGHT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStyle {
    }

    private Context mContext;
    @UIStyle
    private int mStyle;
    @ColorInt
    private int mStatusBarColor;
    @ColorInt
    private int mToolBarColor;
    @ColorInt
    private int mNavigationBarColor;
    private String mTitle;
    private ColorStateList mMediaItemCheckSelector;
    private ColorStateList mBucketItemCheckSelector;
    private ButtonStyle mButtonStyle;

    private Widget(Builder builder) {
        this.mContext = builder.mContext;
        this.mStyle = builder.mStyle;
        this.mStatusBarColor = builder.mStatusBarColor == 0 ?
                ContextCompat.getColor(mContext, R.color.album_ColorPrimaryDark) : builder.mStatusBarColor;
        this.mToolBarColor = builder.mToolBarColor == 0 ?
                ContextCompat.getColor(mContext, R.color.album_ColorPrimary) : builder.mToolBarColor;
        this.mNavigationBarColor = builder.mNavigationBarColor == 0 ?
                ContextCompat.getColor(mContext, R.color.album_ColorPrimaryBlack) : builder.mNavigationBarColor;
        this.mTitle = TextUtils.isEmpty(builder.mTitle) ? mContext.getString(R.string.album_title) : builder.mTitle;
        this.mMediaItemCheckSelector = builder.mMediaItemCheckSelector == null ?
                AlbumUtils.getColorStateList(ContextCompat.getColor(mContext, R.color.album_WhiteGray),
                        ContextCompat.getColor(mContext, R.color.album_ColorPrimary)
                ) :
                builder.mMediaItemCheckSelector;
        this.mBucketItemCheckSelector = builder.mBucketItemCheckSelector == null ?
                AlbumUtils.getColorStateList(ContextCompat.getColor(mContext, R.color.album_WhiteGray),
                        ContextCompat.getColor(mContext, R.color.album_ColorPrimary)
                ) :
                builder.mBucketItemCheckSelector;
        mButtonStyle = builder.mButtonStyle == null ? ButtonStyle.newDarkBuilder(mContext).build() : builder.mButtonStyle;
    }

    @UIStyle
    public int getStyle() {
        return mStyle;
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
        //noinspection WrongConstant
        mStyle = in.readInt();
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
        dest.writeInt(mStyle);
        dest.writeInt(mStatusBarColor);
        dest.writeInt(mToolBarColor);
        dest.writeInt(mNavigationBarColor);
        dest.writeString(mTitle.toString());
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

    public static class Builder {

        private Context mContext;
        @UIStyle
        private int mStyle;
        @ColorInt
        private int mStatusBarColor;
        @ColorInt
        private int mToolBarColor;
        @ColorInt
        private int mNavigationBarColor;
        private String mTitle;
        private ColorStateList mMediaItemCheckSelector;
        private ColorStateList mBucketItemCheckSelector;
        private ButtonStyle mButtonStyle;

        private Builder(Context context, @UIStyle int style) {
            this.mContext = context;
            this.mStyle = style;
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
        public Builder buttonStyle(@NonNull ButtonStyle buttonStyle) {
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
        public static Builder newDarkBuilder(@NonNull Context context) {
            return new Builder(context, STYLE_DARK);
        }

        /**
         * Use when the Button are light.
         */
        public static Builder newLightBuilder(@NonNull Context context) {
            return new Builder(context, STYLE_LIGHT);
        }

        private Context mContext;
        @UIStyle
        private int mButtonStyle;
        private ColorStateList mButtonSelector;

        private ButtonStyle(Builder builder) {
            this.mContext = builder.mContext;
            this.mButtonStyle = builder.mButtonStyle;
            this.mButtonSelector = builder.mButtonSelector == null ?
                    AlbumUtils.getColorStateList(ContextCompat.getColor(mContext, R.color.album_ColorPrimary),
                            ContextCompat.getColor(mContext, R.color.album_ColorPrimaryDark)
                    ) :
                    builder.mButtonSelector;
        }

        public int getButtonStyle() {
            return mButtonStyle;
        }

        public ColorStateList getButtonSelector() {
            return mButtonSelector;
        }

        protected ButtonStyle(Parcel in) {
            //noinspection WrongConstant
            mButtonStyle = in.readInt();
            mButtonSelector = in.readParcelable(ColorStateList.class.getClassLoader());
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mButtonStyle);
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
            private int mButtonStyle;
            private ColorStateList mButtonSelector;

            private Builder(@NonNull Context context, @UIStyle int style) {
                this.mContext = context;
                this.mButtonStyle = style;
            }

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
                .statusBarColor(ContextCompat.getColor(context, R.color.album_ColorPrimaryDark))
                .toolBarColor(ContextCompat.getColor(context, R.color.album_ColorPrimary))
                .navigationBarColor(ContextCompat.getColor(context, R.color.album_ColorPrimaryBlack))
                .title(R.string.album_title)
                .mediaItemCheckSelector(ContextCompat.getColor(context, R.color.album_WhiteGray),
                        ContextCompat.getColor(context, R.color.album_ColorPrimary))
                .bucketItemCheckSelector(ContextCompat.getColor(context, R.color.album_WhiteGray),
                        ContextCompat.getColor(context, R.color.album_ColorPrimary))
                .buttonStyle(
                        ButtonStyle.newDarkBuilder(context)
                                .setButtonSelector(ContextCompat.getColor(context, R.color.album_ColorPrimary),
                                        ContextCompat.getColor(context, R.color.album_ColorPrimaryDark))
                                .build()
                )
                .build();
    }
}
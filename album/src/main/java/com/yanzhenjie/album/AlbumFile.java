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
package com.yanzhenjie.album;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YanZhenjie on 2017/8/15.
 */
public class AlbumFile implements Parcelable, Comparable<AlbumFile> {

    public static final int TYPE_INVALID = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_INVALID, TYPE_IMAGE, TYPE_VIDEO})
    public @interface MediaType {
    }

    /**
     * File path.
     */
    private String mPath;
    /**
     * File name.
     */
    private String mName;
    /**
     * Title.
     */
    private String mTitle;
    /**
     * Folder id.
     */
    private int mBucketId;
    /**
     * Folder mName.
     */
    private String mBucketName;
    /**
     * File mime type.
     */
    private String mMimeType;
    /**
     * Add date.
     */
    private long mAddDate;
    /**
     * Modify date.
     */
    private long mModifyDate;
    /**
     * Latitude
     */
    private float mLatitude;
    /**
     * Longitude.
     */
    private float mLongitude;
    /**
     * Size.
     */
    private long mSize;
    /**
     * Duration.
     */
    private long mDuration;
    /**
     * Resolution.
     */
    private String mResolution;
    /**
     * Thumb path.
     */
    private String mThumbPath;
    /**
     * UI width.
     */
    private int mWidth;
    /**
     * UI height.
     */
    private int mHeight;
    /**
     * MediaType;
     */
    private int mMediaType;
    /**
     * Checked.
     */
    private boolean isChecked;
    /**
     * Enabled.
     */
    private boolean isEnable = true;

    public AlbumFile() {
    }

    @Override
    public int compareTo(AlbumFile o) {
        long time = o.getAddDate() - getAddDate();
        if (time > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        else if (time < -Integer.MAX_VALUE)
            return -Integer.MAX_VALUE;
        return (int) time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AlbumFile) {
            AlbumFile o = (AlbumFile) obj;
            if (!TextUtils.isEmpty(mPath) && !TextUtils.isEmpty(o.getPath())) {
                return mPath.equals(o.getPath());
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        String key = mBucketId + mPath;
        return key.hashCode();
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public int getBucketId() {
        return mBucketId;
    }

    public void setBucketId(int bucketId) {
        mBucketId = bucketId;
    }

    public String getBucketName() {
        return mBucketName;
    }

    public void setBucketName(String bucketName) {
        mBucketName = bucketName;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public void setMimeType(String mimeType) {
        mMimeType = mimeType;
    }

    public long getAddDate() {
        return mAddDate;
    }

    public void setAddDate(long addDate) {
        mAddDate = addDate;
    }

    public long getModifyDate() {
        return mModifyDate;
    }

    public void setModifyDate(long modifyDate) {
        mModifyDate = modifyDate;
    }

    public float getLatitude() {
        return mLatitude;
    }

    public void setLatitude(float latitude) {
        mLatitude = latitude;
    }

    public float getLongitude() {
        return mLongitude;
    }

    public void setLongitude(float longitude) {
        mLongitude = longitude;
    }

    public long getSize() {
        return mSize;
    }

    public void setSize(long size) {
        mSize = size;
    }

    public long getDuration() {
        return mDuration;
    }

    public void setDuration(long duration) {
        mDuration = duration;
    }

    public String getResolution() {
        return mResolution;
    }

    public void setResolution(String resolution) {
        mResolution = resolution;
    }

    public String getThumbPath() {
        return mThumbPath;
    }

    public void setThumbPath(String thumbPath) {
        mThumbPath = thumbPath;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    @MediaType
    public int getMediaType() {
        return mMediaType;
    }

    public void setMediaType(@MediaType int mediaType) {
        mMediaType = mediaType;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    protected AlbumFile(Parcel in) {
        mPath = in.readString();
        mName = in.readString();
        mTitle = in.readString();
        mBucketId = in.readInt();
        mBucketName = in.readString();
        mMimeType = in.readString();
        mAddDate = in.readLong();
        mModifyDate = in.readLong();
        mLatitude = in.readFloat();
        mLongitude = in.readFloat();
        mSize = in.readLong();
        mDuration = in.readLong();
        mResolution = in.readString();
        mThumbPath = in.readString();
        mWidth = in.readInt();
        mHeight = in.readInt();
        mMediaType = in.readInt();
        isChecked = in.readByte() != 0;
        isEnable = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeString(mName);
        dest.writeString(mTitle);
        dest.writeInt(mBucketId);
        dest.writeString(mBucketName);
        dest.writeString(mMimeType);
        dest.writeLong(mAddDate);
        dest.writeLong(mModifyDate);
        dest.writeFloat(mLatitude);
        dest.writeFloat(mLongitude);
        dest.writeLong(mSize);
        dest.writeLong(mDuration);
        dest.writeString(mResolution);
        dest.writeString(mThumbPath);
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        dest.writeInt(mMediaType);
        dest.writeByte((byte) (isChecked ? 1 : 0));
        dest.writeByte((byte) (isEnable ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumFile> CREATOR = new Creator<AlbumFile>() {
        @Override
        public AlbumFile createFromParcel(Parcel in) {
            return new AlbumFile(in);
        }

        @Override
        public AlbumFile[] newArray(int size) {
            return new AlbumFile[size];
        }
    };
}

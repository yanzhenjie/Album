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
package com.yanzhenjie.album;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

/**
 * Created by YanZhenjie on 2017/8/15.
 */
public class AlbumFile implements Parcelable, Comparable<AlbumFile> {

    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_IMAGE, TYPE_VIDEO})
    public @interface MediaType {
    }

    /**
     * File uri.
     */
    private Uri uri;
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
     * Thumb path uri.
     */
    private Uri mThumbUri;
    /**
     * MediaType.
     */
    private int mMediaType;
    /**
     * Checked.
     */
    private boolean isChecked;
    /**
     * Enabled.
     */
    private boolean isDisable;

    public AlbumFile() {
    }

    @Override
    public int compareTo(AlbumFile o) {
        long time = o.getAddDate() - getAddDate();
        if (time > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (time < -Integer.MAX_VALUE) {
            return -Integer.MAX_VALUE;
        }
        return (int) time;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof AlbumFile) {
            AlbumFile o = (AlbumFile) obj;
            Uri inPath = o.getUri();
            if (uri != null && inPath != null) {
                return uri.equals(inPath);
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return uri != null ? uri.hashCode() : super.hashCode();
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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

    public Uri getThumbUri() {
        return mThumbUri;
    }

    public void setThumbUri(Uri thumbUri) {
        mThumbUri = thumbUri;
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

    public boolean isDisable() {
        return isDisable;
    }

    public void setDisable(boolean disable) {
        this.isDisable = disable;
    }

    @Override
    public String toString() {
        return "AlbumFile{" +
            "uri=" + uri +
            ", mBucketName='" + mBucketName + '\'' +
            ", mMimeType='" + mMimeType + '\'' +
            ", mAddDate=" + mAddDate +
            ", mLatitude=" + mLatitude +
            ", mLongitude=" + mLongitude +
            ", mSize=" + mSize +
            ", mDuration=" + mDuration +
            ", mThumbUri='" + mThumbUri + '\'' +
            ", mMediaType=" + mMediaType +
            ", isChecked=" + isChecked +
            ", isDisable=" + isDisable +
            '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.mBucketName);
        dest.writeString(this.mMimeType);
        dest.writeLong(this.mAddDate);
        dest.writeFloat(this.mLatitude);
        dest.writeFloat(this.mLongitude);
        dest.writeLong(this.mSize);
        dest.writeLong(this.mDuration);
        dest.writeParcelable(this.mThumbUri, flags);
        dest.writeInt(this.mMediaType);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDisable ? (byte) 1 : (byte) 0);
    }

    protected AlbumFile(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.mBucketName = in.readString();
        this.mMimeType = in.readString();
        this.mAddDate = in.readLong();
        this.mLatitude = in.readFloat();
        this.mLongitude = in.readFloat();
        this.mSize = in.readLong();
        this.mDuration = in.readLong();
        this.mThumbUri = in.readParcelable(Uri.class.getClassLoader());
        this.mMediaType = in.readInt();
        this.isChecked = in.readByte() != 0;
        this.isDisable = in.readByte() != 0;
    }

    public static final Creator<AlbumFile> CREATOR = new Creator<AlbumFile>() {
        @Override
        public AlbumFile createFromParcel(Parcel source) {
            return new AlbumFile(source);
        }

        @Override
        public AlbumFile[] newArray(int size) {
            return new AlbumFile[size];
        }
    };
}
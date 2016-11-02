/*
 * AUTHOR：Yan Zhenjie
 *
 * DESCRIPTION：create the File, and add the content.
 *
 * Copyright © ZhiMore. All Rights Reserved
 *
 */
package com.yanzhenjie.album.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Yan Zhenjie on 2016/10/14.
 */
public class AlbumImage implements Parcelable, Comparable<AlbumImage> {

    private int id;
    /**
     * 图片路径。
     */
    private String path;
    /**
     * 图片名称。
     */
    private String name;
    /**
     * 被添加到库中的时间。
     */
    private long addTime;
    /**
     * 是否选中。
     */
    private boolean isChecked;

    public AlbumImage() {
        super();
    }

    public AlbumImage(int id, String path, String name, long addTime, boolean isChecked) {
        this.id = id;
        this.path = path;
        this.name = name;
        this.addTime = addTime;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(addTime);
        dest.writeInt(isChecked ? 1 : 0);
    }

    public static final Creator<AlbumImage> CREATOR = new Creator<AlbumImage>() {
        public AlbumImage createFromParcel(Parcel in) {
            return new AlbumImage(in);
        }

        public AlbumImage[] newArray(int size) {
            return new AlbumImage[size];
        }
    };

    public AlbumImage(Parcel in) {
        AlbumImage photo = new AlbumImage();
        photo.id = in.readInt();
        photo.path = in.readString();
        photo.name = in.readString();
        photo.addTime = in.readLong();
        photo.isChecked = in.readInt() == 1;
    }

    @Override
    public int compareTo(AlbumImage o) {
        long time = o.getAddTime() - getAddTime();
        if (time > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        else if (time < -Integer.MAX_VALUE)
            return -Integer.MAX_VALUE;
        return (int) time;
    }
}

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

import java.util.ArrayList;

/**
 * Created by Yan Zhenjie on 2016/10/14.
 */
public class AlbumFolder implements Parcelable {

    private int id;
    /**
     * 文件夹名称
     */
    private String name;
    /**
     * 所有图片
     */
    private ArrayList<AlbumImage> photos = new ArrayList<>();
    /**
     * 文件夹是否被选中。
     */
    private boolean isChecked;

    public AlbumFolder() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AlbumImage> getPhotos() {
        return photos;
    }

    public void addPhoto(AlbumImage photo) {
        this.photos.add(photo);
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
        dest.writeString(name);
        dest.writeList(photos);
        dest.writeInt(isChecked ? 1 : 0);
    }

    public static final Creator<AlbumFolder> CREATOR = new Creator<AlbumFolder>() {
        public AlbumFolder createFromParcel(Parcel in) {
            return new AlbumFolder(in);
        }

        public AlbumFolder[] newArray(int size) {
            return new AlbumFolder[size];
        }
    };

    private AlbumFolder(Parcel in) {
        id = in.readInt();
        name = in.readString();
        photos = in.readArrayList(AlbumImage.class.getClassLoader());
        isChecked = in.readInt() == 1;
    }

}

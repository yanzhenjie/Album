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
package com.yanzhenjie.album.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * <p>Album folder, contains selected status and pictures.</p>
 * Created by Yan Zhenjie on 2016/10/14.
 */
public class AlbumFolder implements Parcelable {

    private int id;
    /**
     * Folder name.
     */
    private String name;
    /**
     * Image list in folder.
     */
    private ArrayList<AlbumImage> images = new ArrayList<>();
    /**
     * checked.
     */
    private boolean isChecked;

    public AlbumFolder() {
        super();
    }

    protected AlbumFolder(Parcel in) {
        id = in.readInt();
        name = in.readString();
        images = in.createTypedArrayList(AlbumImage.CREATOR);
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeTypedList(images);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumFolder> CREATOR = new Creator<AlbumFolder>() {
        @Override
        public AlbumFolder createFromParcel(Parcel in) {
            return new AlbumFolder(in);
        }

        @Override
        public AlbumFolder[] newArray(int size) {
            return new AlbumFolder[size];
        }
    };

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

    public ArrayList<AlbumImage> getImages() {
        return images;
    }

    public void addPhoto(AlbumImage photo) {
        this.images.add(photo);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}

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

/**
 * <p>Image, including time, path and selected status.</p>
 * Created by Yan Zhenjie on 2016/10/14.
 */
public class AlbumImage implements Parcelable, Comparable<AlbumImage> {

    private int id;
    /**
     * Image path.
     */
    private String path;
    /**
     * Image name.
     */
    private String name;
    /**
     * The time to be added to the library.
     */
    private long addTime;
    /**
     * Checked.
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

    protected AlbumImage(Parcel in) {
        id = in.readInt();
        path = in.readString();
        name = in.readString();
        addTime = in.readLong();
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeLong(addTime);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumImage> CREATOR = new Creator<AlbumImage>() {
        @Override
        public AlbumImage createFromParcel(Parcel in) {
            return new AlbumImage(in);
        }

        @Override
        public AlbumImage[] newArray(int size) {
            return new AlbumImage[size];
        }
    };

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
    public int compareTo(AlbumImage o) {
        long time = o.getAddTime() - getAddTime();
        if (time > Integer.MAX_VALUE)
            return Integer.MAX_VALUE;
        else if (time < -Integer.MAX_VALUE)
            return -Integer.MAX_VALUE;
        return (int) time;
    }
}

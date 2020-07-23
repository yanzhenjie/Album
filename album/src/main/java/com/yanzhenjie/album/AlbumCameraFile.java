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

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.yanzhenjie.album.util.AlbumUtils;

/**
 * Created by cc on 2020/7/22.
 */
public class AlbumCameraFile implements Parcelable {
    
    public AlbumCameraFile(Uri uri, String path) {
        this.uri = uri;
        this.path = path;
    }
    
    public static AlbumCameraFile getAlbumCameraFile(Context context, String filePath) {
        return new AlbumCameraFile(AlbumUtils.getUri(context, filePath), filePath);
    }
    
    /**
     * File uri.
     */
    private Uri uri;
    /**
     * File path.
     */
    private String path;
    
    public Uri getUri() {
        return uri;
    }
    
    public void setUri(Uri uri) {
        this.uri = uri;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.path);
    }
    
    public AlbumCameraFile() {
    }
    
    protected AlbumCameraFile(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.path = in.readString();
    }
    
    
    public static final Parcelable.Creator<AlbumCameraFile> CREATOR = new Parcelable.Creator<AlbumCameraFile>() {
        @Override
        public AlbumCameraFile createFromParcel(Parcel source) {
            return new AlbumCameraFile(source);
        }
        
        @Override
        public AlbumCameraFile[] newArray(int size) {
            return new AlbumCameraFile[size];
        }
    };
    
    @Override
    public String toString() {
        return "AlbumCameraFile{" +
            "uri=" + uri +
            ", path='" + path + '\'' +
            '}';
    }
}
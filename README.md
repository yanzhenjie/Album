# Album
`Album` is an MaterialDesign tyle open source album, the main function is: `Album and Gallery`.

Image upload recommended NoHttp：[NoHttp Open source address](https://github.com/yanzhenjie/NoHttp).

[中文文档](./README-CN.md)

# Features
1. Support component：`Activity`、`Fragment`.
2. UI style can be configured, for example: `Toolbar`、`StatusBar`、`NavigationBar`.
3. Radio, multi-select, folder preview, gallery, gallery zoom.
4. Support setting album number.
5. Support for configuring whether to use the camera.
6. Gallery preview multiple pictures, preview can be anti-election.

# Screenshot
Please experience [download apk](https://github.com/yanzhenjie/Album/blob/master/sample-release.apk?raw=true).  
<image src="./image/1.gif" width="170px"/> <image src="./image/2.gif" width="170px"/> <image src="./image/3.gif" width="170px"/> <image src="./image/4.gif" width="170px"/>

# Dependencies
* Gradle：
```groovy
compile 'com.yanzhenjie:album:1.0.1'
```

* Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>album</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```

# Register in mainifest.xml
```xml
<activity
    android:name="com.yanzhenjie.album.AlbumActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    android:windowSoftInputMode="stateAlwaysHidden|stateHidden" />
```

# Permission
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

* Developers do not need to worry about `Android6.0` runtime permissions，`Album` has been very well handled.
* In addition `Android6.0` runtime permissions recommended: [AndPermission](https://github.com/yanzhenjie/AndPermission).

# Usage
Album's main function is: `Album and Gallery`, the following are described separately.

## Album
Use `Album.album(this).start()` to call up the album.
```java
Album.album(this)
    .requestCode(999) // Request code.
    .toolBarColor(toolbarColor) // Toolbar color.
    .statusBarColor(statusBarColor) // StatusBar color.
    .navigationBarColor(navigationBarColor) // NavigationBar color.
    
    .selectCount(9) // Choose up to a few pictures.
    .columnCount(2) // Number of albums.
    .camera(true) // Have a camera function.
    .checkedList(mImageList) // Has selected the picture, automatically select.
    .start();
```

Accept the result：  
```java
ArrayList<String> mImageList;

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 999) {
        if (resultCode == RESULT_OK) { // Successfully.
            // Parse select result.
            mImageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            // User canceled.
        }
    }
}
```

## Gallery
Use the `Album.album(this).start ()` to call up the gallery.
```java
Album.gallery(this)
    .requestCode(999) // Request code.
    .toolBarColor(toolbarColor) // Toolbar color.
    .statusBarColor(statusBarColor) // StatusBar color.
    .navigationBarColor(navigationBarColor) // NavigationBar color.
    
    .checkedList(mImageList) // List of pictures to preview.
    .currentPosition(position) // First display position image of the list.
    .checkFunction(true) // Anti-election function.
    .start();
```

**Note:**

* Be sure to pass in the collection of pictures you want to preview, otherwise it will return immediately after the start.
* It is recommended to call the gallery preview to judge `if(currentPosition < mImageList.size())`, to ensure that the entry of the `position` in the `list`.

If you need to have an anti-selection function at the time of preview，override the `onActivityResult ()` method, after receiving the anti-selected picture `List` result:  
```java
ArrayList<String> mImageList;

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 666) {
        if (resultCode == RESULT_OK) { // Successfully.
            // Parse select result.
            mImageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            // User canceled.
        }
    }
}
```

# Proguard-rules
If there is a problem, add the rule to the proguard-rules:
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```

# Thanks
1. [PhotoView](https://github.com/chrisbanes/PhotoView)
2. [LoadingDrawable](https://github.com/dinuscxj/LoadingDrawable)

# License
```text
Copyright 2017 Yan Zhenjie

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

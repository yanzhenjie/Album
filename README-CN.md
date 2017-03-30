# Album
`Album`是一个MD风格的开源相册，主要功能分为两部分：相册选图、画廊预览。

技术交流群：[46523908](http://jq.qq.com/?_wv=1027&k=410oIg0)  
图片上传推荐使用NoHttp：[NoHttp开源地址](https://github.com/yanzhenjie/NoHttp)、[NoHttp使用文档](http://doc.nohttp.net)。

我的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
我的博客：[http://blog.yanzhenjie.com](http://blog.yanzhenjie.com)  

# 特性
1. 支持组件：`Activity`、`Fragment`。
2. UI风格可以配置，比如：`Toolbar`、`StatusBar`、`NavigationBar`。
3. 单选、多选、文件夹预览、画廊、画廊缩放。
4. 支持配置相册展示时的列数。
5. 支持配置是否使用相机。
6. 画廊预览选择的图片，预览时可以反选。

# 效果预览
体验请[下载demo的apk](https://github.com/yanzhenjie/Album/blob/master/sample-release.apk?raw=true)。  
<image src="./image/1.gif" width="170px"/> <image src="./image/2.gif" width="170px"/> <image src="./image/3.gif" width="170px"/> <image src="./image/4.gif" width="170px"/>

# 依赖
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
* Eclipse 请放弃治疗。

# mainifest.xml中需要注册
```xml
<activity
    android:name="com.yanzhenjie.album.AlbumActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    android:windowSoftInputMode="stateAlwaysHidden|stateHidden" />
```

* 注意：1.0.0以上版本给这个activity配置的`android:title=""`属性会被自动忽略。

# 权限
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

* 开发者不需要担心`Android6.0`运行时权限，`Album`已经非常完善的处理过了。  
* 另外`Android6.0`运行时权限推荐使用：[AndPermission](https://github.com/yanzhenjie/AndPermission)，如果不使用6.0特性，建议把`targetSdkVersion`值设置的小于23，这样就不会使用`Android6.0`运行时权限了。

# 使用教程
`Album`主要功能分为两部分：相册选图、画廊预览，下面分别说明。

## Album相册
使用`Album.album(this).start()`即可调起相册。
```java
Album.album(this)
    .requestCode(999) // 请求码，返回时onActivityResult()的第一个参数。
    .toolBarColor(toolbarColor) // Toolbar 颜色，默认蓝色。
    .statusBarColor(statusBarColor) // StatusBar 颜色，默认蓝色。
    .navigationBarColor(navigationBarColor) // NavigationBar 颜色，默认黑色，建议使用默认。
    
    .selectCount(9) // 最多选择几张图片。
    .columnCount(2) // 相册展示列数，默认是2列。
    .camera(true) // 是否有拍照功能。
    .checkedList(mImageList) // 已经选择过得图片，相册会自动选中选过的图片，并计数。
    .start();
```

重写`onActivityResult()`方法，接受图片选择结果：  
```java
ArrayList<String> mImageList;

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 999) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 不要质疑你的眼睛，就是这么简单。
            mImageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // User canceled.
            // 用户取消了操作。
        }
    }
}
```

## Galleryhuala
使用`Album.album(this).start()`即可调起画廊，画廊只支持预览本地图片，你只需要传入一个图片集合：  
```java
Album.gallery(this)
    .requestCode(666) // 请求码，返回时onActivityResult()的第一个参数。
    .toolBarColor(toolbarColor) // Toolbar 颜色，默认蓝色。
    .statusBarColor(statusBarColor) // StatusBar 颜色，默认蓝色。
    .navigationBarColor(navigationBarColor) // NavigationBar 颜色，默认黑色，建议使用默认。
    
    .checkedList(mImageList) // 要预览的图片list。
    .currentPosition(position) // 预览的时候要显示list中的图片的index。
    .checkFunction(true) // 预览时是否有反选功能。
    .start();
```
**注意：**

* 一定要传入要预览的图片集合，否则启动会立即返回。
* 调用画廊预览时判断`if(currentPosition < mImageList.size())`，这样才能保证传入的`position`在`list`中，否则会立即返回。

如果你需要预览时的反选功能，那么重写`onActivityResult()`方法，接受反选后的图片`List`结果：  
```java
ArrayList<String> mImageList;

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 666) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 不要再次质疑你的眼睛，还是这么简单。
            mImageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // User canceled.
            // 用户取消了操作。
        }
    }
}
```

# 混淆
`Album`是完全可以混淆的，如果混淆后相册出现了问题，请在混淆规则中添加：  
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```

# Thanks
1. [PhotoView](https://github.com/chrisbanes/PhotoView)  
2. [LoadingDrawable](https://github.com/dinuscxj/LoadingDrawable)  

在此特别感谢上述项目及作者。  

**关注我的微信公众号**  
![公众号](./image/wechat.jpg)

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

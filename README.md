# 一个简约的Android相册（支持多选）
技术交流群：[46523908](http://jq.qq.com/?_wv=1027&k=410oIg0)  
图片上传推荐使用NoHttp：[NoHttp源码](https://github.com/yanzhenjie/NoHttp)、[NoHttp详细使用文档](http://doc.nohttp.net)

本项目开源地址：[https://github.com/yanzhenjie/album](https://github.com/yanzhenjie/album)

1. Album是一个Android开源相册，支持单/多选、支持预览、支持选择文件夹查看图片，后期会加入图片剪切、放大缩小等操作。

2. 开发者不需要担心`Android6.0`的运行时权限，`Album`已经非常完善的处理过了。

3. 支持自定义样式风格，比如Toolbar颜色、状态栏颜色等。

4. 内置支持了相机，开发者不用担心相机的使用问题，Album自动搞定。

5. 支持`Activity`、`Fragment`调用。

## Demo效果预览
<image src="https://github.com/yanzhenjie/album/blob/master/image/1.gif?raw=true" width="250px"/>  <image src="https://github.com/yanzhenjie/album/blob/master/image/2.gif?raw=true" width="250px"/>  
<image src="https://github.com/yanzhenjie/album/blob/master/image/3.gif?raw=true" width="250px"/>  <image src="https://github.com/yanzhenjie/album/blob/master/image/4.gif?raw=true" width="250px"/>  

如果你想体验一把，你可以[下载demo的apk](https://github.com/yanzhenjie/album/blob/master/sample-release.apk?raw=true)来玩玩。

## 使用方法
Gradle：
```groovy
compile 'com.yanzhenjie:album:1.0.0'
```
Or Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>album</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
Eclipse请下载源码自行转换成Library project。

## mainifest.xml中需要注册
```xml
<activity
    android:name="com.yanzhenjie.album.AlbumActivity"
    android:label="图库"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar" />
```
其中`android:label="xx"`中的xx是调起的`Activity`的标题，你可以自定义，其它请照抄即可。

## 需要的权限
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```
开发者不需要担心`Android6.0`的运行时权限，`Album`已经非常完善的处理过了。

## 如何调用
调起Album的界面：
```java
// 1. 使用默认风格，并指定选择数量：
// 第一个参数Activity/Fragment； 第二个request_code； 第三个允许选择照片的数量，不填可以无限选择。
// Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO, 9);

// 2. 使用默认风格，不指定选择数量：
// Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO); // 第三个参数不填的话，可以选择无数个。

// 3. 指定风格，并指定选择数量，如果不想限制数量传入Integer.MAX_VALUE;
Album.startAlbum(this, ACTIVITY_REQUEST_SELECT_PHOTO
    , 9                                                         // 指定选择数量。
    , ContextCompat.getColor(this, R.color.colorPrimary)        // 指定Toolbar的颜色。
    , ContextCompat.getColor(this, R.color.colorPrimaryDark));  // 指定状态栏的颜色。
```

接受结果，重写`Activity/Fragment`的`onActivityResult()`方法：
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == 100) {
        if (resultCode == RESULT_OK) { // 判断是否成功。
            // 拿到用户选择的图片路径List：
            List<String> pathList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
            // 根据需要提示用户取消了选择。
        }
    }
}
```

## 注意点
由于支持了MaterialDesign（以后会更加MD），项目中已经引用了Google官方的的support库：
```groovy
compile 'com.android.support:appcompat-v7:24.2.1'
compile 'com.android.support:recyclerview-v7:24.2.1'
compile 'com.android.support:design:24.2.1'
```

## 混淆
都是可以混淆的，如果混淆遇到问题了，请添加如下规则。
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```

## License
```text
Copyright 2016 Yan Zhenjie

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
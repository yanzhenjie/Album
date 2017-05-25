# Album
`Album`是一个MD风格的开源相册，支持国际化，主要功能分为三部分：相册选图、相机拍照、画廊预览。

技术交流群：[46523908](http://jq.qq.com/?_wv=1027&k=410oIg0)  

我的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
我的微博：[http://weibo.com/yanzhenjieit](http://weibo.com/yanzhenjieit)  

图片剪切框架：[https://github.com/yanzhenjie/Durban](https://github.com/yanzhenjie/Durban)  
文件上传Http框架：[https://github.com/yanzhenjie/NoHttp](https://github.com/yanzhenjie/NoHttp)  

# 特性
1. 完美支持7.0，不存在**Android7.0 FileUriExposedException**。
2. 支持组件：`Activity`、`Fragment`。
3. UI可以配置，比如：`Toolbar`、`StatusBar`、`NavigationBar`。
4. 相册选图，单选、多选、文件夹预览。
5. 相机拍照，可以单独调用、也可以以Item展示在相册中。
6. 画廊，支持缩放、支持浏览本地图片、支持浏览网络图片。
7. 支持配置相册列数，支持配置相册是否使用相机。
8. 画廊预览选择的图片，预览时可以反选。
9. 支持自定义`LocalImageLoader`，例如使用：`Glide`、`Picasso`、`ImageLoader`实现。
10. 支持和图片裁剪框架[Durban](https://github.com/yanzhenjie/Durban)结合使用，Durban支持一次性裁剪多张图片。

# 效果预览
体验请[下载demo的apk](https://github.com/yanzhenjie/Album/blob/master/sample-release.apk?raw=true)。  

<image src="./image/1.gif" width="170px"/> <image src="./image/2.gif" width="170px"/> <image src="./image/3.gif" width="170px"/> <image src="./image/4.gif" width="170px"/>

# 依赖
* Gradle：
```groovy
compile 'com.yanzhenjie:album:1.0.6'
```

* Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>album</artifactId>
  <version>1.0.6</version>
  <type>pom</type>
</dependency>
```
* Eclipse 请放弃治疗。

# mainifest.xml中需要注册
这里留给开发者自己注册是因为你可以选择横屏或者竖屏。  
```xml
<activity
    android:name="com.yanzhenjie.album.AlbumActivity"
    android:configChanges="orientation|keyboardHidden|screenSize"
    android:theme="@style/Theme.AppCompat.Light.NoActionBar"
    android:windowSoftInputMode="stateAlwaysHidden|stateHidden" />
```

# 权限
```xml
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```

* 开发者不需要担心`Android6.0`运行时权限，`Album`已经非常完善的处理过了。  
* `Android6.0`运行时权限推荐使用：[AndPermission](https://github.com/yanzhenjie/AndPermission)，如果不使用6.0特性，建议把`targetSdkVersion`值设置的小于23，这样就不会使用`Android6.0`运行时权限了。
* 本库使用的`support`库的版本为`25.3.1`，如果你的版本高于`25.3.1`，你需要明确指定`CardView`的版本。

# 使用教程
`Album`主要功能分为三部分：相册选图、相机拍照、画廊预览，下面分别说明。

## Album 相册
使用`Album.album(context).start()`即可调起相册。
```java
Album.album(context)
    .requestCode(999) // 请求码，返回时onActivityResult()的第一个参数。
    .toolBarColor(toolbarColor) // Toolbar 颜色，默认蓝色。
    .statusBarColor(statusBarColor) // StatusBar 颜色，默认蓝色。
    .navigationBarColor(navigationBarColor) // NavigationBar 颜色，默认黑色，建议使用默认。
    .title("图库") // 配置title。
    
    .selectCount(9) // 最多选择几张图片。
    .columnCount(2) // 相册展示列数，默认是2列。
    .camera(true) // 是否有拍照功能。
    .checkedList(mImageList) // 已经选择过得图片，相册会自动选中选过的图片，并计数。
    .start();
```

重写`onActivityResult()`方法，接受图片选择结果：  
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 999) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 不要质疑你的眼睛，就是这么简单。
            ArrayList<String> pathList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // User canceled.
            // 用户取消了操作。
        }
    }
}
```

如果你接着需要对图片进行裁剪，那么你可以紧接着调用[Durban](https://github.com/yanzhenjie/Durban)。

## Camera 相机
使用`Album.camera(context).start()`即可调起相机，已经处理了权限和`Android7.0`的`FileProvider`问题。
```java
Album.camera(context)
    .requestCode(666)
	// .imagePath() // 指定相机拍照的路径，建议非特殊情况不要指定.
    .start();
```

重写`onActivityResult()`方法，接受图片选择结果： 
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 666) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 这里的List的size肯定是1。
            List<String> pathList = Album.parseResult(data); // Parse path.
        } else if (resultCode == RESULT_CANCELED) {
            // 用户取消了操作。
        }
    }
}
```

如果你接着需要对图片进行裁剪，那么你可以紧接着调用[Durban](https://github.com/yanzhenjie/Durban)。

## Gallery 画廊
使用`Album.gallery(context).start()`即可调起画廊。画廊默认支持预览本地图片，如果要预览网络图片，你需要在初始化的时候配置`ImageLoader`，具体见下文。  

调用的时候你只需要传入一个路径集合：  
```java
Album.gallery(context)
    .requestCode(555) // 请求码，返回时onActivityResult()的第一个参数。
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
    if(requestCode == 555) {
        if (resultCode == RESULT_OK) { // Successfully.
            // 不要再次质疑你的眼睛，还是这么简单。
            mImageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) { // User canceled.
            // 用户取消了操作。
        }
    }
}
```

## 高级配置
**这个配置不是必须的，不配置也完全可以用：**。

1. `ImageLoader`，默认使用`LocalImageLoader`，你可以用`Glide`和`Picasso`等其它第三方框架来实现。
2. `Locale`，默认已经支持国际化了，支持简体中文、繁体中文、英语。如果你要指定语言，可以使用`Locale`配置。

### ImageLoader配置
我推荐优先使用默认`ImageLoader`，其次用`Glide`实现、其次是`Picasso`，最后是`ImageLoader`，暂时不支持`Fresco`。

Album提供的默认的`LocalImageLoader`如下：
```java
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Album.initialize(
            new AlbumConfig.Build()
                .setImageLoader(new LocalImageLoader()) // 使用默认loader.
                .build()
        );
    }
}
```

**用Glide实现：**
```java
public class GlideImageLoader implements AlbumImageLoader {
    @Override
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        Glide.with(imageView.getContext())
            .load(new File(imagePath))
            .into(imageView);
    }
}

...

Album.initialize(new AlbumConfig.Build()
    .setImageLoader(new GlideImageLoader()) // Use glide loader.
    .build()
```

**用Picasso实现**
```java
public class PicassoImageLoader implements AlbumImageLoader {

    @Override
    public void loadImage(ImageView imageView, String imagePath, int width, int height) {
        Picasso.with(imageView.getContext())
            .load(new File(imagePath))
            .centerCrop()
            .resize(width, height)
            .into(imageView);
    }
}

...

Album.initialize(new AlbumConfig.Build()
    .setImageLoader(new PicassoImageLoader()) // Use picasso loader.
    .build()
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
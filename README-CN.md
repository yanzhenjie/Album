# Album
`Album`是一个MD风格的开源相册，支持国际化，支持国际化扩展；主要功能模块：选择图片与视频、拍照、录视频、画廊（支持本地和网络）。

技术交流群：[547839514](https://jq.qq.com/?_wv=1027&k=4B0yi1n)  

我的主页：[http://www.yanzhenjie.com](http://www.yanzhenjie.com)  
我的微博：[http://weibo.com/yanzhenjieit](http://weibo.com/yanzhenjieit)  

# 特性
1. 流式Api（Stream Api）。
2. 图片选择、视频选择、图片+视频混合选择，全部支持单选模式和多选模式。
3. 相机支持单独调用，支持拍照片和录视频，支持放在相册的Item中使用。
4. 画廊，支持缩放、支持浏览本地图片与网络图片，或者本地图片和网络图的混合。
5. 画廊支持预览时的选择功能，不使用选择时右上角不会出现【完成】按钮。
6. 多选模式下支持配置选择数量。
7. 支持配置相册列数，支持配置相机是否出现在Item中。
8. StatusBar和Toolbar的背景可定制，而且支持浅色背景，例如白色（支持小米、魅族）。
9. 支持配置第三方ImageLoader，例如使用：Glide、Picasso、ImageLoader实现。
10. 支持和图片裁剪框架[Durban](https://github.com/yanzhenjie/Durban)结合使用，Durban支持一次性裁剪多张图片。

> **注意**：从2.0-alpha版本开始，可以拿到图片和视频的路径、标题、缩略图、大小、经纬度、添加日期、修改日期以及所在文件夹；视频还可以拿到分辨率和时长。

# 截图
<image src="./image/1.gif" width="210px"/> <image src="./image/2.gif" width="210px"/> <image src="./image/3.gif" width="210px"/> <image src="./image/4.gif" width="210px"/>  

白色状态栏，因为Android5.0不支持状态栏深色字体，所以当使用白色状态栏时，状态栏的文字就看不到了，因此Album内部在Android5.0的系统中默认把状态栏置为灰黑色（系统原生色），下图中左边为5.0的效果，右边为5.0+的效果：  
  
<image src="./image/5.gif" width="210px"/> <image src="./image/6.gif" width="210px"/>

# 依赖
* Gradle：
```groovy
compile 'com.yanzhenjie:album:2.0.0-alpha'
```

* Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>album</artifactId>
  <version>2.0.0-alpha</version>
  <type>pom</type>
</dependency>
```

> 本库使用的`support`库的版本为`25.3.1`，如果引入的`support`库版本高于`25.3.1`，请明确指定`CardView`的版本或者使用`exlude`语法过滤。

# 用法
`Album`主要功能模块：选择图片与视频、拍照、录视频、画廊。

## 图片和视频混合选择
```java
Album.album(this) // 图片和视频混选。
    .multipleChoice() // 多选模式，单选模式为：singleChoice()。
    .requestCode(200) // 请求码，会在listener中返回。
    .columnCount(2) // 页面列表的列数。
    .selectCount(6)  // 最多选择几张图片。
    .camera(true) // 是否在Item中出现相机。
    .checkedList(mAlbumFiles) // 要反选的列表，比如选择一次再次选择，可以把上次选择的传入。
    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
            // TODO 接受结果。
        }

        @Override
        public void onAlbumCancel(int requestCode) {
            // 用户取消了操作。
        }
    })
    .start();
```

## 图片的选择
```java
Album.image(this) // 选择图片。
    .multipleChoice()
    .requestCode(200)
    .camera(true)
    .columnCount(2)
    .selectCount(6)
    .checkedList(mAlbumFiles)
    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
        }

        @Override
        public void onAlbumCancel(int requestCode) {
        }
    })
    .start();
```

如果需要对图片进行裁剪，可以使用[Durban](https://github.com/yanzhenjie/Durban)。

## 视频的选择
```java
Album.video(this) // 选择视频。
    .multipleChoice()
    .requestCode(200)
    .camera(true)
    .columnCount(2)
    .selectCount(6)
    .checkedList(mAlbumFiles)
    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
        }

        @Override
        public void onAlbumCancel(int requestCode) {
        }
    })
    .start();
```

## 拍照
```
Album.camera(this) // 相机功能。
    .image() // 拍照。
    // .filePath() // 文件保存路径，非必须。
    .requestCode(2)
    .listener(new AlbumListener<String>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull String result) {
        }

        @Override
        public void onAlbumCancel(int requestCode) {
        }
    })
    .start();
```

如果需要对图片进行裁剪，可以使用[Durban](https://github.com/yanzhenjie/Durban)。

## 录视频
```
Album.camera(this)
    .video() // 录视频，注意与拍照的方法不同。
    // .filePath()
    .requestCode(2)
    .quality(1) // 视频质量，[0, 1]。
    .limitDuration(Long.MAX_VALUE) // 视频最长时长，单位是毫秒。
    .limitBytes(Long.MAX_VALUE) // 视频最大大小，单位byte。
    .listener(new AlbumListener<String>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull String result) {
        }

        @Override
        public void onAlbumCancel(int requestCode) {
        }
    })
    .start();
```

## 画廊
默认支持浏览本地图片，可以配置AlbumLoader来支持浏览网络图片列表。
```java
Album.gallery(this)
    .requestCode(2) // 请求码，会在listener中返回。
    .checkedList(imageList) // 要浏览的图片列表：ArrayList<String>。
    .navigationAlpha(80) // Android5.0+的虚拟导航栏的透明度。
    .checkable(true) // 是否有浏览时的选择功能。
    .listener(new AlbumListener<ArrayList<String>>() { // 如果checkable(false)，那么listener不用传。
        @Override
        public void onAlbumResult(int requestCode, @NonNull ArrayList<String> result) {
            // TODO 接受选择结果。
        }

        @Override
        public void onAlbumCancel(int requestCode) {
        }
    })
    .start(); // 千万不要忘记调用start()方法。
```

> `checkable()`传`false`时，`listener`参数可以不用传，同时在用户浏览图片时，不会出现选择框和右上角的确定按钮。

## 关于AlbumFile的说明
`AlbumFile`是选择图片和视频的返回结果，它包含了图片和视频的路径、标题、缩略图、大小、经纬度、添加日期、修改日期以及所在文件夹；视频还可以拿到分辨率和时长。

> **注意**：图片和视频的预览图，大多数情况下都是有的，如果不是本机拍的，而是导入的，某一部分手机没有预览图。

### 返回的如果是图片
```java
public int getMediaType(); // 文件类型，图片为AlbumFile.TYPE_IMAGE。
public String getPath(); // 文件路径，一定有且不为空。
public String getName(); // 文件名，一定有且不为空，例如：xyz.jpg。
public String getTitle(); // 文件的标题，如果文件名是xyz.jpg，那么标题为xyz。
public String getBucketName(); // 文件所在的文件夹名。
public String getMimeType(); // 文件的MimeType，例如：image/jpeg。
public long getAddDate(); // 文件添加日期，一定有。
public long getModifyDate(); // 文件修改日期，一定不为空。
public float getLatitude(); // 文件被加入库时的纬度，可能为0。
public float getLongitude(); // 文件被加入库时的经度，可能为0。
public long getSize(); // 文件大小，单位是bytes。
public String getThumbPath(); // 图片的预览图，大多数情况有，相册内拍照时返回的一定是原图路径。
```

### 选择图片时的有效参数
```java
public int getMediaType(); // 文件类型，视频为，AlbumFile.TYPE_VIDEO。
public String getPath(); // 文件路径，一定有且不为空。
public String getName(); // 文件名，一定有且不为空，例如：xyz.jpg。
public String getTitle(); // 文件的标题，如果文件名是xyz.jpg，那么标题为xyz。
public String getBucketName(); // 文件所在的文件夹名。
public String getMimeType(); // 文件的MimeType，例如：video/mp4。
public long getAddDate(); // 文件添加日期，一定有。
public long getModifyDate(); // 文件修改日期，一定有。
public float getLatitude(); // 文件被加入库时的纬度，可能为0。
public float getLongitude(); // 文件被加入库时的经度，可能为0。
public long getSize(); // 文件大小，单位是byte。
public long getDuration(); // 视频时长，一定有。
public String getResolution(); // 视频分辨率，一定有。
public String getThumbPath(); // 视频的预览图，大多数情况有，相册内录视频时返回的一定是视频路径。
public int getWidth(); // 视频宽，一定有。
public int getHeight(); // 视频高，一定有。
```

## 定制UI
图片与视频的选择、画廊，支持定制UI，包括修改`StatusBar`的颜色、`Toolbar`的颜色、`StatusBar`和`Toolbar`的文字颜色和图标颜色，例如开头的白色状态栏的演示。

```java
// 比如图片视频混选：
 Album.album(this)
    .multipleChoice()
    .widget(...) // 此方法既是配置UI的方法。
    ...

// 图片的选择：
Album.image(this)
    .multipleChoice()
    .widget(...) // 此方法既是配置UI的方法。
    ...

// 视频的选择：
Album.video(this)
    .multipleChoice()
    .widget(...) // 此方法既是配置UI的方法。
    ...

// 比如画廊：
Album.gallery(this)
    .widget(...) // 此方法既是配置UI的方法。
    ...
```

所以我们只需要传入一个`Widget`参数就好了，这个参数也特别好生成：
```
// 状态栏是深色背景时的构建：
Widget.newDarkBuilder(this)
...

// 状态栏是白色背景时的构建：
Widget.newLightBuilder(this)
...

// 他们调用的方法和传的参数都是一致的：
Widget.xxxBuilder(this)
    .title(...) // 标题。
    .statusBarColor(Color.WHITE) // 状态栏颜色。
    .toolBarColor(Color.WHITE) // Toolbar颜色。
    .navigationBarColor(Color.WHITE) // Android5.0+的虚拟导航栏颜色。
    .mediaItemCheckSelector(Color.BLUE, Color.GREEN) // 图片或者视频选择框的选择器。
    .bucketItemCheckSelector(Color.RED, Color.YELLOW) // 切换文件夹时文件夹选择框的选择器。
    .buttonStyle( // 用来配置当没有发现图片/视频时的拍照按钮和录视频按钮的风格。
        Widget.ButtonStyle.newLightBuilder(this) // 同Widget的Builder模式。
            .setButtonSelector(Color.WHITE, Color.WHITE) // 按钮的选择器。
            .build()
    )
    .build()
```

## 高级配置
如果开发者在**画廊**中不预览网络图片，并且不考虑国际化问题，这个可以不配置。

1. `AlbumLoader`，默认使用`DefaultAlbumLoader`，开发者可以用`Glide`和`Picasso`等其它第三方框架来实现。
2. `Locale`，默认已经支持国际化了，支持简体中文、繁体中文、英语、葡萄牙语。如果开发者不想在切换系统语言时`Album`的语言也跟着变化，开发者可以指定语言。

### Album中自带的AlbumLoader
本人比较推荐加载网络图和本地图用开发者喜欢的框架、加载视频预览图用自带的`DefaultAlbumLoader`。比如第三方框架可以使用`Glide`、`Picasso`和`ImageLoader`，暂时不支持`Fresco`。

`AlbumLoad`的两个方法如下：
```java
public interface AlbumLoader {

    /**
     * Load a preview of the album file.
     *
     * @param imageView  {@link ImageView}.
     * @param albumFile  the media object may be a picture or video.
     * @param viewWidth  the width fo target view.
     * @param viewHeight the width fo target view.
     */
    void loadAlbumFile(ImageView imageView, AlbumFile albumFile, int viewWidth, int viewHeight);

    /**
     * Load a preview of the picture.
     *
     * @param imageView  {@link ImageView}.
     * @param imagePath  file path, which may be a local path or a network path.
     * @param viewWidth  the width fo target view.
     * @param viewHeight the width fo target view.
     */
    void loadImage(ImageView imageView, String imagePath, int viewWidth, int viewHeight);

}
```

Album提供的默认的`AlbumLoader`在实现了上述方法的基础上，又添加了一个直接传入视频路径加载视频预览图的方法：
```java
/**
 * 加载本地视频预览图。
 * 第一个参数：ImageView。
 * 第二个参数：视频文件路径。
 * 第三个参数：加载目的宽，一般都是View的宽，不能传0。
 * 第四个参数：加载目的高，一般都是View的高，不能传0。
 */
DefaultAlbumLoader.getInstance().loadVideo(ImageView, String, int, int);
```

### 用Glide或者Picasso实现AlbumLoader
[GlideAlbumLoader](https://github.com/yanzhenjie/Album/blob/master/sample/src/main/java/com/yanzhenjie/album/sample/load/GlideAlbumLoader.java)  

[PicassoAlbumLoader](https://github.com/yanzhenjie/Album/blob/master/sample/src/main/java/com/yanzhenjie/album/sample/load/PicassoAlbumLoader.java)

### 如何配置
在`Application`的`onCreate()`中调用配置即可：
```
@Override
public void onCreate() {
    super.onCreate();

    Album.initialize(
        AlbumConfig.newBuilder(this)
            .setAlbumLoader(new GlideAlbumLoader()) // 设置Album加载器。
            .setLocale(Locale.CHINA) // 比如强制设置在任何语言下都用中文显示。
            .build()
    );
}
```

如果自带的语言类型不够用，请开发者拷贝Album库中的`string.xml`到目标项目的对应语言的文件夹下并做好翻译即可，如果需要强制指定为该语言，设置方法同上。

# 混淆
这是不必要的，如果混淆后相册出现了问题，请在混淆规则中添加：  
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```

# Thanks
[PhotoView](https://github.com/chrisbanes/PhotoView)  

# 关于我
微信扫一扫关注我的微信公众号  
  
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
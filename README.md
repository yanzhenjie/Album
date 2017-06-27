# Album
`Album` is a MaterialDesign tyle open source album, the main function is: `Album, Camera and Gallery`.  

Image crop library **Durban**: [https://github.com/yanzhenjie/Durban](https://github.com/yanzhenjie/Durban)

[中文文档](./README-CN.md)

# Features
1. Perfect for **Android7.0 FileUriExposedException**.
2. Support component：`Activity`、`Fragment`.
3. UI style can be configured, for example: `Toolbar`、`StatusBar`、`NavigationBar`.
4. Album: Radio, multi-select, folder preview.
5. Camera: call alone, as the item of the album.
6. Gallery: Support zoom, browse local pictures and network pictures.
7. Configure the album's number of columns, configure the album if there is a camera.
8. Gallery preview multiple pictures, preview can be anti-election.
9. Support for custom LocalImageLoader, such as: `Glide`, `Picasso`, `ImageLoder`.
10. Support and use the image crop library [Durban](https://github.com/yanzhenjie/Durban).

# Screenshot
Please experience [download apk](https://github.com/yanzhenjie/Album/blob/master/sample-release.apk?raw=true).  

<image src="./image/1.gif" width="170px"/> <image src="./image/2.gif" width="170px"/> <image src="./image/3.gif" width="170px"/> <image src="./image/4.gif" width="170px"/>

# Dependencies
* Gradle：
```groovy
compile 'com.yanzhenjie:album:1.0.7'
```

* Maven:
```xml
<dependency>
  <groupId>com.yanzhenjie</groupId>
  <artifactId>album</artifactId>
  <version>1.0.7</version>
  <type>pom</type>
</dependency>
```

# Permission
```xml
<uses-permission android:name="android.permission.CAMERA"/>
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
<uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
```

* Developers do not need to worry about `Android6.0` runtime permissions，`Album` has been very well handled.  
* The repository version of the `support-library` is `25.3.1`, if your version is higher than `25.3.1`, you need to explicitly specify the version of` CardView`.

# Usage
Album's main function is: `Album, Camera and Gallery`, the following are described separately.  

## Album
Use `Album.album(context).start()` to call up the `Album`.  
```java
Album.album(context)
    .toolBarColor(toolbarColor) // Toolbar color.
    .statusBarColor(statusBarColor) // StatusBar color.
    .navigationBarColor(navigationBarColor) // NavigationBar color.
    .title("Album") // Title.
    
    .selectCount(9) // Choose up to a few pictures.
    .columnCount(2) // Number of albums.
    .camera(true) // Have a camera function.
    .checkedList(mImageList) // Has selected the picture, automatically select.
    .start(999); // 999 is requestCode.
```

Accept the result：  
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 999) {
        if (resultCode == RESULT_OK) { // Successfully.
            // Parse select result.
            ArrayList<String> imageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            // User canceled.
        }
    }
}
```

Cut the selection results, then use [Durban](https://github.com/yanzhenjie/Durban).  

**If you need to radio**, that is, in the album click on the picture to return:  
```java
Album.albumRadio(this)
    .toolBarColor(toolbarColor) // Toolbar color.
    .statusBarColor(statusBarColor) // StatusBar color.
    .navigationBarColor(navigationBarColor) // NavigationBar color.
    .title("Album") // Title.
    
    .selectCount(9) // Choose up to a few pictures.
    .columnCount(2) // Number of albums.
    .start(999); // 999 is requestCode.
```
And call the election is different from the `Album.albumRadio(context)`, and lost `selectCount()` and `checkedList()` methods.

## Camera
Use the `Album.camera(context).start ()` to call up the `Camera`, has handled the RunTimePermissions and `Android7.0 FileProvider`
```java
Album.camera(context)
    // .imagePath() // Specify the image path, optional.
    .start(666); // 666 is requestCode.
```

Accept the result： 
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 666) {
        if (resultCode == RESULT_OK) { // Successfully.
            // The size of the pathList is 1.
            List<String> pathList = Album.parseResult(data); // Parse path.
        } else if (resultCode == RESULT_CANCELED) {
            // User canceled.
        }
    }
}
```

Cut the selection results, then use [Durban](https://github.com/yanzhenjie/Durban).

## Gallery
Use the `Album.gallery(context).start ()` to call up the `Gallery`, default support for previewing local images, preview the network pictures need to configure `ImageLoader`, see below for details.

Call you only need to pass in a path set:  
```java
Album.gallery(context)
    .toolBarColor(toolbarColor) // Toolbar color.
    .statusBarColor(statusBarColor) // StatusBar color.
    .navigationBarColor(navigationBarColor) // NavigationBar color.
    
    .checkedList(mImageList) // List of pictures to preview.
    .currentPosition(position) // First display position image of the list.
    .checkFunction(true) // Anti-election function.
    .start(444); // 444 is requestCode.
```

**Note:**

* Be sure to pass in the collection of pictures you want to preview, otherwise it will return immediately after the start.  
* It is recommended to call the gallery preview to judge `if(currentPosition < mImageList.size())`, to ensure that the entry of the `position` in the `list`.  

If you need to have an anti-selection function at the time of preview，override the `onActivityResult ()` method, after receiving the anti-selected picture `List` result:  
```java
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if(requestCode == 444) {
        if (resultCode == RESULT_OK) { // Successfully.
            // Parse select result.
            ArrayList<String> imageList = Album.parseResult(data);
        } else if (resultCode == RESULT_CANCELED) {
            // User canceled.
        }
    }
}
```

## Advanced configuration
**This configuration is not necessary, not configured can also be used:**

1. `ImageLoader`, the default use of 'LocalImageLoader`, you can use` Glide` and `Picasso` and other third-party framework to achieve.  
2. `Locale`, the default has been supported international, and support Simplified Chinese, Traditional Chinese, English. If you want to specify the language, you can use the `Locale` configuration.

### ImageLoader config
I recommend using the default `ImageLoader` first, followed by` Glide`, followed by `Picasso`, and finally` ImageLoader`, which does not support `Fresco` for the time being.

```java
public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Album.initialize(
            new AlbumConfig.Build()
                .setImageLoader(new LocalImageLoader()) // Use default loader.
                .build()
        );
    }
}
```

**Use Glide:**
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

**Use Picasso:**
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

# Proguard-rules
If there is a problem, add the rule to the proguard-rules:
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```

# Thanks
1. [PhotoView](https://github.com/chrisbanes/PhotoView)

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
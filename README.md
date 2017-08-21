# Album
`Album` is an Material Design Style open source album, support internationalization, support for international expansion; main function modules: select image and video, take picture, record video, gallery.

<image src="./image/logo.png"/>  

# Feature
1. Stream Api.
2. Select Image, selecte video, select a mix of pictures and videos, all support single-select mode and multi-select mode.
3. Thme camera function to support taking pictures and recording video, and support for use in the album's item.
4. Gallery, support for zoom, support for browsing local images with network images, or a mix of local images and network images.
5. The gallery supports for selection when previewing, and the "Finish" button does not appear in the upper right corner when the selection is not used.
6. Multi-select mode supports setting the number of choices.
7. Support setting number of column, support setting the camera appears in the Item.
8. StatusBar and Toolbar's background can be customized, and support light-colored background, such as white.
9. Support other `ImageLoader`, for example: `Glide`, `Picasso`...
10. Support and image cropping framework [Durban](https://github.com/yanzhenjie/Durban) in combination, `Durban` it supports cutting multiple images at the same time.

> **Note**: Starting from version 2.0-alpha, return the data for the `AlbumFile` object, developer can get image and video path, title, thumbnail, size, latitude and longitude, added and modified date, the folder; video can also get the resolution and duration.

# Screenshot
<image src="./image/1.gif" width="210px"/> <image src="./image/2.gif" width="210px"/> <image src="./image/3.gif" width="210px"/> <image src="./image/4.gif" width="210px"/>  

White StatusBar, because Android5.0 does not support the StatusBar dark font, so when using the white StatusBar, the text of the StatusBar can not see, so the album within the Android5.0 system default StatusBar gray Black (system original color). The left is the effect of 5.0, the right is the effect of 5.0:  
  
<image src="./image/5.gif" width="210px"/> <image src="./image/6.gif" width="210px"/>

# Dependencies
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

> The `support` library version is `25.3.1`. If the `support` library version is higher than `25.3.1`, please specify the version of `CardView` or use the `exlude` syntax to filter it.

# Usege
The function modules of Album: select image and video, take picture, record video, gallery.

## Image and video mix options
```java
Album.album(this) // Image and video mix options.
    .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
    .requestCode(200) // The request code will be returned in the listener.
    .columnCount(2) // The number of columns in the page list.
    .selectCount(6)  // Choose up to a few images.
    .camera(true) // Whether the camera appears in the Item.
    .checkedList(mAlbumFiles) // To reverse the list.
    .listener(new AlbumListener<ArrayList<AlbumFile>>() {
        @Override
        public void onAlbumResult(int requestCode, @NonNull ArrayList<AlbumFile> result) {
            // TODO accept the result.
        }

        @Override
        public void onAlbumCancel(int requestCode) {
            // The user canceled the operation.
        }
    })
    .start();
```

## Image Selection
```java
Album.image(this) // Image selection.
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

If developer want to crop the image, please use [Durban](https://github.com/yanzhenjie/Durban).

## Video Selection
```java
Album.video(this) // Video selection.
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

## Take Picture
```
Album.camera(this) // Camera function.
    .image() // Take Picture.
    // .filePath() // File save path, not required.
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

If developer want to crop the image, please use [Durban](https://github.com/yanzhenjie/Durban).

## Record Video
```
Album.camera(this)
    .video() // Record Video.
    // .filePath()
    .requestCode(2)
    .quality(1) // Video quality, [0, 1].
    .limitDuration(Long.MAX_VALUE) // The longest duration of the video is in milliseconds.
    .limitBytes(Long.MAX_VALUE) // Maximum size of the video, in bytes.
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

## Gallery
By default, developer can preview the local image, developer can configure AlbumLoader to support browsing the network image list.

```java
Album.gallery(this)
    .requestCode(2)
    .checkedList(imageList) // List of image to view: ArrayList<String>.
    .navigationAlpha(80) // Virtual NavigationBar alpha of Android5.0+.
    .checkable(true) // Whether there is a selection function.
    .listener(new AlbumListener<ArrayList<String>>() { // If checkable(false), listener not required.
        @Override
        public void onAlbumResult(int requestCode, @NonNull ArrayList<String> result) {
        }

        @Override
        public void onAlbumCancel(int requestCode) {
        }
    })
    .start();
```

> If `checkable(false)`, listener not required, the `CheckBox` and the `FinishButton` will be not appear.

## Capabilities of AlbumFile
`AlbumFile` is the result of the selection of images and videos, which contains the path and title of the image and video, the title, thumbnail, size, latitude and longitude, added and modified date, and folder; video can also get resolution and duration.

> **Note**: thumbnail of image and video, in most cases are there, if not locally produced, but imported via externally, a part of the phone did not.

### Image
```java
public int getMediaType(); // File type, the image is AlbumFile.TYPE_IMAGE.
public String getPath(); // File path, must not be empty.
public String getName(); // File name, must not be empty, for example: xyz.jpg.
public String getTitle(); // The title of the file, if the file name is xyz.jpg, then the title is xyz.
public String getBucketName(); // The name of the folder where the file is located.
public String getMimeType(); // File MimeType, for example: image/jpeg.
public long getAddDate(); // File to add date, must have.
public long getModifyDate(); // File to modification date, must not be empty.
public float getLatitude(); // The latitude of the file, may be zero.
public float getLongitude(); // The longitude of the file, may be zero.
public long getSize(); // File size in bytes.
public String getThumbPath(); // The preview of image, the photo album to return to the original image must be the original path.
```

### Video
```java
public int getMediaType(); // File type, the video is AlbumFile.TYPE_VIDEO.
public String getPath(); // File path, must not be empty.
public String getName(); // File name, must not be empty, for example: xyz.jpg.
public String getTitle(); // The title of the file, if the file name is xyz.jpg, then the title is xyz.
public String getBucketName(); // The name of the folder where the file is located.
public String getMimeType(); // File MimeType, for example: image/jpeg.
public long getAddDate(); // File to add date, must have.
public long getModifyDate(); // File to modification date, must not be empty.
public float getLatitude(); // The latitude of the file, may be zero.
public float getLongitude(); // The longitude of the file, may be zero.
public long getSize(); // File size in bytes.
public long getDuration(); // Video duration, must have.
public String getResolution(); // Video resolution, must have.
public int getWidth(); // Video wide, must have.
public int getHeight(); // Video height, must have.
public String getThumbPath(); // The preview of video, the photo album to return to the original image must be the original path.
```

## Customize UI
Select image and video, gallery, support custom UI, including modification of the StatusBar's color, Toolbar's color, text color and icon color os StatusBar and Toolbar, such as the beginning of the white StatusBar demo.

```java
// Such as image video mix:
 Album.album(this)
    .multipleChoice()
    .widget(...)
    ...

// Image selection:
Album.image(this)
    .multipleChoice()
    .widget(...)
    ...

// Video selection:
Album.video(this)
    .multipleChoice()
    .widget(...)
    ...

// Gallery:
Album.gallery(this)
    .widget(...)
    ...
```

So we only need to pass in a `Widget` parameter just fine:
```
// StatusBar is a dark background when building:
Widget.newDarkBuilder(this)
...

// StatusBar is a light background when building:
Widget.newLightBuilder(this)
...

// Such as:
Widget.xxxBuilder(this)
    .title(...) // Title.
    .statusBarColor(Color.WHITE) // StatusBar color.
    .toolBarColor(Color.WHITE) // Toolbar color.
    .navigationBarColor(Color.WHITE) // Virtual NavigationBar color of Android5.0+.
    .mediaItemCheckSelector(Color.BLUE, Color.GREEN) // Image or video selection box.
    .bucketItemCheckSelector(Color.RED, Color.YELLOW) // Select the folder selection box.
    .buttonStyle( // Used to configure the style of button when the image/video is not found.
        Widget.ButtonStyle.newLightBuilder(this) // With Widget's Builder model.
            .setButtonSelector(Color.WHITE, Color.WHITE) // Button selector.
            .build()
    )
    .build()
```

## Advanced configuration
If the developer does not preview the network images in **Gallery**, And do not consider the issue of internationalization, this can not be setted.

1. `AlbumLoader`, The default use of `DefaultAlbumLoader`, developers can use other frameworks,  such as `Glide` and `Picasso`.
2. `Locale`，acquiescence has supported internationalization, support Simplified Chinese, Traditional Chinese, English, Portuguese. If the developer does not want to change the language of the system when the language is changed, the developer can specify the language.

### AlbumLoader
I prefer to load the network image and the local image with the developers like the framework, load the video preview with the own `DefaultAlbumLoader`.

The two methods of `AlbumLoad` are as follows:
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

Album provides the default `AlbumLoader` on the basis of the above method, but also added a direct access to the video path to load the video preview method:
```java
/**
 * Load local video preview.
 * The first parameter: ImageView.
 * The second parameter: the video file path.
 * The third parameter: the width of the view, can not pass 0.
 * The third parameter: the height of the view, can not pass 0.
 */
DefaultAlbumLoader.getInstance().loadVideo(ImageView, String, int, int);
```

Implement AlbumLoader with Glide or Picasso：
1. [GlideAlbumLoader](https://github.com/yanzhenjie/Album/blob/master/sample/src/main/java/com/yanzhenjie/album/sample/load/GlideAlbumLoader.java)
2. [PicassoAlbumLoader](https://github.com/yanzhenjie/Album/blob/master/sample/src/main/java/com/yanzhenjie/album/sample/load/PicassoAlbumLoader.java)

### How to configure
Invoke it in the `Application#onCreate()`:
```
@Override
public void onCreate() {
    super.onCreate();

    Album.initialize(
        AlbumConfig.newBuilder(this)
            .setAlbumLoader(new GlideAlbumLoader()) // Set album load.
            .setLocale(Locale.ENGLISH) // For example, Fixed display language is English.
            .build()
    );
}
```

If the language is not enough, then please copy the `string.xml` in the Library to target project. And then do the corresponding translation can be.

# Proguard-rules
Not necessary, if there is a problem, add the rule to the proguard-rules:
```txt
-dontwarn com.yanzhenjie.album.**
-keep class com.yanzhenjie.album.**{*;}
```

# Thanks
[PhotoView](https://github.com/chrisbanes/PhotoView)  

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
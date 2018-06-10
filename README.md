# Album
<image src="./image/logo.png"/>  

Album is a Material Design style album, it provides three functions: album, camera and gallery.

1. Select images, selecte videos, or select pictures and videos.
2. Take a picture, record a video, or use camera in album list.
3. Preview pictures and videos in the gallery or select pictures and videos in the gallery.

## Screenshot
<image src="./image/1.gif" width="210px"/> <image src="./image/2.gif" width="210px"/> <image src="./image/3.gif" width="210px"/> <image src="./image/4.gif" width="210px"/>  

White StatusBar, the left is the effect of 5.0-(Containing 5.0), the right is the effect of 6.0+(Containing 6.0):  
  
<image src="./image/5.gif" width="210px"/> <image src="./image/6.gif" width="210px"/>  

Effect on landscape screen:  

<image src="./image/7.gif"/>

## Download
```groovy
implementation 'com.yanzhenjie:album:2.1.2'
```

## Usage
Developers must configure `AlbumLoader` to make Album work normally, and AlbumLoader is used to load thumbnails of images and videos.

This is an example:
```java
public class MediaLoader implements AlbumLoader {

    @Override
    public void load(ImageView imageView, AlbumFile albumFile) {
        load(imageView, albumFile.getPath());
    }

    @Override
    public void load(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .load(url)
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .crossFade()
                .into(imageView);
    }
}
```
The example uses [Glide](https://github.com/bumptech/glide) to load thumbnails of pictures and videos. Please remember to configure the `AlbumLoader` you just implemented.
```java
Album.initialize(AlbumConfig.newBuilder(this)
    .setAlbumLoader(new MediaLoader())
    ...
    .build());
```

### Image and video mix options
```java
Album.album(this) // Image and video mix options.
    .multipleChoice() // Multi-Mode, Single-Mode: singleChoice().
    .columnCount() // The number of columns in the page list.
    .selectCount()  // Choose up to a few images.
    .camera() // Whether the camera appears in the Item.
    .cameraVideoQuality(1) // Video quality, [0, 1].
    .cameraVideoLimitDuration(Long.MAX_VALUE) // The longest duration of the video is in milliseconds.
    .cameraVideoLimitBytes()(Long.MAX_VALUE) // Maximum size of the video, in bytes.
    .checkedList() // To reverse the list.
    .filterSize() // Filter the file size.
    .filterMimeType() // Filter file format.
    .filterDuration() // Filter video duration.
    .afterFilterVisibility() // Show the filtered files, but they are not available.
    .onResult(new Action<ArrayList<AlbumFile>>() {
        @Override
        public void onAction(@NonNull ArrayList<AlbumFile> result) {
            // TODO accept the result.
        }
    })
    .onCancel(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
            // The user canceled the operation.
        }
    })
    .start();
```

### Image Selection
```java
Album.image(this) // Image selection.
    .multipleChoice()
    .camera()
    .columnCount()
    .selectCount()
    .checkedList(mAlbumFiles)
    .filterSize() // Filter the file size.
    .filterMimeType() // Filter file format.
    .afterFilterVisibility() // Show the filtered files, but they are not available.
    .onResult(new Action<ArrayList<AlbumFile>>() {
        @Override
        public void onAction(@NonNull ArrayList<AlbumFile> result) {
        }
    })
    .onCancel(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .start();
```

If developer want to crop the image, please use [Durban](https://github.com/yanzhenjie/Durban).

### Video Selection
```java
Album.video(this) // Video selection.
    .multipleChoice()
    .camera(true)
    .columnCount(2)
    .selectCount(6)
    .checkedList(mAlbumFiles)
    .filterSize()
    .filterMimeType()
    .filterDuration()
    .afterFilterVisibility() // Show the filtered files, but they are not available.
    .onResult(new Action<ArrayList<AlbumFile>>() {
        @Override
        public void onAction(@NonNull ArrayList<AlbumFile> result) {
        }
    })
    .onCancel(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .start();
```

### Take Picture
```java
Album.camera(this) // Camera function.
    .image() // Take Picture.
    .filePath() // File save path, not required.
    .onResult(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .onCancel(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .start();
```

If developer want to crop the image, please use [Durban](https://github.com/yanzhenjie/Durban).

### Record Video
```java
Album.camera(this)
    .video() // Record Video.
    .filePath()
    .quality(1) // Video quality, [0, 1].
    .limitDuration(Long.MAX_VALUE) // The longest duration of the video is in milliseconds.
    .limitBytes(Long.MAX_VALUE) // Maximum size of the video, in bytes.
    .onResult(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .onCancel(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .start();
```

### Gallery
```java
// Preview AlbumFile:
Album.galleryAlbum(this)
...

// Preview path:
Album.gallery(this)
    .checkedList(imageList) // List of image to view: ArrayList<String>.
    .checkable(true) // Whether there is a selection function.
    .onResult(new Action<ArrayList<String>>() { // If checkable(false), action not required.
        @Override
        public void onAction(@NonNull ArrayList<String> result) {
        }
    })
    .onCancel(new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
        }
    })
    .start();
```

> If `checkable(false)`, listener not required, the `CheckBox` and the `FinishButton` will be not appear.

The user may click or long press on the preview image and the developer can listen to both events:
```java
Album.gallery(this)
    ...
    .itemClick(new ItemAction<String>() {
        @Override
        public void onAction(Context context, String item) {
        }
    })
    .itemLongClick(new ItemAction<String>() {
        @Override
        public void onAction(Context context, String item) {
        }
    })
    .start();
```

### Capabilities of AlbumFile
`AlbumFile` is the result of the selection of images and videos, The properties of the image and video are different, and their different attributes are listed below.

#### Image
```java
public int getMediaType(); // File type, the image is AlbumFile.TYPE_IMAGE.
public String getPath(); // File path, must not be empty.
public String getBucketName(); // The name of the folder where the file is located.
public String getMimeType(); // File MimeType, for example: image/jpeg.
public long getAddDate(); // File to add date, must have.
public float getLatitude(); // The latitude of the file, may be zero.
public float getLongitude(); // The longitude of the file, may be zero.
public long getSize(); // File size in bytes.
public String getThumbPath(); // This is a small thumbnail.
```

#### Video
```java
public int getMediaType(); // File type, the video is AlbumFile.TYPE_VIDEO.
public String getPath(); // File path, must not be empty.
public String getBucketName(); // The name of the folder where the file is located.
public String getMimeType(); // File MimeType, for example: image/jpeg.
public long getAddDate(); // File to add date, must have.
public float getLatitude(); // The latitude of the file, may be zero.
public float getLongitude(); // The longitude of the file, may be zero.
public long getSize(); // File size in bytes.
public long getDuration(); // Video duration, must have.
public String getThumbPath(); // This is a small thumbnail.
```

### Customize UI
Through `Widget`, developer can configure the title, color of StatusBar, color of NavigationBar and so on.

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

// Gallery, preview AlbumFile:
Album.galleryAlbum(this)
    .widget(...)
    ...

// Gallery, preview path:
Album.gallery(this)
    .widget(...)
    ...
```

So we only need to pass in a `Widget` parameter just fine:
```java
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

### Configuration language
Album defaults to English and changes with the system language. Unfortunately, Album only supports English, Simplified Chinese, Traditional Chinese and Portuguese. However, developers can copy the items in Album's `string.xml` into your project for translation, the best thing is that you can [contribute](CONTRIBUTING.md) and submit pull requests to perfect Album.

Developers can specify Album's language:
```java
Album.initialize(AlbumConfig.newBuilder(this)
    ...
    .setLocale(Locale.ENGLISH)
    .build());
```

## Contributing
Before submitting pull requests, contributors must abide by the [agreement](CONTRIBUTING.md) .

## Proguard-rules
If you are using ProGuard you might need to add the following options:
```txt
-dontwarn com.yanzhenjie.album.**
-dontwarn com.yanzhenjie.mediascanner.**
```

## License
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
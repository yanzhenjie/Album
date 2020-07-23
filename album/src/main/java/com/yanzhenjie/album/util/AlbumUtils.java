/*
 * Copyright 2016 Yan Zhenjie.
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
package com.yanzhenjie.album.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.webkit.MimeTypeMap;

import com.yanzhenjie.album.provider.CameraFileProvider;
import com.yanzhenjie.album.widget.divider.Api20ItemDivider;
import com.yanzhenjie.album.widget.divider.Api21ItemDivider;
import com.yanzhenjie.album.widget.divider.Divider;

import java.io.File;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * <p>Helper for album.</p>
 * Created by Yan Zhenjie on 2016/10/30.
 */
public class AlbumUtils {
    
    private static final String CACHE_DIRECTORY = "AlbumCache";
    
    /**
     * Get a writable root directory.
     *
     * @param context context.
     * @return {@link File}.
     */
    @NonNull
    public static File getAlbumRootPath(Context context) {
        if (sdCardIsAvailable(context)) {
            return new File(context.getExternalCacheDir(), CACHE_DIRECTORY);
        } else {
            return new File(context.getFilesDir(), CACHE_DIRECTORY);
        }
    }
    
    /**
     * SD card is available.
     *
     * @return true when available, other wise is false.
     */
    public static boolean sdCardIsAvailable(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return context.getExternalCacheDir().canWrite();
        } else {
            return false;
        }
    }
    
    /**
     * Setting {@link Locale} for {@link Context}.
     *
     * @param context to set the specified locale context.
     * @param locale  locale.
     */
    @NonNull
    public static Context applyLanguageForContext(@NonNull Context context, @NonNull Locale locale) {
        Resources resources = context.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);
            return context.createConfigurationContext(config);
        } else {
            config.locale = locale;
            resources.updateConfiguration(config, resources.getDisplayMetrics());
            return context;
        }
    }
    
    /**
     * Take picture.
     *
     * @param activity    activity.
     * @param requestCode code, see {@link Activity#onActivityResult(int, int, Intent)}.
     * @param outPath     file path.
     */
    public static void takeImage(@NonNull Activity activity, int requestCode, File outPath) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = getUri(activity, outPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, requestCode);
    }
    
    /**
     * Take video.
     *
     * @param activity    activity.
     * @param requestCode code, see {@link Activity#onActivityResult(int, int, Intent)}.
     * @param outPath     file path.
     * @param quality     currently value 0 means low quality, suitable for MMS messages, and  value 1 means high quality.
     * @param duration    specify the maximum allowed recording duration in seconds.
     * @param limitBytes  specify the maximum allowed size.
     */
    public static void takeVideo(@NonNull Activity activity, int requestCode, File outPath,
                                 @IntRange(from = 0, to = 1) int quality,
                                 @IntRange(from = 1) long duration,
                                 @IntRange(from = 1) long limitBytes) {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        Uri uri = getUri(activity, outPath);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, quality);
        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, duration);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, limitBytes);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, requestCode);
    }
    
    /**
     * Generates an externally accessed URI based on path.
     *
     * @param context context.
     * @param outPath file path.
     * @return the uri address of the file.
     */
    @NonNull
    public static Uri getUri(@NonNull Context context, @NonNull File outPath) {
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(outPath);
        } else {
            uri = CameraFileProvider.getUriForFile(context, CameraFileProvider.getProviderName(context), outPath);
        }
        return uri;
    }
    
    /**
     * Generates an externally accessed URI based on path.
     *
     * @param context context.
     * @param outPath file path.
     * @return the uri address of the file.
     */
    @NonNull
    public static Uri getUri(@NonNull Context context, @NonNull String outPath) {
        return getUri(context, new File(outPath));
    }
    
    /**
     * Generates a random jpg file path in the specified directory.
     *
     * @return file path.
     */
    @NonNull
    public static String randomJPGPath(@NonNull Context context) {
        return randomJPGPath(getAlbumRootPath(context));
    }
    
    /**
     * Generates a random jpg file path in the specified directory.
     *
     * @param bucket specify the directory.
     * @return file path.
     */
    @NonNull
    public static String randomJPGPath(@NonNull File bucket) {
        String outFileName = AlbumUtils.getNowDateTime("yyyyMMdd_HHmmssSSS") + "_" + getMD5ForString(UUID.randomUUID().toString()) + ".jpg";
        if (bucket.exists() && bucket.isFile()) {
            bucket.delete();
        }
        if (!bucket.exists()) {
            bucket.mkdirs();
        }
        File file = new File(bucket, outFileName);
        return file.getAbsolutePath();
    }
    
    /**
     * Generate a random mp4 file path.
     *
     * @param context context.
     * @return file path.
     */
    @NonNull
    public static String randomMP4Path(@NonNull Context context) {
        return randomMP4Path(getAlbumRootPath(context));
    }
    
    /**
     * Generates a random mp4 file path in the specified directory.
     *
     * @return file path.
     */
    @NonNull
    public static String randomMP4Path(@NonNull File bucket) {
        String outFileName = AlbumUtils.getNowDateTime("yyyyMMdd_HHmmssSSS") + "_" + getMD5ForString(UUID.randomUUID().toString()) + ".mp4";
        if (bucket.exists() && bucket.isFile()) {
            bucket.delete();
        }
        if (!bucket.exists()) {
            bucket.mkdirs();
        }
        File file = new File(bucket, outFileName);
        return file.getAbsolutePath();
        
    }
    
    /**
     * Format the current time in the specified format.
     *
     * @return the time string.
     */
    @NonNull
    public static String getNowDateTime(@NonNull String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.ENGLISH);
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }
    
    /**
     * Get the mime type of the file in the url.
     *
     * @param url file url.
     * @return mime type.
     */
    public static String getMimeType(String url) {
        String extension = getExtension(url);
        if (!MimeTypeMap.getSingleton().hasExtension(extension)) {
            return "";
        }
        
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return TextUtils.isEmpty(mimeType) ? "" : mimeType;
    }
    
    /**
     * Get the file extension in url.
     *
     * @param url file url.
     * @return extension.
     */
    public static String getExtension(String url) {
        url = TextUtils.isEmpty(url) ? "" : url.toLowerCase();
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return TextUtils.isEmpty(extension) ? "" : extension;
    }
    
    /**
     * Specifies a tint for {@code drawable}.
     *
     * @param drawable drawable target, mutate.
     * @param color    color.
     */
    public static void setDrawableTint(@NonNull Drawable drawable, @ColorInt int color) {
        DrawableCompat.setTint(DrawableCompat.wrap(drawable.mutate()), color);
    }
    
    /**
     * Specifies a tint for {@code drawable}.
     *
     * @param drawable drawable target, mutate.
     * @param color    color.
     * @return convert drawable.
     */
    @NonNull
    public static Drawable getTintDrawable(@NonNull Drawable drawable, @ColorInt int color) {
        drawable = DrawableCompat.wrap(drawable.mutate());
        DrawableCompat.setTint(drawable, color);
        return drawable;
    }
    
    /**
     * {@link ColorStateList}.
     *
     * @param normal    normal color.
     * @param highLight highLight color.
     * @return {@link ColorStateList}.
     */
    public static ColorStateList getColorStateList(@ColorInt int normal, @ColorInt int highLight) {
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_checked};
        states[1] = new int[]{android.R.attr.state_pressed};
        states[2] = new int[]{android.R.attr.state_selected};
        states[3] = new int[]{};
        states[4] = new int[]{};
        states[5] = new int[]{};
        int[] colors = new int[]{highLight, highLight, highLight, normal, normal, normal};
        return new ColorStateList(states, colors);
    }
    
    /**
     * Change part of the color of CharSequence.
     *
     * @param content content text.
     * @param start   start index.
     * @param end     end index.
     * @param color   color.
     * @return {@code SpannableString}.
     */
    @NonNull
    public static SpannableString getColorText(@NonNull CharSequence content, int start, int end, @ColorInt int color) {
        SpannableString stringSpan = new SpannableString(content);
        stringSpan.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return stringSpan;
    }
    
    /**
     * Return a color-int from alpha, red, green, blue components.
     *
     * @param color color.
     * @param alpha alpha, alpha component [0..255] of the color.
     */
    @ColorInt
    public static int getAlphaColor(@ColorInt int color, @IntRange(from = 0, to = 255) int alpha) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
    
    /**
     * Generate divider.
     *
     * @param color color.
     * @return {@link Divider}.
     */
    public static Divider getDivider(@ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Api21ItemDivider(color);
        }
        return new Api20ItemDivider(color);
    }
    
    /**
     * Time conversion.
     *
     * @param duration ms.
     * @return such as: {@code 00:00:00}, {@code 00:00}.
     */
    @NonNull
    public static String convertDuration(@IntRange(from = 1) long duration) {
        duration /= 1000;
        int hour = (int) (duration / 3600);
        int minute = (int) ((duration - hour * 3600) / 60);
        int second = (int) (duration - hour * 3600 - minute * 60);
        
        String hourValue = "";
        String minuteValue;
        String secondValue;
        if (hour > 0) {
            if (hour >= 10) {
                hourValue = Integer.toString(hour);
            } else {
                hourValue = "0" + hour;
            }
            hourValue += ":";
        }
        if (minute > 0) {
            if (minute >= 10) {
                minuteValue = Integer.toString(minute);
            } else {
                minuteValue = "0" + minute;
            }
        } else {
            minuteValue = "00";
        }
        minuteValue += ":";
        if (second > 0) {
            if (second >= 10) {
                secondValue = Integer.toString(second);
            } else {
                secondValue = "0" + second;
            }
        } else {
            secondValue = "00";
        }
        return hourValue + minuteValue + secondValue;
    }
    
    /**
     * Get the MD5 value of string.
     *
     * @param content the target string.
     * @return the MD5 value.
     */
    public static String getMD5ForString(String content) {
        StringBuilder md5Buffer = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] tempBytes = digest.digest(content.getBytes());
            int digital;
            for (int i = 0; i < tempBytes.length; i++) {
                digital = tempBytes[i];
                if (digital < 0) {
                    digital += 256;
                }
                if (digital < 16) {
                    md5Buffer.append("0");
                }
                md5Buffer.append(Integer.toHexString(digital));
            }
        } catch (Exception ignored) {
            return Integer.toString(content.hashCode());
        }
        return md5Buffer.toString();
    }
    
    /**
     * @return 是否是 Android 10 （Q） 之前的版本
     */
    public static boolean isBeforeAndroidTen() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.Q || Environment.isExternalStorageLegacy();
    }
}
package com.yk.customsdk.customlibiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

public class TakePhotoUtils {
    /***拍照*/
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    /***从相册选择照片*/
    public static final int REQUEST_CODE_CHOOSE_PHOTO = 2;
    /***裁剪*/
    public static final int REQUEST_CODE_CROP_PHOTO = 3;

    /**
     * 拍照
     *
     * @param activity
     * @param outputFile
     */
    public static void takePhoto(Activity activity, File outputFile) {
        Intent intent = new Intent();
        intent.setAction("android.media.action.IMAGE_CAPTURE");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri uri = FileProviderUtils.uriFromFile(activity, outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        activity.startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * 从相册选择
     *
     * @param activity
     */
    public static void choosePhoto(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        intent.addCategory("android.intent.category.DEFAULT");
        activity.startActivityForResult(intent, REQUEST_CODE_CHOOSE_PHOTO);
    }

    /**
     * 裁剪照片
     *
     * @param activity
     * @param uri
     * @param outputFile
     */
    public static void cropPhoto(Activity activity, Uri uri, File outputFile) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        FileProviderUtils.setIntentDataAndType(activity, intent, "image/*", uri, true);
        intent.putExtra("crop", "true");
        if (Build.MANUFACTURER.contains("HUAWEI")) {
            intent.putExtra("aspectX", 9998);
            intent.putExtra("aspectY", 9999);
        } else {
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
        }
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        //return-data为true时，直接返回bitmap，可能会很占内存，不建议，小米等个别机型会出异常！！！
        //所以适配小米等个别机型，裁切后的图片，不能直接使用data返回，应使用uri指向
        //裁切后保存的URI，不属于我们向外共享的，所以可以使用fill://类型的URI
        Uri outputUri = Uri.fromFile(outputFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("return-data", false);

        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, REQUEST_CODE_CROP_PHOTO);
    }

}

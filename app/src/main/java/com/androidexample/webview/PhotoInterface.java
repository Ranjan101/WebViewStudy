package com.androidexample.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by PC on 10/27/2015.
 */
class PhotoInterface {
    Activity activity = null;

    PhotoInterface(Activity activity) {
        this.activity = activity;
    }

    void openCamera() {
        File baseDirectory = new File(Environment.getExternalStorageDirectory(), "video_thumb.png");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(baseDirectory));
        activity.startActivityForResult(cameraIntent, 1);
    }
}

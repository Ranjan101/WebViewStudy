package com.androidexample.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.File;

public class ShowWebView extends Activity {

    //private Button button;
    private WebView webView;

    final Activity activity = this;

    public Uri imageUri;

    private static final int FILECHOOSER_RESULTCODE = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;

    final int SELECT_GALLERY_PHOTO = 1;
    final int SELECT_CAMERA_PHOTO = 2;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.show_web_view);

        // Define url that will open in webview
        String webViewUrl = "http://projectsonseoxperts.net.au/godiner/live/public/test";

        //Get webview
        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setLoadWithOverviewMode(true);

        // Other webview settings
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportZoom(true);
        webView.addJavascriptInterface(new MyJavascriptInterface(this), "Android");

        webView.loadUrl("file:///android_asset/webdemo.html");


        //webView.loadUrl(webViewUrl);

    }

    class MyJavascriptInterface {

        Context mContext;

        /**
         * Instantiate the interface and set the context
         */
        MyJavascriptInterface(Context c) {
            mContext = c;
        }

        /**
         * Show a toast from the web page
         */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public String choosePhoto() {
            // TODO Auto-generated method stub
            String file = "test";
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_GALLERY_PHOTO);
            return file;
        }

        @JavascriptInterface
        public String openCamera() {
            String file = "test";
            File baseDirectory = new File(Environment.getExternalStorageDirectory(), "video_thumb.png");
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(baseDirectory));
            startActivityForResult(cameraIntent, SELECT_CAMERA_PHOTO);
            return file;
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case SELECT_GALLERY_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = intent.getData();
                    webView.loadUrl("javascript:setFileUri('" + selectedImage.toString() + "')");
                    String path = getRealPathFromURI(this, selectedImage);
                    webView.loadUrl("javascript:setFilePath('" + path + "')");
                }
                break;
            case SELECT_CAMERA_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri outputFileUri = null;
                    try {
                        outputFileUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                                "video_thumb.png"));
                        webView.loadUrl("javascript:setFileUri('" + outputFileUri.toString() + "')");
                        // String path = getRealPathFromURI(this, );
                        webView.loadUrl("javascript:setFilePath('" + outputFileUri.toString() + "')");
                    } catch (Exception e) {
                        Toast.makeText(ShowWebView.this, "Please Try Again", Toast.LENGTH_LONG).show();
                    }
                }
                break;

        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

}
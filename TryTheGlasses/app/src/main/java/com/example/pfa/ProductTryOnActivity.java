package com.example.pfa;

import android.Manifest;
import android.content.DialogInterface;
import android.icu.util.Measure;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class ProductTryOnActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
   private String desciption="";

    WebView myWebView;

    private String TAG = "TEST";
    private PermissionRequest mPermissionRequest;

    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final String[] PERM_CAMERA =
            {Manifest.permission.CAMERA};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_try_on);
        desciption=getIntent().getStringExtra("description");

        myWebView = (WebView) findViewById(R.id.webviewTryOn);
        //myWebView  = new WebView(this);
        //myWebView.setMinimumHeight(1000);

        // myWebView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDisplayMetrics().widthPixels,(int)(height *getResources().getDisplayMetrics().density)));



        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setAllowFileAccessFromFileURLs(true);
        myWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        //myWebView.setWebViewClient(new WebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            // Grant permissions for cam
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                Log.i(TAG, "onPermissionRequest");
                mPermissionRequest = request;
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {

                    mPermissionRequest.grant(new String[]{PermissionRequest.RESOURCE_VIDEO_CAPTURE});
                    Log.d(TAG,"Granted");
                }
            }

            @Override
            public void onPermissionRequestCanceled(PermissionRequest request) {
                super.onPermissionRequestCanceled(request);
                Toast.makeText(ProductTryOnActivity.this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        });


        if(hasCameraPermission()){

            myWebView.setWebViewClient(new WebViewClient());

            String test ="blaze_genral_or_bleudegrademiroir";
            String customHTML =
                    "<!DOCTYPE html>\n"+
                            "<html>\n" +
                            "  <head>\n" +
                            "<style>\n"+
                            "body {\n"+
                            "min-height: 100vh;\n"+
                            "}\n"+
                            "</style>\n"+
                            "    <meta charset='utf-8' />\n" +
                            "    <script type='text/javascript' src='https://appstatic.jeeliz.com/jeewidget/JeelizNNCwidget.js'></script>\n" +
                            "    <meta name=\"viewport\" content=\"width=device-width , initial-scale=1.0\">\n" +
                            "    <script type='text/javascript'>\n" +
                            "      function main() {\t\n" +
                            "        JEEWIDGET.start({\n" +
                            "          sku: '"+ desciption +"',\n" +
                            "          searchImageMask: 'https://appstatic.jeeliz.com/jeewidget/images/target.png',\n" +
                            "          searchImageColor: 0xff0000,\n" +
                            "        })\n" +
                            "      }\n" +
                            "    </script>\n" +
                            "    <link rel='stylesheet' href='css/JeeWidgetPublicGit.css' />\n" +
                            "  </head>\n" +
                            "\n" +
                            "  <body onload=\"main()\">\n" +
                            "    <main>\n" +
                            "      <div id='JeeWidget'>\n" +
                            "        <canvas id='JeeWidgetCanvas'></canvas>\n" +
                            "      </div>\n" +
                            "    </main>\n" +
                            "  </body>\n" +
                            "</html>\n";

            //myWebView.loadData(customHTML,null,null);
            myWebView.loadDataWithBaseURL("file:///android_assetnm/jeelizGlassesVTOWidget-master/", customHTML,null , null, null);
            //myWebView.loadUrl("https://www.google.com/");
            // myWebView.loadData(customHTML,"text/html","UTF-8");
            //myWebView.loadUrl("file:///android_asset/www/jeelizGlassesVTOWidget-master/demo.html");
            //setContentView(myWebView);

        }else{
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs access to your camera so you can take pictures.",
                    REQUEST_CAMERA_PERMISSION,
                    PERM_CAMERA);
        }

    }





    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(ProductTryOnActivity.this, PERM_CAMERA);
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myWebView != null){
            myWebView.destroy();
        }
    }
}
package com.example.fishclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Location extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        WebView webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient());
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setLoadWithOverviewMode(true);
//        webSettings.setUseWideViewPort(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(false);
//        webSettings.setSupportZoom(true);
//        webSettings.setDefaultTextEncodingName("utf-8");
//        webSettings.setGeolocationEnabled(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.setWebChromeClient(new WebChromeClient(){
//            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
//                // callback.invoke(String origin, boolean allow, boolean remember);
//                callback.invoke(origin, true, false);
//            }
//        });
//
//        webView.getSettings().setGeolocationDatabasePath( Location.this.getFilesDir().getPath() );
//
//        //webView.loadUrl("C:\\Users\\user\\AndroidStudioProjects\\FishClassification\\app\\src\\main\\assets\\index.html");
//       // webView.loadUrl("\\app\\src\\main\\assets\\i1.html");
        webView.loadUrl("file:///android_asset/i1.html");   //



    }
}
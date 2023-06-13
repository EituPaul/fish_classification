package com.example.fishclassification;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class LogOut extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);

        WebView webView = findViewById(R.id.webView);
        ImageView logoutbtn= findViewById(R.id.logoutbtn);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setGeolocationEnabled(true);
        //webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.getJavaScriptCanOpenWindowsAutomatically();

        webView.loadUrl("file:///android_asset/logout.html");   //
        LottieAnimationView logoutanim = findViewById(R.id.lottie_animation_logoutimg);
        logoutanim.playAnimation();
        logoutanim.loop(true);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); //signout firebase
                Intent setupIntent = new Intent(getApplicationContext(), MainActivity.class);
                Toast.makeText(getBaseContext(), "Logged Out", Toast.LENGTH_LONG).show(); //if u want to show some text
                setupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(setupIntent);
                finish();
            }
        });



    }//end on create
    @Override
    protected void onStart() {
        super.onStart();
        //if user not log in
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(LogOut.this,LogIn.class);
            Toast.makeText(getBaseContext(), " You are already logged Out", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }

    }
}
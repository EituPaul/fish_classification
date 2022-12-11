package com.example.fishclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
//variable

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_SCREEN = 4000;

    ImageView image;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        //find the widget of splash screen by id
        image=(ImageView) findViewById(R.id.imageView);
        textView =(TextView)findViewById(R.id.textView);

        //setting animation element

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        image.setAnimation(anim);
        textView.startAnimation(anim);
        //end animation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                intent = new Intent(getApplicationContext(),LogIn.class);
                startActivity(intent);
                finish();

            }
        },SPLASH_SCREEN);








    }
}
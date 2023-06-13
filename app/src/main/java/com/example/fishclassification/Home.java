package com.example.fishclassification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    CardView post,location,classify,about_us,profile,logOut,tips,userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        post = findViewById(R.id.post_card);
        location = findViewById(R.id.location_card);
        classify = findViewById(R.id.classify_card);
        about_us = findViewById(R.id.about_card);
        profile = findViewById(R.id.profile_card);
        logOut = findViewById(R.id.logout_card);
        tips = findViewById(R.id.Tips_card);
        userlist = findViewById(R.id.user_card);
        //start lottie anim
        LottieAnimationView coveranim = findViewById(R.id.lottie_animation_homecover);
        coveranim.playAnimation();
        coveranim.loop(true);
        LottieAnimationView coveranim1 = findViewById(R.id.lottie_animation_homecover1);
        coveranim1.playAnimation();
        coveranim1.loop(true);
        LottieAnimationView logoutanim = findViewById(R.id.lottie_animation_logoutimghome);
        logoutanim.playAnimation();
        logoutanim.loop(true);
        LottieAnimationView postanim = findViewById(R.id.lottie_animation_postimghome);
        postanim.playAnimation();
        postanim.loop(true);
        LottieAnimationView classifyanim = findViewById(R.id.lottie_animation_classifyimghome);
        classifyanim.playAnimation();
        classifyanim.loop(true);
        LottieAnimationView locationanim = findViewById(R.id.lottie_animation_locationimghome);
        locationanim.playAnimation();
        locationanim.loop(true);
        LottieAnimationView tipsanim = findViewById(R.id.lottie_animation_Tipsimghome);
        tipsanim.playAnimation();
        tipsanim.loop(true);
        LottieAnimationView profileanim = findViewById(R.id.lottie_animation_profileimghome);
        profileanim.playAnimation();
        profileanim.loop(true);
        LottieAnimationView userlistanim = findViewById(R.id.lottie_animation_userlistimghome);
        userlistanim.playAnimation();
        userlistanim.loop(true);
        LottieAnimationView aboutUsanim = findViewById(R.id.lottie_animation_aboutusimghome);
        aboutUsanim.playAnimation();
        aboutUsanim.loop(true);
        //end lottie anim
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i1);
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), Location.class);
                startActivity(i1);
            }
        });
        classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), Classify_Fish.class);
                startActivity(i1);
            }
        });
        about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), About_Us.class);
                startActivity(i1);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //if user not log in
                    if(FirebaseAuth.getInstance().getCurrentUser() == null){
                        Intent intent = new Intent(Home.this,LogIn.class);
                        Toast.makeText(getBaseContext(), " You are already logged Out", Toast.LENGTH_LONG).show();
                        startActivity(intent);
                        finish();


                }
                    else {
                        Intent i1 = new Intent(getApplicationContext(), Profile.class);
                        startActivity(i1);
                    }
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), LogOut.class);
                startActivity(i1);
            }
        });
        tips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), TipsInYoutubeVideo.class);
                startActivity(i1);
            }
        });
        userlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(getApplicationContext(), Pagination.class);
                startActivity(i1);
            }
        });




    }
}
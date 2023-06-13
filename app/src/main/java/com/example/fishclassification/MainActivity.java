package com.example.fishclassification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {

    TextView text;
    ImageView loadImage, btn1,btn2,btn3,startbtn;
    //card view animation
    Animation topAnim,bottomAnim;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.textviewTittle);
        loadImage= findViewById(R.id.imageView2);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        startbtn = findViewById(R.id.btnNext);
        cardView = findViewById(R.id.card);

        //cardview animation

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
       // bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        //setting animation time
        topAnim.setDuration(2000);
        //bottomAnim.setDuration(2000);


        //setting animation element
        cardView.setAnimation(topAnim);


        //for animation of btn 1,2,3;
        AnimatorSet scaleAnim2 = new AnimatorSet();
        AnimatorSet scaleAnim3 = new AnimatorSet();

        //setting animation for loadimage element//blinking

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        loadImage.setAnimation(anim);
       // text.startAnimation(anim);
        //end animation

        //setting animation for going to each button

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(btn1, "scaleX", 0.9f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(btn1, "scaleY", 0.9f, 1.1f);

        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);

        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet scaleAnim1= new AnimatorSet();
        scaleAnim1.setDuration(1000);
        scaleAnim1.play(scaleX).with(scaleY);

        scaleAnim1.start();
        // end setting animation for going to each button

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textforbtn2 = getString(R.string.btn2_text);
                text.setText(textforbtn2);
                loadImage.setImageResource(R.drawable.socialpost);
               //baki duita off
               scaleAnim1.end();
               scaleAnim3.end();
                //setting animation for going to each button2//zoom

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(btn2, "scaleX", 0.9f, 1.1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(btn2, "scaleY", 0.9f, 1.1f);

                scaleX.setRepeatCount(ObjectAnimator.INFINITE);
                scaleX.setRepeatMode(ObjectAnimator.REVERSE);

                scaleY.setRepeatCount(ObjectAnimator.INFINITE);
                scaleY.setRepeatMode(ObjectAnimator.REVERSE);

               // AnimatorSet scaleAnim2 = new AnimatorSet();
                scaleAnim2.setDuration(1000);
                scaleAnim2.play(scaleX).with(scaleY);

                scaleAnim2.start();
                // end setting animation for going to each button//zoom
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textforbtn3 = getString(R.string.btn3_text);
                text.setText(textforbtn3);
                loadImage.setImageResource(R.drawable.classifymain);
                //baki duita off
                scaleAnim1.end();
                scaleAnim2.end();
                //setting animation for going to each button2//zoom

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(btn3, "scaleX", 0.9f, 1.1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(btn3, "scaleY", 0.9f, 1.1f);

                scaleX.setRepeatCount(ObjectAnimator.INFINITE);
                scaleX.setRepeatMode(ObjectAnimator.REVERSE);

                scaleY.setRepeatCount(ObjectAnimator.INFINITE);
                scaleY.setRepeatMode(ObjectAnimator.REVERSE);

               // AnimatorSet scaleAnim3 = new AnimatorSet();
                scaleAnim3.setDuration(1000);
                scaleAnim3.play(scaleX).with(scaleY);

                scaleAnim3.start();
                // end setting animation for going to button3//zoom
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textforbtn1 = getString(R.string.btn1_text);
                text.setText(textforbtn1);
                loadImage.setImageResource(R.drawable.unlockimg);
                //baki duita off
                scaleAnim2.end();
                scaleAnim3.end();
                //setting animation for going to each button2//zoom

                ObjectAnimator scaleX = ObjectAnimator.ofFloat(btn1, "scaleX", 0.9f, 1.1f);
                ObjectAnimator scaleY = ObjectAnimator.ofFloat(btn1, "scaleY", 0.9f, 1.1f);

                scaleX.setRepeatCount(ObjectAnimator.INFINITE);
                scaleX.setRepeatMode(ObjectAnimator.REVERSE);

                scaleY.setRepeatCount(ObjectAnimator.INFINITE);
                scaleY.setRepeatMode(ObjectAnimator.REVERSE);

                // AnimatorSet scaleAnim3 = new AnimatorSet();
                scaleAnim1.setDuration(1000);
                scaleAnim1.play(scaleX).with(scaleY);

                scaleAnim1.start();
                // end setting animation for going to button3//zoom
            }
        });
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getApplicationContext(),Home.class);
                startActivity(intent);
                finish();
            }
        });


    }//end oncreate


   }

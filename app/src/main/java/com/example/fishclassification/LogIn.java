package com.example.fishclassification;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogIn extends AppCompatActivity {
    //variable
    public ImageView  proimage;
    public EditText signInEmailEditText, signInPasswordEditext;
    public Button signInButton;TextView SignUpButton;
    public ProgressBar signInprogress;
    public FirebaseAuth mAuth;
    String email,password;
    CheckBox checkBox;
    DatabaseReference databaseReference;//my part

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Profile");//mypart
        //finding by id
        proimage =(ImageView)findViewById(R.id.proimage);
        checkBox =(CheckBox) findViewById(R.id.checkbox);
        signInEmailEditText = (EditText) findViewById(R.id.useremailsignIn);
        signInPasswordEditext = (EditText) findViewById(R.id.passwordsignIn);
        signInButton = (Button) findViewById(R.id.login);
        SignUpButton = (TextView) findViewById(R.id.SignUpLogIn);


        //setting animation signupImage

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(proimage, "scaleX", 0.9f, 1.1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(proimage, "scaleY", 0.9f, 1.1f);

        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);

        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        AnimatorSet scaleAnim = new AnimatorSet();
        scaleAnim.setDuration(1000);
        scaleAnim.play(scaleX).with(scaleY);

        scaleAnim.start();

        //password show
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    signInPasswordEditext.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    signInPasswordEditext.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //end password show

        //for a new account
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ii = new Intent(getApplicationContext(), SignUp.class);
                startActivity(ii);
            }
        });



        //end animation
    }///end oncreate
}//appcompat class
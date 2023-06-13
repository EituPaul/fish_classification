package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogIn extends AppCompatActivity {
    //variable

    public EditText signInEmailEditText, signInPasswordEditext;
    public Button signInButton;TextView SignUpButton;
    public ProgressBar signInprogress;
    public FirebaseAuth mAuth;
    String email,password;
    CheckBox checkBox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        //finding by id

        checkBox =(CheckBox) findViewById(R.id.checkbox);
        signInEmailEditText = (EditText) findViewById(R.id.useremailsignIn);
        signInPasswordEditext = (EditText) findViewById(R.id.passwordsignIn);
        signInButton = (Button) findViewById(R.id.login);
        SignUpButton = (TextView) findViewById(R.id.SignUpLogIn);
        signInprogress = findViewById(R.id.loading);


//        //setting animation signupImage
//
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat(proimage, "scaleX", 0.9f, 1.1f);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat(proimage, "scaleY", 0.9f, 1.1f);
//
//        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
//        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
//
//        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
//        scaleY.setRepeatMode(ObjectAnimator.REVERSE);
//
//        AnimatorSet scaleAnim = new AnimatorSet();
//        scaleAnim.setDuration(1000);
//        scaleAnim.play(scaleX).with(scaleY);
//
//        scaleAnim.start();

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
        signInEmailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().isEmpty())
                {
                    signInEmailEditText.setError("Enter Your Email");


                }
                if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {

                    signInEmailEditText.setError("invalid");


                }





            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
        signInPasswordEditext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check password is empty or not?
                if (s.toString().isEmpty()) {
                    signInPasswordEditext.setError("Enter a password");

                }
                if (s.toString().length() < 6) {
                    signInPasswordEditext.setError("Minimum password length should be 6");

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(v.getId()==R.id.login)
//                {
//                    if(signInEmailEditText.getError()==null && signInEmailEditText.getText().length()!=0 &&
//                            signInPasswordEditext.getError()==null && signInPasswordEditext.getText().length()!=0
//                           ){
//
//                        userlogin();
//                        Log.d("Hi","kuki");
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(), "Fill up Correctly", Toast.LENGTH_LONG).show();
//
//                    }
//
//
//                }
                if(v.getId()==R.id.login)
                {
                    if(
                            signInEmailEditText.getError()==null && signInEmailEditText.getText().length()!=0 &&
                            signInPasswordEditext.getError()==null && signInPasswordEditext.getText().length()!=0
                             ){
                        Log.d("Ch", "Hi");
                        userlogin();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Fill up Correctly", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });




    }///end oncreate


    private void userlogin() {
        email = signInEmailEditText.getText().toString().trim();
        password = signInPasswordEditext.getText().toString().trim();


        signInprogress.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                signInprogress.setVisibility(View.GONE);
                if (task.isSuccessful()) {
//                    if(mAuth.getCurrentUser().isEmailVerified()){
//
//                        Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(),Home.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(intent);
//                        finish();
//                    }
//                    else if (!mAuth.getCurrentUser().isEmailVerified()){
//                        Toast.makeText(getApplicationContext(), "Email is not verified", Toast.LENGTH_LONG).show();
//                    }

                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                    //Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                    //no need to come this activity again







                } else {
                    Toast.makeText(getApplicationContext(), "Login unsuccesful", Toast.LENGTH_LONG).show();

                }
            }


        });



    }


}//appcompat class


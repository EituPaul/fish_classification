package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class SignUp<databaseReference> extends AppCompatActivity {
    EditText name,email,password,contact,dateofbirth;
    //ProgressBar loadingProgress;

    Calendar myCalendar;
    CheckBox checkBox;
    public Button signupButton,back;
    ImageView img;
    DatabaseReference databaseReference;


    //auth
    ProgressBar bar;
    FirebaseAuth mAuth;

//end auth



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        img = (ImageView)findViewById(R.id.textView3);
        email=(EditText)findViewById(R.id.emailid);
        myCalendar= Calendar.getInstance();
        dateofbirth = (EditText) findViewById(R.id.date);
        password =(EditText)findViewById(R.id.password);
        checkBox =(CheckBox) findViewById(R.id.checkbox);
        name = (EditText) findViewById(R.id.name);
        contact =(EditText)findViewById(R.id.contact);
        signupButton =(Button) findViewById(R.id.signup);
        back = (Button)findViewById(R.id.back);
        //auth
        bar=(ProgressBar)findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserData");//mypart
        //end auth
        //password show
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // show password
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    // hide password
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        //end password show
        //date calendar
        dateofbirth.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH,month);
                        myCalendar.set(Calendar.DAY_OF_MONTH,day);
                        updateLabel();

                    }
                };

                new DatePickerDialog(SignUp.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //end date calendar
        //for realtime check
        String name1 = name.getText().toString().trim();
        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        String contact1 = contact.getText().toString().trim();
        String dateofbirth1 =  dateofbirth.getText().toString().trim();



        // each edit text realtime check by addTextChangedListener starting
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        for(DataSnapshot snapshot : datasnapshot.getChildren()){
                            String namefromdatabase = snapshot.child("name").getValue().toString();
//                           Log.d("My",ma);
//                           Log.d("edit1",s.toString());
                            if(s.toString().isEmpty())
                            {
                                name.setError("Enter Your Name");


                            }
                           if(namefromdatabase.equals(s.toString()))
                           {
                               //Log.d("finaly","match");// ekhane ma == s.toString kaj kore nah
                               name.setError("Ooops!Name Has already been taken!");

                           }
                          else{

                           }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("UserData");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                        for(DataSnapshot snapshot : datasnapshot.getChildren()){
                            String emailfromdatabase = snapshot.child("email").getValue().toString();
//                           Log.d("My",ma);
//                           Log.d("edit1",s.toString());
                            if(s.toString().isEmpty())
                            {
                                email.setError("Enter Your Email");


                            }
                            if (!Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
//                    email.setError("Enter a valid email address");
//                    email.requestFocus();
//                    return;
                                email.setError("invalid");


                            }
                            if(emailfromdatabase.equals(s.toString()))
                            {
                                //Log.d("finaly","match");// ekhane ma == s.toString kaj kore nah
                                email.setError("Ooops!Email Has already been taken!");


                            }
                            else {


                            }

                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {


                    }
                });






            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check password is empty or not?
                if (s.toString().isEmpty()) {
                    password.setError("Enter a password");

                }
                if (s.toString().length() < 6) {
                    password.setError("Minimum password length should be 6");

                }
                else
                {
                    signupButton.setEnabled(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        contact.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((s.toString().length()!=11) ||
                        !(          (s.toString().startsWith("013") )
                                || (s.toString().startsWith("014") )
                                || (s.toString().startsWith("015") )
                                || (s.toString().startsWith("016") )
                                || (s.toString().startsWith("017") )
                                || (s.toString().startsWith("018") )
                                || (s.toString().startsWith("019") ) ) ){
                    contact.setError("invalid contact");

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dateofbirth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(s.toString().endsWith("15") ||
                        s.toString().endsWith("16") ||
                        s.toString().endsWith("17") ||
                        s.toString().endsWith("18") ||
                        s.toString().endsWith("19") ||
                        s.toString().endsWith("20") ||s.toString().endsWith("21") ||
                        s.toString().endsWith("22")  || s.toString().isEmpty()) ){
                    dateofbirth.setError(null);

                }
                else {
                    dateofbirth.setError("minimum year 8");

                }
//                try {
//                    Date date = new SimpleDateFormat("dd/MM/yy").parse(s.toString());
//                    // Year thisYear = Year.now();
//                    long age = 122- date.getYear();
//                    // Log.d("Tag",String.valueOf(LocalDate.MIN.getYear()));
////                    Log.d("Tag",String.valueOf(date.getYear()));
////                    Log.d("Tag",String.valueOf(age));
//
//
//                    if(!(age>=3))
//                    {
//                        Log.d("Tag",String.valueOf(age));
//                        dateofbirth.setError("minimum age should be 3");
//                    }
//                    else {
//
//                    }
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(v.getId()==R.id.signup)
                {
                    if(name.getError()==null && name.getText().length()!=0 &&
                            email.getError()==null && email.getText().length()!=0 &&
                            password.getError()==null && password.getText().length()!=0 &&
                            contact.getError()==null && contact.getText().length()!=0  &&
                            dateofbirth.getError()!=null && dateofbirth.getText().length()!=0 ){
                        Log.d("Ch", "Hi");
                        newusersignUp();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Fill up Correctly", Toast.LENGTH_LONG).show();
                    }


                }


            }
        });
        //end each edit text realtime check by addTextChangedListener


    }// after on create end
    //out of on create
    //call after signup button click
    private void newusersignUp(){

        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();
        bar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                bar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                   Toast.makeText(getApplicationContext(), "Register is Succesful", Toast.LENGTH_LONG).show();
                    saveData();
                    //pore add korbo
//                   mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if(task.isSuccessful()){
//                                Intent ii1 = new Intent(getApplicationContext(), Home.class);
//                                startActivity(ii1);
//                                finish();
//                                Toast.makeText(SignUp.this, "Registered Successfully,Please verify your email", Toast.LENGTH_LONG).show();
//                            }
//                            else{
//                                Toast.makeText(SignUp.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//                    });
//end pore add korbo
                    Intent ii1 = new Intent(getApplicationContext(), Home.class);
                    startActivity(ii1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Register is not Succesful", Toast.LENGTH_LONG).show();

                }
            }
        });


    }
    // end call after signup button click
    //called from newusersignUp() to send data in database
    public  void saveData()
    {

        String name1 = name.getText().toString().trim();
        String contact1 = contact.getText().toString().trim();
        String dateofbirth1 = dateofbirth.getText().toString().trim();

        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();


        String key =databaseReference.push().getKey();

        SignUpDataHandleForDatabase signUpDataHandleForDatabase = new SignUpDataHandleForDatabase(email1,password1,name1,contact1,dateofbirth1);

        databaseReference.child(mAuth.getUid()).setValue(signUpDataHandleForDatabase);

        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();

    }
    //end called from newusersignUp() to send data in database



    private void updateLabel(){
        String myFormat="MM/dd/yy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        dateofbirth.setText(dateFormat.format(myCalendar.getTime()));

    }


}//end appcompat class

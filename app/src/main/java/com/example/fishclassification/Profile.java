package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private  static final int REQUEST_CODE=101;

    EditText UserName,UserEmail,UserContact,UserDateOfBirth;
    DatabaseReference fetch_profile_data_reference;
    DatabaseReference add_image_uri_to_UserData_ref;
    FirebaseAuth firebaseAuth;
    FirebaseUser user ;
    StorageReference storeImageRef;
    CircleImageView profile_image;
    Button savebtn;
    Uri imageUri;
    ProgressDialog mLoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mLoadingBar = new ProgressDialog(this);
        profile_image =findViewById(R.id.profileLogo);
        savebtn = findViewById(R.id.btnSave);
        UserName = findViewById(R.id.inputUserName);
        UserEmail= findViewById(R.id.inputUserEmail);
        UserContact=findViewById(R.id.inputUserContact);
        UserDateOfBirth= findViewById(R.id.inputUserDateofBirth);

        add_image_uri_to_UserData_ref =FirebaseDatabase.getInstance().getReference().child("UserData");


        //EditText Enabling off
        UserName.setEnabled(false);
        UserEmail.setEnabled(false);

        //end EditText Enabling off
        // Starting Work of fetch Data From UserDate for Profile set Up
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String UserKey =user.getEmail();
        Log.d("data",UserKey);




        fetch_profile_data_reference = FirebaseDatabase.getInstance().getReference().child("UserData").child(user.getUid());

        fetch_profile_data_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {


                    String Username = datasnapshot.child("name").getValue().toString();
                    UserName.setText(Username);
                    String Useremail = datasnapshot.child("email").getValue().toString();
                    UserEmail.setText(Useremail);
                    String Usercontact = datasnapshot.child("contact").getValue().toString();
                    UserContact.setText(Usercontact);
                    String Userdateofbirth = datasnapshot.child("dateofbirth").getValue().toString();
                    UserDateOfBirth.setText(Userdateofbirth);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         // ending work of fetch Data From UserDate for Profile set Up

        //image
        storeImageRef = FirebaseStorage.getInstance().getReference().child("ProfileImage");
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);

            }
        });
        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });



    }//end OnCreate

    private void saveData() {
       String userName =UserName.getText().toString();
        String userEmail =UserEmail.getText().toString();
        String userContact =UserContact.getText().toString();
        String userdateofbirth =UserDateOfBirth.getText().toString();

        mLoadingBar.setTitle("adding setup profile");
        mLoadingBar.setCanceledOnTouchOutside(false);

        if(imageUri!=null &&
                UserName.getText().length()!=0 &&
                UserEmail.getText().length()!=0 &&
                UserContact.getText().length()!=0  &&
                UserDateOfBirth.getText().length()!=0 ) {
            mLoadingBar.show();
            storeImageRef.child(user.getUid()).putFile(imageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                storeImageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        HashMap hashMap = new HashMap();
                                        hashMap.put("name", userName);
                                        hashMap.put("email", userEmail);
                                        hashMap.put("contact", userContact);
                                        hashMap.put("dateofbirth", userdateofbirth);
                                        hashMap.put("profileImage", uri.toString());
                                        hashMap.put("status", "offline");

                                        // only image add korar jnno "UserData" te

                                        add_image_uri_to_UserData_ref.child(user.getUid()).
                                                updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        mLoadingBar.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Setup profile completed", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Profile.this, MainActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mLoadingBar.dismiss();
                                                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();

                                                    }
                                                });

                                    }
                                });
                            }

                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            profile_image.setImageURI(imageUri);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
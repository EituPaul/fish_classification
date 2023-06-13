package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fishclassification.Utils.Posts;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity {
    private  static final int REQUEST_CODE=101;


    EditText UserName,UserEmail,UserContact,UserDateOfBirth;
    DatabaseReference fetch_profile_data_reference,mUserReference;
    DatabaseReference add_image_uri_to_UserData_ref;
    FirebaseAuth firebaseAuth;
    FirebaseUser user ;
    StorageReference storeImageRef;
    CircleImageView profile_image;
    Button savebtn;
    Uri imageUri;
    ProgressDialog mLoadingBar;
    TextView districttextview;
    TextView thanatextview;
    String district,thana;
    //added auto set proimage

    String  profileImageUrlV;
    //end added auto set proimage

    //added for multiple drop down
    private String selectedState,selectedDistrict;
    TextView tvStateSpinner, tvDistrictSpinner;
    private Spinner stateSpinner, districtSpinner;
    private ArrayAdapter<CharSequence> stateAdapter;
    private ArrayAdapter<CharSequence> districtAdapter;

    // end added for multiple drop down
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Profile");
        mLoadingBar = new ProgressDialog(this);
        profile_image =findViewById(R.id.profileLogo);
        savebtn = findViewById(R.id.btnSave);
        UserName = findViewById(R.id.inputUserName);
        UserEmail= findViewById(R.id.inputUserEmail);
        UserContact=findViewById(R.id.inputUserContact);
        UserDateOfBirth= findViewById(R.id.inputUserDateofBirth);


        // spinner

            stateSpinner = findViewById(R.id.spinnerBangladeshState);
            stateAdapter = ArrayAdapter.createFromResource(this, R.array.array_indian_state, R.layout.spinner_layout);
            stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            stateSpinner.setAdapter(stateAdapter);
            stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    districtSpinner = findViewById(R.id.spinnerBangladeshDistrict);
                    selectedState = stateSpinner.getSelectedItem().toString();

                    int parentID = ((ViewGroup) view.getParent()).getId();
                    if (parentID == R.id.spinnerBangladeshState) {
                        switch (selectedState) {
                            case "Select your District":
                                districtAdapter = ArrayAdapter.createFromResource
                                        (((ViewGroup) view.getParent()).getContext(),
                                                R.array.array_default_districts, R.layout.layout);
                                break;
                            case "Chittagong":
                                districtAdapter = ArrayAdapter.createFromResource
                                        (((ViewGroup) view.getParent()).getContext(),
                                                R.array.Chittagong, R.layout.layout);
                                break;
                            case "Dhaka":
                                districtAdapter = ArrayAdapter.createFromResource
                                        (((ViewGroup) view.getParent()).getContext(),
                                                R.array.Dhaka, R.layout.layout);
                                break;
                            case "Rajshahi":
                                districtAdapter = ArrayAdapter.createFromResource
                                        (((ViewGroup) view.getParent()).getContext(),
                                                R.array.Rajshahi, R.layout.layout);
                                break;
                            default:
                                break;
                        }
                        try {
                            districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            districtSpinner.setAdapter(districtAdapter);
                        }catch (NullPointerException nullPointerException){
                            nullPointerException.printStackTrace();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        //end spinner



        //added auto set proimag

        add_image_uri_to_UserData_ref =FirebaseDatabase.getInstance().getReference().child("UserData");


        //end added auto set proimag


        //EditText Enabling off
       // UserName.setEnabled(false);
        UserEmail.setEnabled(false);

        //end EditText Enabling off
        // Starting Work of fetch Data From UserDate for Profile set Up
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        String UserKey =user.getEmail();
        Log.d("data",UserKey);




        fetch_profile_data_reference = FirebaseDatabase.getInstance().getReference().child("UserData").child(user.getUid());
        //auto image set
        mUserReference = FirebaseDatabase.getInstance().getReference().child("UserData");
        //end auto image set


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
                    //added for auto set proimage
//                    String imageUriV = datasnapshot.child("profileImage").getValue().toString();
//                   Picasso.get().load(imageUriV).into(profile_image);

               try {
                   //added for fetch spinner data from database
                   if (datasnapshot.child("district").exists()) {
                       //districttextview= findViewById(R.id.districttextview);

                        district = datasnapshot.child("district").getValue().toString();
                       //districttextview.setText(district);
                       stateSpinner.setSelection(stateAdapter.getPosition(district));

                   }

                   if (datasnapshot.child("thana").exists()) {
                       thanatextview = findViewById(R.id.thanatextview);
                       thana = datasnapshot.child("thana").getValue().toString();

                       thanatextview.setText(thana);
                       Toast.makeText(Profile.this, thana, Toast.LENGTH_SHORT).show();

                   }

               }catch (Exception e){
                   e.printStackTrace();
               }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         // ending work of fetch Data From UserDate for Profile set Up

//        //add for auto image

        mUserReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    if(dataSnapshot.child("profileImage").exists()){

                        profileImageUrlV = dataSnapshot.child("profileImage").getValue().toString();

                        Log.d("im",profileImageUrlV);

                        Picasso.get().load(profileImageUrlV).into(profile_image);

                    }
                    else{

                        Toast.makeText(Profile.this, "Select an Image", Toast.LENGTH_LONG).show();



                    }




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Profile.this,"sorry",Toast.LENGTH_SHORT).show();

            }
        });
//        //end added for auto image



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
        //added for spinner
        String state = stateSpinner.getSelectedItem().toString();
        String district = districtSpinner.getSelectedItem().toString();

        Toast.makeText(getApplicationContext(), state + " " + district, Toast.LENGTH_SHORT).show();
        //end added for spinner

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

                                        //added for spinner
                                        hashMap.put("district", state);
                                        hashMap.put("thana", district);

                                        //end added for spinner

                                        // only image add korar jnno "UserData" te

                                        add_image_uri_to_UserData_ref.child(user.getUid()).
                                                updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        mLoadingBar.dismiss();
                                                        Toast.makeText(getApplicationContext(), "Setup profile completed", Toast.LENGTH_LONG).show();
                                                        Intent intent = new Intent(Profile.this, Home.class);
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



                            }//if clause end

                        }
                    });
        }
        else{
            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_LONG).show();
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
      try {
          if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
              imageUri = data.getData();
              profile_image.setImageURI(imageUri);

          }

          super.onActivityResult(requestCode, resultCode, data);
      }catch (Exception e){
          e.printStackTrace();
      }
    }
}
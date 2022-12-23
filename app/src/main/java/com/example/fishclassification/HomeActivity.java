package com.example.fishclassification;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.fishclassification.Utils.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.cli.Option;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.cli.Options;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private  static final int REQUEST_CODE=101;


    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;  
    FirebaseAuth mAuth_for_navigation_user_mail;
    FirebaseUser mUser;
    DatabaseReference mUserReference,postRef;
    String profileImageUrlV ,userNameV;//for fetch from database
    CircleImageView profileImageViewHeader;
    TextView userNameHeader;

    //this is for posting
    ImageView addImageToPost,sendImagePost;
    EditText inputPostDescription;
    Uri imageUri;
    ProgressDialog mloadingbar;
    StorageReference postImageStorageRef;
    RecyclerView recyclerView;


    //these are for fetching posts data from database
    FirebaseRecyclerAdapter<Posts,MyViewHolder>adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        //appbar title
        getSupportActionBar().setTitle("Finding Nemo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

        //initialeze for add post to firebase database
        addImageToPost = findViewById(R.id.addImage);
        sendImagePost = findViewById(R.id.sendPost);
        inputPostDescription = findViewById(R.id.inputAddPost);
        mloadingbar = new ProgressDialog(this);

        //for fetching image post
        recyclerView = findViewById(R.id.recyclerViewPosts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Initialize firebase auth and database

        mAuth_for_navigation_user_mail = FirebaseAuth.getInstance();
        mUser = mAuth_for_navigation_user_mail.getCurrentUser();
        mUserReference = FirebaseDatabase.getInstance().getReference().child("UserData");
        //add post and image uri to 'Posts' firebase
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");//also be used to fetch postdata
        //add image to firebase storage
        postImageStorageRef = FirebaseStorage.getInstance().getReference().child("PostImages");




        //for navdrawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        View view = navigationView.inflateHeaderView(R.layout.drawer_header);
           //for setting proimage and user name in nav Header with view
        profileImageViewHeader = view.findViewById(R.id.profileImageHeader);
        userNameHeader = view.findViewById(R.id.userNameHeader);
        ////for setting proimage and user name in nav Header with view
        navigationView.setNavigationItemSelectedListener(this);

        //if we click imageview then it will take pic from gallery
        addImageToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE);

            }
        });

        //if we click send button we fetch image and edittextdata so setonclicklistener is needed
        sendImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost();

            }
        });

        loadPost();




    }//end oncreate
    //for fetching post data to recyclerview
    private void loadPost() {

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build(); //chamge from video
        adapter = new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
                holder.postDesc.setText(model.getPostDesc());
                holder.timeAgo.setText(model.getPostDate());
                holder.username.setText(model.getUserName());
                Picasso.get().load(model.getPostImageUri()).into(holder.postImage);
                Picasso.get().load(model.getUserProfileImage()).into(holder.profileImage);

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_post,parent,false);
                return new MyViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    //for fetch image from gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==REQUEST_CODE && resultCode == RESULT_OK && data!=null){
            imageUri = data.getData();
            addImageToPost.setImageURI(imageUri);

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    //this is being called from setonclicklistener Method
    private void addPost() {

       String postDesc = inputPostDescription.getText().toString();
       if(postDesc.isEmpty())
       {
           inputPostDescription.setError("Please write something");

       } else if (imageUri == null) {

           Toast.makeText(HomeActivity.this, "Please add an image", Toast.LENGTH_SHORT).show();
       }
       else
       {
           //progress diaglo show korbe sob input neya thakle and send button click korle
           mloadingbar.setTitle("Adding Post");
           mloadingbar.setCanceledOnTouchOutside(false);
           mloadingbar.show();
           Date date = new Date();
           SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
           String   strDate = formatter.format(date);
           postImageStorageRef.child(mUser.getUid()+strDate).putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
               @Override
               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                   if(task.isSuccessful()){
                       postImageStorageRef.child(mUser.getUid()+strDate).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                           @Override
                           public void onSuccess(Uri uri) {

                               HashMap hashMap = new HashMap();
                               hashMap.put("PostDate",strDate);
                               hashMap.put("PostImageUri",uri.toString());
                               hashMap.put("PostDesc",postDesc);
                               hashMap.put("UserName",userNameV);
                               hashMap.put("userProfileImage",profileImageUrlV);//navbar e j profile imageUri ache seta post e jabe
                               postRef.child(mUser.getUid()+strDate).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                   @Override
                                   public void onComplete(@NonNull Task task) {
                                       if(task.isSuccessful())
                                       {
                                          // post data databse e gele loading dialog dismiss hobe
                                           mloadingbar.dismiss();
                                           Toast.makeText(HomeActivity.this, "Post Added", Toast.LENGTH_SHORT).show();
                                           addImageToPost.setImageResource(R.drawable.ic_baseline_post_image_24);
                                           inputPostDescription.setText(null);
                                       }
                                       else{
                                           Toast.makeText(HomeActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });


                           }
                       });
                   }
                   else{
                       mloadingbar.dismiss();
                       Toast.makeText(HomeActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                   }

               }
           });
       }

    }
    //check if userExistOr Not


    @Override
    protected void onStart() {
        super.onStart();
        //if user not log in
        if(mUser == null){
            sendUserToLoginActivity();
        }
        else{
            mUserReference.child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        profileImageUrlV = dataSnapshot.child("profileImage").getValue().toString();
                        userNameV = dataSnapshot.child("name").getValue().toString();

                        Picasso.get().load(profileImageUrlV).into(profileImageViewHeader);
                        userNameHeader.setText(userNameV);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(HomeActivity.this,"sorry",Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    private void sendUserToLoginActivity() {
        Intent intent = new Intent(HomeActivity.this,LogIn.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.home:
                Intent ii1 = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(ii1);

                break;
            case R.id.profile:
                Intent ii2 = new Intent(getApplicationContext(), Profile.class);
                startActivity(ii2);
                break;
            case R.id.classify:
                Intent ii3 = new Intent(getApplicationContext(), Classify_Fish.class);
                startActivity(ii3);

                break;
            case R.id.location:
                Intent ii4 = new Intent(getApplicationContext(), Location.class);
                startActivity(ii4);

                break;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
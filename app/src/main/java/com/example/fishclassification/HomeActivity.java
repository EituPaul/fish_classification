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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
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


import com.example.fishclassification.Utils.Comments;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private  static final int REQUEST_CODE=101;


    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;  
    FirebaseAuth mAuth_for_navigation_user_mail;
    FirebaseUser mUser;
    DatabaseReference mUserReference,postRef,likeRef,dislikeRef,commentRef;//added dislike ref
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
    //for comment fetch
    //FirebaseRecyclerOptions<Comments>CommentOption;//ekhane Comments  holo class
    FirebaseRecyclerAdapter<Comments,CommenViewHolder>CommentAdapter;


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

       //for like and like count
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        // //for dislike and dislike count
        dislikeRef = FirebaseDatabase.getInstance().getReference().child("disLikes");

      //for comment and comment cout
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

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

        FirebaseRecyclerOptions <Posts>options = new FirebaseRecyclerOptions.Builder<Posts>().setQuery(postRef,Posts.class).build(); //chamge from video
        adapter = new FirebaseRecyclerAdapter<Posts, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Posts model) {
                final String postKey = getRef(position).getKey();//posts er j id te data pathabe seta
                holder.postDesc.setText(model.getPostDesc());
               //calculate time ago from date time
                String timeAgo = calculateTimeAgo(model.getPostDate());
                holder.timeAgo.setText(timeAgo);
                holder.username.setText(model.getUserName());
                Picasso.get().load(model.getPostImageUri()).into(holder.postImage);
                Picasso.get().load(model.getUserProfileImage()).into(holder.profileImage);
                //like count
                holder.countLikes(postKey,mUser.getUid(),likeRef);
                //dislike count
                holder.countDislikes(postKey,mUser.getUid(),dislikeRef);

                //adding like  feature
                holder.likeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //likeRef er mddhe j id(node) te data pathabe seta child er mddhe thakbe
                        likeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //if user already like this post we remove that like
                                if(snapshot.exists())
                                {
                                    likeRef.child(postKey).child(mUser.getUid()).removeValue();
                                    holder.likeImage.setColorFilter(Color.GRAY);
                                  adapter.notifyDataSetChanged();//ekhane
                                }
                                else{
                                    // start  myLogic jodi dislike kore user taile  like click korar somoy dislike k remove korbe
                                    dislikeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //if user already like this post we remove that like
                                            if(snapshot.exists())
                                            {
                                                dislikeRef.child(postKey).child(mUser.getUid()).removeValue();
                                                holder.dislikeImage.setColorFilter(Color.GRAY);
                                                adapter.notifyDataSetChanged();//ekhane
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    // end  myLogic jodi dislike kore user taile  like click korar somoy dislike k remove korbe
                                //if user not like this post yet, we add that like
                                    likeRef.child(postKey).child(mUser.getUid()).setValue("like");
                                    holder.likeImage.setColorFilter(Color.BLUE);
                                  adapter.notifyDataSetChanged();//ekhane first e adapter chara disilam

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });

                //adding dislike feature
                holder.dislikeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //dislikeRef er mddhe j id(node) te data pathabe seta child er mddhe thakbe
                        dislikeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //if user already like this post we remove that like
                                if(snapshot.exists())
                                {
                                    dislikeRef.child(postKey).child(mUser.getUid()).removeValue();
                                    holder.dislikeImage.setColorFilter(Color.GRAY);
                                    adapter.notifyDataSetChanged();//ekhane
                                }
                                else{
                                    //eta hocche jodi like diye thak user tahole dislike dite gele like k gray (remove kore) dibe
                                    likeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            //if user already like this post we remove that like
                                            if(snapshot.exists())
                                            {
                                                likeRef.child(postKey).child(mUser.getUid()).removeValue();
                                                holder.likeImage.setColorFilter(Color.GRAY);
                                                adapter.notifyDataSetChanged();//ekhane
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    //if user not like this post yet, we add that like
                                    dislikeRef.child(postKey).child(mUser.getUid()).setValue("dislike");
                                    holder.dislikeImage.setColorFilter(Color.RED);
                                    adapter.notifyDataSetChanged();//ekhane first e adapter chara disilam

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(HomeActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                });
                    //comment send
                holder.CommentSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get data from input Field
                        String comment = holder.inputComment.getText().toString();
                        if(comment.isEmpty()){
                            Toast.makeText(HomeActivity.this, "Please write something", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            addComment(holder,postKey,commentRef,mUser.getUid(),comment);
                        }

                    }
                });
                //comment fetch
                LoadComment(postKey);

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
    //comment fetch to recycler view through single_view_comment_layout
    private void LoadComment(String postKey) {
        MyViewHolder.recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        Log.d("innerLoad","hei");

        FirebaseRecyclerOptions CommentOption = new FirebaseRecyclerOptions.Builder<Comments>().setQuery(commentRef.child(postKey),Comments.class).build();
        CommentAdapter = new FirebaseRecyclerAdapter<Comments, CommenViewHolder>(CommentOption) {

            @Override
            protected void onBindViewHolder(@NonNull CommenViewHolder holder, int position, @NonNull Comments model) {
               Picasso.get().load(model.getProfileImageUrl()).into(holder.profileImage);
               holder.username.setText(model.getUserName());
               holder.comments.setText(model.getComment());
                Log.d("BinnerLoad","hi");



            }

            @NonNull
            @Override
            public CommenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_comment,parent,false);
                Log.d("BinnerLoad","hi");
                return new CommenViewHolder(view);

            }
        };

        CommentAdapter.startListening();
        MyViewHolder.recyclerView.setAdapter(CommentAdapter);
    }
    //comment send
    private void addComment(MyViewHolder holder, String postKey, DatabaseReference commentRef, String uid, String comment) {
        HashMap hashMap = new HashMap();
        hashMap.put("UserName",userNameV);
        hashMap.put("ProfileImageUrl",profileImageUrlV);
        hashMap.put("comment",comment);
        //each postkey (each post) er jnno many user(userid wise) comment jabe
        commentRef.child(postKey).child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(HomeActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    holder.inputComment.setText(null);
                }
                else{
                    Toast.makeText(HomeActivity.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    ////calculate time ago from post date firebase
    private String calculateTimeAgo(String postDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        try {
            long time = sdf.parse(postDate).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =

                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
              return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";

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
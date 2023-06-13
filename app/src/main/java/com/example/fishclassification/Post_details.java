package com.example.fishclassification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fishclassification.Utils.Comments;
import com.example.fishclassification.Utils.Posts;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Post_details extends AppCompatActivity {

    ImageView postImage, likeImage, commentImage,  CommentSend, dislikeImage;
    CircleImageView ProfileImage;
    TextView userName, timeAgo, postDesc, likeCounter, totalComment, dislikeCounter,Count_avg_rating;
    EditText inputComment;
    RatingBar RatingBar ;
    FirebaseAuth mAuth;
    DatabaseReference postRef, likeRef, commentRef, dislikeRef, ratingRef;
    StorageReference postImageStorageRef;
    FirebaseUser mUser;
    String profileImageUrl, postImageUrl, usernamestring, postDate, postdesc, postKey, uid;//for fetch from database


    RecyclerView commentRecyclerView;
    FirebaseStorage storage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ProfileImage = findViewById(R.id.details_post_profileImagePost);
        postDesc = findViewById(R.id.details_post_postDescription);
        userName = findViewById(R.id.details_post_TextProfileUsernamePost);
        timeAgo = findViewById(R.id.details_post_timeAgo);
        likeImage = findViewById(R.id.details_post_like);
        dislikeImage = findViewById(R.id.details_post_dislike_btn);
        likeCounter = findViewById(R.id.details_post_likeCount);
        dislikeCounter = findViewById(R.id.details_post_dislike_counter);
        totalComment = findViewById(R.id.details_post_commentcounter);
        postImage = findViewById(R.id.details_post_postImage);
        inputComment = findViewById(R.id.details_post_commentEdittext);
        CommentSend = findViewById(R.id.details_post_sendComments);
        Count_avg_rating = findViewById(R.id.details_post_avgrating);
        RatingBar = findViewById(R.id.details_post_ratingBar);
        commentRecyclerView = findViewById(R.id.details_post_recyclerViewComments);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        uid=mUser.getUid();
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");//also be used to fetch postdata
        postImageStorageRef = FirebaseStorage.getInstance().getReference().child("PostImages");
        likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        dislikeRef = FirebaseDatabase.getInstance().getReference().child("disLikes");
        ratingRef = FirebaseDatabase.getInstance().getReference().child("ratings");
        commentRef = FirebaseDatabase.getInstance().getReference().child("Comments");

        //fetch from homeactivity
        Intent intent = getIntent();
        postKey = intent.getExtras().getString("postKey");
        //end fetch from homeactivity
        try {
            postRef.child(postKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child("userProfileImage").exists()) {
                        profileImageUrl = dataSnapshot.child("userProfileImage").getValue().toString();
                        Picasso.get().load(profileImageUrl).into(ProfileImage);

                    }
                    if (dataSnapshot.child("PostImageUri").exists()) {
                        postImageUrl = dataSnapshot.child("PostImageUri").getValue().toString();
                        Picasso.get().load(postImageUrl).into(postImage);

                    }

                    if (dataSnapshot.child("UserName").exists()) {
                        usernamestring = dataSnapshot.child("UserName").getValue().toString();
                        userName.setText(usernamestring);

                    }
                    if (dataSnapshot.child("PostDate").exists()) {
                        postDate = dataSnapshot.child("PostDate").getValue().toString();
                        timeAgo.setText(postDate);

                    }
                    if (dataSnapshot.child("PostDesc").exists()) {
                        postdesc = dataSnapshot.child("PostDesc").getValue().toString();
                        postDesc.setText(postdesc);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(Post_details.this, error.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            CommentSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get data from input Field
                    String comment = inputComment.getText().toString();
                    if(comment.isEmpty()){
                        Toast.makeText(Post_details.this, "Please write something", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        addComment(postKey,commentRef,mUser.getUid(),comment);
                    }

                }
            });
            LoadComment(postKey);

            likeImage.setOnClickListener(new View.OnClickListener() {
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
                                likeImage.setColorFilter(Color.GRAY);

                            }
                            else{
                                // start  myLogic jodi dislike kore user taile  like click korar somoy dislike k remove korbe
                                dislikeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        //if user dislike already this post we remove that dislike
                                        if(snapshot.exists())
                                        {
                                            dislikeRef.child(postKey).child(mUser.getUid()).removeValue();
                                            dislikeImage.setColorFilter(Color.GRAY);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Post_details.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                                // end  myLogic jodi dislike kore user taile  like click korar somoy dislike k remove korbe
                                //if user not like this post yet, we add that like
                                likeRef.child(postKey).child(mUser.getUid()).setValue("like");
                                likeImage.setColorFilter(Color.BLUE);


                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Post_details.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
            dislikeImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //dislikeRef er mddhe j id(node) te data pathabe seta child er mddhe thakbe
                    dislikeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //if user already dislike this post we remove that dislike
                            if(snapshot.exists())
                            {
                                dislikeRef.child(postKey).child(mUser.getUid()).removeValue();
                                dislikeImage.setColorFilter(Color.GRAY);

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
                                            likeImage.setColorFilter(Color.GRAY);

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(Post_details.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                                //if user not like this post yet, we add that like
                                dislikeRef.child(postKey).child(mUser.getUid()).setValue("dislike");
                                //Start dislike dile oi user er rating remove korbe and star gulo null kore dibe real time e
                                ratingRef.child(postKey).child(mUser.getUid()).removeValue();

                                RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                    @Override
                                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                        ratingBar.setRating(0);
                                    }
                                });
                                //End dislike dile oi user er rating remove korbe and star gulo white kore dibe
                                dislikeImage.setColorFilter(Color.RED);



                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Post_details.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
            RatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    String rat1 = String.valueOf(rating);
                    Log.d("rating",rat1);
                    //jodi dislike thak seta remove kore dibe
                    dislikeRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //jodi dislike na thak tobe rate korte parbe
                            if(!snapshot.exists())
                            {

                                dislikeImage.setColorFilter(Color.GRAY);


                                //dislike remove korar por rating set korbe
                                ratingRef.child(postKey).child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                            Drawable drawable=    ratingBar.getProgressDrawable();
//                                            drawable.setColorFilter((Color.parseColor("#EAB410")), PorterDuff.Mode.SRC_ATOP);

                                        ratingRef.child(postKey).child(mUser.getUid()).setValue(String.valueOf(rating));

                                        Toast.makeText(Post_details.this, "Your Response is taken", Toast.LENGTH_SHORT).show();


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                            }
                            //jodi dislike  thak tobe rating database e jabe na mane count hobe nah and color gray kore dibe
                            else{

                              RatingBar.setRating(0);
//                                    Drawable drawable=    ratingBar.getProgressDrawable();
//                                    drawable.setColorFilter((Color.parseColor("#808080")), PorterDuff.Mode.SRC_ATOP);
                                Toast.makeText(Post_details.this, "Rating can't be taken when disliked", Toast.LENGTH_SHORT).show();

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Post_details.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


                }
            });


            countLikes(postKey, uid, likeRef);
            countDislikes(postKey, uid, dislikeRef);
            countComments(postKey, uid, commentRef);
            countAvgrating(postKey,uid,ratingRef);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }//end oncreate
    private void LoadComment(String postKey) {
        FirebaseRecyclerAdapter<Comments,CommenViewHolder>CommentAdapter;
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Log.d("innerLoad","hei");

        FirebaseRecyclerOptions CommentOption = new FirebaseRecyclerOptions.Builder<Comments>().setQuery(commentRef.child(postKey),Comments.class).build();
        CommentAdapter = new FirebaseRecyclerAdapter<Comments, CommenViewHolder>(CommentOption) {

            @Override
            protected void onBindViewHolder(@NonNull CommenViewHolder holder, int position, @NonNull Comments model) {
                Picasso.get().load(model.getProfileImageUrl()).into(holder.profileImage);
                holder.username.setText(model.getUserName());
                holder.comments.setText(model.getComment());
                Log.d("BinnerLoad","hi");

                Log.d("postkey",postKey);
                Log.d("postcomment",model.getComment());

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
        commentRecyclerView.setAdapter(CommentAdapter);
    }

    private void countAvgrating(String postKey, String uid, DatabaseReference ratingRef) {
        try {
            ratingRef.child(postKey).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //then userId= snapshot value
                    //jodi user thak thn rating count korbe
                    if (snapshot.exists()) {
                        Log.d("snapshot", snapshot.getValue().toString());
                        //userId jotogulo count korbe
                        int totalUserRated = (int) snapshot.getChildrenCount();//total rating koijon user dise


                        double avgrate = 0.0;

                        //datasnapshotvalue = each userId er rating(each snapshot (uid) er value)
                        for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                            Log.d("datasnap", datasnapshot.getValue().toString().trim());
                            //loop protita user er rating mibe.rating ache uid er value hisebe;
                            String fetch_rating = datasnapshot.getValue().toString().trim();//sob uid  thek rate value fetch korbe
                            Log.d("fetch rating", fetch_rating);
                            double fetch_rating_double = Double.parseDouble(fetch_rating);
                            avgrate = avgrate + fetch_rating_double;//each value jog korbe
                        }


                        double avgrate2 = ((avgrate) / totalUserRated);
                        // RatingBar.setRating((float )avgrate2);

                        String str = String.valueOf(avgrate2);

                        Count_avg_rating.setText(str);

                    } else {
                        Count_avg_rating.setText("0.0");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addComment(String postKey, DatabaseReference commentRef, String uid, String comment) {
        //added by me for a user various comment
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
        String   strDatecomment = formatter.format(date);
        //end added by me for a user various comment
        HashMap hashMap = new HashMap();
        hashMap.put("UserName",usernamestring);
        hashMap.put("ProfileImageUrl",profileImageUrl);
        hashMap.put("comment",comment);
        //each postkey (each post) er jnno many user(userid wise) comment jabe
        commentRef.child(postKey).child(uid+strDatecomment).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful()){
                    Toast.makeText(Post_details.this, "Comment added", Toast.LENGTH_SHORT).show();
                     inputComment.setText(null);
                }
                else{
                    Toast.makeText(Post_details.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void countLikes(String postKey, String uid, DatabaseReference likeRef) {
        //only for like count under each postkey(posts)
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //postkey hocche protita post er id.proti postkey te joto gulo note seta
                //hocche getchildren orthat each user id .jodi snapshot orthat postkey te
                //user id thak getChildrenCount() koita ase seta ber korbe
                //snapshot hocche postId mane postkey .snapshot thakle tar children (user)count korbe
                if (snapshot.exists()) {
                    int totalLikes = (int) snapshot.getChildrenCount();
                    likeCounter.setText(totalLikes + "");
                } else {
                    likeCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        likeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //ar snapshot child mane post(postkey) er user thakle like image blue otherwise gray
                if(snapshot.child(uid).exists()){
                    likeImage.setColorFilter(Color.BLUE);
                }
                else{
                    likeImage.setColorFilter(Color.GRAY);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void countDislikes(String postKey, String uid, DatabaseReference dislikeRef) {
        //only for like count under each postkey(posts)
        dislikeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //postkey hocche protita post er id.proti postkey te joto gulo note seta
                //hocche getchildren orthat each user id .jodi snapshot orthat postkey te
                //user id thak getChildrenCount() koita ase seta ber korbe
                //snapshot hocche postId mane postkey .snapshot thakle tar children (user)count korbe
                if(snapshot.exists()){
                    int totaldisLikes = (int) snapshot.getChildrenCount();
                    dislikeCounter.setText(totaldisLikes+"");
                }
                else {
                    dislikeCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dislikeRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //ar snapshot child mane post(postkey) er user thakle like image blue otherwise gray
                if(snapshot.child(uid).exists()){
                    dislikeImage.setColorFilter(Color.RED);
                }
                else{
                    dislikeImage.setColorFilter(Color.GRAY);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void countComments(String postKey, String uid, DatabaseReference commentRef) {
        //only for like count under each postkey(posts)
        commentRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //postkey hocche protita post er id.proti postkey te joto gulo note seta
                //hocche getchildren orthat each user id .jodi snapshot orthat postkey te
                //user id thak getChildrenCount() koita ase seta ber korbe
                //snapshot hocche postId mane postkey .snapshot thakle tar children (user)count korbe
                if(snapshot.exists()){
                    int totalComments = (int) snapshot.getChildrenCount();
                    Log.d("cmntcount","1");
                    totalComment.setText(totalComments+"");
                }
                else {
                    totalComment.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

}
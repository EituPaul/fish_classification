package com.example.fishclassification;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
     CircleImageView profileImage,detetepostbutton;
     ImageView postImage,likeImage,dislikeImage,commentsImage,CommentSend;
     TextView username,timeAgo,postDesc,likeCounter,dislikeCounter,commentCounter;
     EditText inputComment;
     //added for rating
    RatingBar ratingBarholderclass ;
    TextView Count_avg_rating_holder;
    //end added for rating
    //for details post
    CardView post_detail_card_inholderclass ;
    Double AVGRateholder;
    LottieAnimationView Likeanim,disLikeanim;

     // comment fetch
    public static RecyclerView recyclerViewcomment;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImagePost);
        postImage = itemView.findViewById(R.id.postImage);
        username = itemView.findViewById(R.id.TextProfileUsernamePost);
        timeAgo = itemView.findViewById(R.id.timeAgo);
        postDesc = itemView.findViewById(R.id.postDescription);
        likeImage = itemView.findViewById(R.id.like);
        //added by me for dislike
        dislikeImage = itemView.findViewById(R.id.dislike_btn);

        commentsImage = itemView.findViewById(R.id.comment);
        likeCounter = itemView.findViewById(R.id.likeCount);
        //added by me for dislike
        dislikeCounter =itemView.findViewById(R.id.dislike_counter);
        commentCounter = itemView.findViewById(R.id.commentcounter);
        //CommentSend = itemView.findViewById(R.id.sendComments);
       // inputComment = itemView.findViewById(R.id.commentEdittext);
        //added for rating
        ratingBarholderclass = itemView.findViewById(R.id.ratingBar);
        Count_avg_rating_holder = itemView.findViewById(R.id.avgrating);
        //end added for rating
        //post details
        post_detail_card_inholderclass = itemView.findViewById(R.id.post_card);
        detetepostbutton=itemView.findViewById(R.id.deletebtnpost);
        Likeanim= itemView.findViewById(R.id.likesanim);
        disLikeanim= itemView.findViewById(R.id.dislikesanim);

        //comment fetch in this view holder
      //  recyclerViewcomment = itemView.findViewById(R.id.recyclerViewComments);
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
                if(snapshot.exists()){
                    int totalLikes = (int) snapshot.getChildrenCount();
                    likeCounter.setText(totalLikes+"");
                }
                else {
                    likeCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //for dislikeCount method is called from home


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
                    int totalLikes = (int) snapshot.getChildrenCount();
                    dislikeCounter.setText(totalLikes+"");
                }
                else {
                    dislikeCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //for dislikeCount method is called from home


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
     //likecount o dislike count er moto rating count korbe
    public void countAvgrating(String postKey, String uid, DatabaseReference ratingRef) {
                  //postkey table e jabe
        ratingRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               //then userId= snapshot value
                //jodi user thak thn rating count korbe
                if(snapshot.exists()){
                    Log.d("snapshot",snapshot.getValue().toString());
                      //userId jotogulo count korbe
                    int totalUserRated = (int) snapshot.getChildrenCount();//total rating koijon user dise



                    double  avgrate=0.0;

                    //datasnapshotvalue = each userId er rating(each snapshot (uid) er value)
                    for(DataSnapshot datasnapshot : snapshot.getChildren()){
                        Log.d("datasnap",datasnapshot.getValue().toString().trim());
                          //loop protita user er rating mibe.rating ache uid er value hisebe;
                        String fetch_rating=datasnapshot.getValue().toString().trim();//sob uid  thek rate value fetch korbe
                        Log.d("fetch rating",fetch_rating);
                        double fetch_rating_double =Double.parseDouble(fetch_rating);
                        avgrate =avgrate + fetch_rating_double;//each value jog korbe
                    }


                    double avgrate2 =((avgrate)/totalUserRated);

                        ratingBarholderclass.setRating((float)avgrate2);
                        String str = String.valueOf(avgrate2);

                     Count_avg_rating_holder.setText(str);

                }
                else {
                    Count_avg_rating_holder.setText("0.0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    // added by me for comment count
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
                    commentCounter.setText(totalComments+"");
                }
                else {
                    commentCounter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }
    //end added by me for comment count
}

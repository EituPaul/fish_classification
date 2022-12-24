package com.example.fishclassification;

import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
     CircleImageView profileImage;
     ImageView postImage,likeImage,dislikeImage,commentsImage,CommentSend;
     TextView username,timeAgo,postDesc,likeCounter,dislikeCounter,commentCounter;
     EditText inputComment;

     // comment fetch
    public static RecyclerView recyclerView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImagePost);
        postImage = itemView.findViewById(R.id.postImage);
        username = itemView.findViewById(R.id.TextProfileUsernamePost);
        timeAgo = itemView.findViewById(R.id.timeAgo);
        postDesc = itemView.findViewById(R.id.postDescription);
        likeImage = itemView.findViewById(R.id.like);
        //added by me
        dislikeImage = itemView.findViewById(R.id.dislike_btn);

        commentsImage = itemView.findViewById(R.id.comment);
        likeCounter = itemView.findViewById(R.id.likeCount);
        //added by me
        dislikeCounter =itemView.findViewById(R.id.dislike_counter);
        commentCounter = itemView.findViewById(R.id.commentcounter);
        CommentSend = itemView.findViewById(R.id.sendComments);
        inputComment = itemView.findViewById(R.id.commentEdittext);
        //comment fetch in this view holder
        recyclerView = itemView.findViewById(R.id.recyclerViewComments);
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
}

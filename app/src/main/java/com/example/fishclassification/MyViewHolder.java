package com.example.fishclassification;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyViewHolder extends RecyclerView.ViewHolder {
     CircleImageView profileImage;
     ImageView postImage,likeImage,commentsImage;
     TextView username,timeAgo,postDesc,likeCounter,commentCounter;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImagePost);
        postImage = itemView.findViewById(R.id.postImage);
        username = itemView.findViewById(R.id.TextProfileUsernamePost);
        timeAgo = itemView.findViewById(R.id.timeAgo);
        postDesc = itemView.findViewById(R.id.postDescription);
        likeImage = itemView.findViewById(R.id.like);
        commentsImage = itemView.findViewById(R.id.comment);
        likeCounter = itemView.findViewById(R.id.likeCount);
        commentCounter = itemView.findViewById(R.id.commentcounter);
    }
}

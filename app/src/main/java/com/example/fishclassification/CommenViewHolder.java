package com.example.fishclassification;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommenViewHolder  extends RecyclerView.ViewHolder {
    CircleImageView profileImage;
    TextView username,comments;

    public CommenViewHolder(@NonNull View itemView) {
        super(itemView);
        profileImage = itemView.findViewById(R.id.profileImage_comment);
        username = itemView.findViewById(R.id.usernameComment);
        comments = itemView.findViewById(R.id.commentmsg);

    }
}

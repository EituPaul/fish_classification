package com.example.fishclassification.Utils;

public class Comments {

   private String ProfileImageUrl,UserName,comment;

    public Comments() {
    }

    public String getProfileImageUrl() {
        return ProfileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        ProfileImageUrl = profileImageUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Comments(String profileImageUrl, String userName, String comment) {
        ProfileImageUrl = profileImageUrl;
        UserName = userName;
        this.comment = comment;
    }
}

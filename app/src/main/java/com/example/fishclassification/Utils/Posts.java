package com.example.fishclassification.Utils;

public class Posts {

    private String PostDate,PostDesc,PostImageUri,UserName,userProfileImage;
    public Posts(){

    }

    public Posts(String postDate, String postDesc, String postImageUri, String userName, String userProfileImage) {
        PostDate = postDate;
        PostDesc = postDesc;
        PostImageUri = postImageUri;
        UserName = userName;
        this.userProfileImage = userProfileImage;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public String getPostDesc() {
        return PostDesc;
    }

    public void setPostDesc(String postDesc) {
        PostDesc = postDesc;
    }

    public String getPostImageUri() {
        return PostImageUri;
    }

    public void setPostImageUri(String postImageUri) {
        PostImageUri = postImageUri;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }
}

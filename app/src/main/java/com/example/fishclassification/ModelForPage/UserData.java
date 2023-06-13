package com.example.fishclassification.ModelForPage;

public class UserData {
    private String name,email;

    public UserData(){

    }

    public UserData(String name, String email, String dateofbirth, String password, String contact) {
        this.name = name;
        this.email = email;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




}

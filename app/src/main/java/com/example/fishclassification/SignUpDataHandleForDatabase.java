package com.example.fishclassification;

public class SignUpDataHandleForDatabase {

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email,password,name,contact,dateofbirth;

    public SignUpDataHandleForDatabase(String email, String password,String name, String contact,String dateofbirth) {
        this.email = email;
        this.password = password;
        this.name =name;
        this.contact=contact;
        this.dateofbirth=dateofbirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact= contact;
    }
    public String getDateofbirth() {
        return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
        this.dateofbirth= dateofbirth;
    }


}

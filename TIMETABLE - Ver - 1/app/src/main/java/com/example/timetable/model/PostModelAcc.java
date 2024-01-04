package com.example.timetable.model;

import com.google.gson.annotations.SerializedName;

public class PostModelAcc {
    @SerializedName("Email")
    private String Email;
    @SerializedName("Password")
    private String Password;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public PostModelAcc(String email, String password) {
        Email = email;
        Password = password;
    }
}

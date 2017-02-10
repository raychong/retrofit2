package com.pentagon.retrofitexample.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by RayChongJH on 9/2/17.
 */

public class User implements Serializable {
    @SerializedName("username")
    String username;
    @SerializedName("password")
    String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

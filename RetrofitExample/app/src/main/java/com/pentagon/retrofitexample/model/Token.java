package com.pentagon.retrofitexample.model;

import java.io.Serializable;

/**
 * Created by RayChongJH on 8/2/17.
 */

public class Token implements Serializable {
    int accountId;
    String accessToken;
    String refreshToken;


    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

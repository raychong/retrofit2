package com.pentagon.retrofitexample.http;

import com.pentagon.retrofitexample.model.HttpResult;
import com.pentagon.retrofitexample.model.Token;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface GetTokenService {

    @FormUrlEncoded
    @POST("auth/token")
    Observable<HttpResult<Token>> getToken(@Field("username")String  username, @Field("password")String  password);
}

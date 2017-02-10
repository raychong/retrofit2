package com.pentagon.retrofitexample.http;

import com.pentagon.retrofitexample.model.GoList;
import com.pentagon.retrofitexample.model.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface GoListService {


    @Headers({"x-version:2"})
    @GET("golist/featured-inspiration")
    Observable<HttpResult<List<GoList>>> getWorldFeaturedGoList(@Header("Authorization")String accessToken, @Query("limit") int limit, @Query("page") int page);
}

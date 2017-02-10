package com.pentagon.retrofitexample.http;

import com.pentagon.retrofitexample.model.City;
import com.pentagon.retrofitexample.model.HttpResult;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface GetLocationService {


    @Headers({"x-version:2"})
    @GET("search/city")
    Observable<HttpResult<City>> getCurrentCityByLatLng(@Header("Authorization") String accessToken,
                                                        @Query("lat") double lat, @Query("lng") double png);
}

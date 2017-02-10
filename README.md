Retrofit2 with RxJava sample project
-Implemented butterknife

For GET http request
	@Headers({"x-version:2"}) 
	@GET("golist/featured-inspiration")
	getWorldFeaturedGoList(@Header("Authorization")String accessToken, @Query("limit") int limit, @Query("page") int page);

For POST http request
	@FormUrlEncoded
 	@POST("auth/token")
  getToken(@Field("username")String  username, @Field("password")String  password);
	
You may refer to this doc for others type of request, http://square.github.io/retrofit/

Note:
You may set the universal header for http request in httpMethod class.
     builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                Request original = chain.request();
                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("x-version", "2");
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });
				

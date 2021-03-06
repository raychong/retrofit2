package com.pentagon.retrofitexample.http;


import com.pentagon.retrofitexample.model.City;
import com.pentagon.retrofitexample.model.GoList;
import com.pentagon.retrofitexample.model.HttpResult;
import com.pentagon.retrofitexample.model.Token;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class HttpMethods {

//    public static final String BASE_URL = "https://api.upnixt.com";
    public static final String BASE_URL = "https://api.upnixt.com/";
    public static final String GOLIST_URL = BASE_URL+"/golist/";

    private static final int DEFAULT_TIMEOUT = 60;

    private Retrofit retrofit;
    private GoListService goListService;
    private GetTokenService getTokenService;
    private GetLocationService getLocationService;

    private HttpMethods() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
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
//        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
//                .cipherSuites(
//                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
//                        CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
//                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
//                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384
//                        )
//                .build();
//        builder.connectionSpecs(Collections.singletonList(spec));

//        try{
////            // Create a trust manager that does not validate certificate chains
//            final TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509TrustManager() {
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
//                        }
//
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                            return null;
//                        }
//                    }
//            };
//
//            // Install the all-trusting trust manager
//            final SSLContext sslContext = SSLContext.getInstance("TLSv1");
//            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
//            // Create an ssl socket factory with our all-trusting manager
//            SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslContext.getSocketFactory());
//            builder.socketFactory(NoSSLv3Factory);
//            builder.hostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String hostname, SSLSession session) {
//                        return true;
//                    }
//                });
//        }catch (Exception e){
//            e.printStackTrace();
//        }



        /** Add logging **/
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();


    }

    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
         o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * @param <T>   Subscriber need this type
     * This portion is for result processing
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getStatus() != 200) {
                throw new ApiException(httpResult.getStatus());
            }
            return httpResult.getData();
        }
    }

    /**
     * Get World Featured Go List
     * @param subscriber  is pass from observer
     * @param limit is for dataset size per request
     * @param page is for page count
     */
    public void getWorldFeaturedGoList(Subscriber<List<GoList>> subscriber,String accessToken, int limit, int page){
        goListService = retrofit.create(GoListService.class);
        Observable observable = goListService.getWorldFeaturedGoList(accessToken,limit, page)
                .map(new HttpResultFunc<List<GoList>>());
        toSubscribe(observable, subscriber);
    }

    public void getToken(Subscriber<Token> subscriber, String appId, String appSecret){
        getTokenService = retrofit.create(GetTokenService.class);
        Observable observable = getTokenService.getToken(appId,appSecret)
                .map(new HttpResultFunc<Token>());

        toSubscribe(observable, subscriber);
    }

    public void getCityName(Subscriber<City> subscriber,String accessToken, Double lat, Double lng){
        getLocationService = retrofit.create(GetLocationService.class);
        Observable observable = getLocationService.getCurrentCityByLatLng(accessToken,lat,lng)
                .map(new HttpResultFunc<City>());

        toSubscribe(observable, subscriber);
    }


}

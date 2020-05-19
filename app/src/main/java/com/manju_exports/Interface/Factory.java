package com.manju_exports.Interface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Factory {
    //This is for SK
    public static final String BASE_URL = "http://skyrand.in/exports/api/v1/";
    //This is for SK
//    public static final String BASE_URL = "http://192.168.2.3/exportsapis/api/v1/";
    //This is for Skyrand
//    public static final String BASE_URL = "http://192.168.31.245/exportsapis/api/v1/";

    public static APIInterface getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .writeTimeout(30, TimeUnit.MINUTES)
                .addNetworkInterceptor(interceptor)
                .build();

        return new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .baseUrl(Factory.BASE_URL)
                .build()
                .create(APIInterface.class);
    }
}

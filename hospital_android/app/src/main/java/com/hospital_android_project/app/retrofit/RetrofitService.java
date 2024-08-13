package com.hospital_android_project.app.retrofit;


import com.google.gson.Gson;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }

    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://172.20.10.10:8080") // base url for api endpoints
                .addConverterFactory(GsonConverterFactory.create(new Gson())) // convert json to java obj using gson
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}

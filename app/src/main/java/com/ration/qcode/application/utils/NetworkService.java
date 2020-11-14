package com.ration.qcode.application.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ration.qcode.application.utils.internet.AddProductAPI;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkService {
    private static NetworkService mInstance;

    private Retrofit mRetrofit;

    private NetworkService(String BASE_URL) {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static NetworkService getInstance(String BASE_URL) {
        mInstance = new NetworkService(BASE_URL);
        return mInstance;
    }


    public AddProductAPI getJSONApi() {
        return mRetrofit.create(AddProductAPI.class);
    }

}

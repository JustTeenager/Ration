package com.ration.qcode.application.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
        if (mInstance==null)
        mInstance = new NetworkService(BASE_URL);
        return mInstance;
    }

    public <T> T getApi(Class<T> apiClass){
        return mRetrofit.create(apiClass);
    }

    public void changeRetrofitUrl(String BASE_URL){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        mRetrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }


    //public AddProductAPI getJSONApi() {
       // return mRetrofit.create(AddProductAPI.class);
    //}

    /*public AddMenuAPI getMenuJSONApi(){
        return mRetrofit.create(AddMenuAPI.class);
    }*/

    /*public AddDateAPI getDateJSONApi(){
        return mRetrofit.create(AddDateAPI.class);
    }*/

    /*public AddMenuDateAPI getMenuDateJSONApi(){
        return mRetrofit.create(AddMenuDateAPI.class);
    }*/

    /*public AddComplicatedAPI getComplicatedApi(){return mRetrofit.create(AddComplicatedAPI.class);}*/
}

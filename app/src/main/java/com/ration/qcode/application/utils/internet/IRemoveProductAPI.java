package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IRemoveProductAPI {
    @FormUrlEncoded
    @POST("/removeProduct.php")
    Call<String> removeProduct(@Field("Name") String name);
}

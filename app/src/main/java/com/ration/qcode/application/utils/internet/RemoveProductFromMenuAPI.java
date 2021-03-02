package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RemoveProductFromMenuAPI {
    @FormUrlEncoded
    @POST("/removeProductFromMenu.php")
    Call<String> removeProductFromMenu(@Field("menu") String menu, @Field("date") String date, @Field("product") String product);
}
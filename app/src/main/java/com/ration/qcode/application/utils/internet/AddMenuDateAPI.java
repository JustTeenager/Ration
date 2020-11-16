package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddMenuDateAPI {
    @FormUrlEncoded
    @POST("/insertdatemenu.php")
    Call<AddProductResponse> insertDateMenu(@Field("menu") String menu, @Field("date") String date);
}
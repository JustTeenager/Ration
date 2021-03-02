package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface DeleteFromDateAPI {

    @FormUrlEncoded
    @POST("/removeFromDate.php")
    Call<String> removeDate(@Field("date") String date);
}

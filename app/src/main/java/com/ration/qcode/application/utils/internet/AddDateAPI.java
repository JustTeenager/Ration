package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddDateAPI {
    @FormUrlEncoded
    @POST("/insertdate.php")
    Call<String> insertDate (@Field("date") String date);
}

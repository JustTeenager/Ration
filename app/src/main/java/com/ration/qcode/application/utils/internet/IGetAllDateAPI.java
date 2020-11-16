package com.ration.qcode.application.utils.internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetAllDateAPI {
    @GET("/querydate.php")
    Call<List<DateResponse>> getAllDate();
}

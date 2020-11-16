package com.ration.qcode.application.utils.internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetAllMenuDateAPI {
    @GET("/querydatemenu.php")
    Call<List<DateMenuResponse>> getAllMenuDate();
}

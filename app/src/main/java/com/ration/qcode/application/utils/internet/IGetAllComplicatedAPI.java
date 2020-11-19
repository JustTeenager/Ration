package com.ration.qcode.application.utils.internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IGetAllComplicatedAPI {


    @GET("/querycomplicateds.php")
    Call<List<ComplicatedResponse>> queryComplicated();
}

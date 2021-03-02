package com.ration.qcode.application.utils.internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by deepdev on 12.04.17.
 */

public interface IGetAllDataAPI {
    @GET("/queryall.php")
    Call<List<TasksResponse>> getAllTasks();

}

package com.ration.qcode.application.utils.internet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by deepdev on 12.04.17.
 */

public interface ISearchAPI {
    @GET("/query.php")
    Call<List<TasksResponse>> getAllTasks(@Query("name") String name);

}

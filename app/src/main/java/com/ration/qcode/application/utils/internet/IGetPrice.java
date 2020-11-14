package com.ration.qcode.application.utils.internet;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by deepdev on 26.04.17.
 */

public interface IGetPrice {
    @GET("/price.php")
    Call<PriceResponse> getPrice();
}

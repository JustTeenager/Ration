package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UpdateGrAPI {
    @FormUrlEncoded
    @POST("/updateComplicatedGr.php")
    Call<String> updateGrApi(@Field("Name") String name, @Field("product") String product, @Field("Jiry") String jiry, @Field("Belki") String belki,
                                               @Field("Uglevod") String uglevod, @Field("FA") String fa, @Field("kkal") String kl, @Field("gram") String gram);
}

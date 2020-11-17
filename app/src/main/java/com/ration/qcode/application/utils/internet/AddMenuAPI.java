package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AddMenuAPI {

    @FormUrlEncoded
    @POST("/insertmenu.php")
    Call<AddProductResponse> insertProduct (@Field("menu") String menu, @Field("date") String date,
                                            @Field("product") String product, @Field("Jiry") String jiry, @Field("Belki") String belki,
                                            @Field("Uglevod") String uglevod,@Field("FA") String fa,@Field("kkal") String kl, @Field("gram") String gram, @Field("complicated")  String complicated);
}
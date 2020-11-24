package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Влад
 */

public interface AddProductAPI {

    @FormUrlEncoded
    @POST("/insertproduct.php")
    Call<AddProductResponse> insertProduct (@Field("name") String name, @Field("FA") String FA,
                                            @Field("KKAL") String KKAL, @Field("Belok") String Belok,
                                            @Field("Uglevod") String Uglevod, @Field("Jiry") String Jiry, @Field("Gram") String gram, @Field("complicated")  String complicated);

}
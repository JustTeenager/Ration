package com.ration.qcode.application.utils.internet;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author Влад
 */

public interface AddProductAPI {
//    @POST("/insertproduct.php")
//    Call<AddProductResponse> insertProduct(@Body AddProductBody addProductBody);

    @FormUrlEncoded
    @POST("/insertproduct.php")
    Call<AddProductResponse> insertProduct (@Field("name") String name, @Field("FA") String FA,
                                            @Field("KKAL") String KKAL, @Field("Belok") String Belok,
                                            @Field("Uglevod") String Uglevod, @Field("Jiry") String Jiry);

}

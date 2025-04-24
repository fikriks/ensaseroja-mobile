package com.algoritma.melani_AE6.api;

import com.algoritma.melani_AE6.model.Response;
import com.algoritma.melani_AE6.model.ResponseHome;
import retrofit2.Call;
//import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ResponseAPI {
    @FormUrlEncoded
    @POST("c_api")
    Call<Response> getData(
            @Field("enc") String enc
    );

    @GET("getDataHome")
    Call<ResponseHome> getDataHome();
}
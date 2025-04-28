package com.algoritma.melani_AE6.api;

import com.algoritma.melani_AE6.model.DataRes;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ResponseAPI {
    // Mengindikasikan request menggunakan format URL encoded
    @FormUrlEncoded
    // Endpoint API untuk request POST
    @POST("c_api")
    // Method untuk mendapatkan data dari API
    Call<DataRes> getData(
            // Parameter yang dikirim berupa string encoded
            @Field("enc") String enc
    );
}
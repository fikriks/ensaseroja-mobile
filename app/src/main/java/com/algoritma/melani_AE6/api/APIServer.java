package com.algoritma.melani_AE6.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServer {
    private static final String baseURL = "https://ensaseroja.my.id/";
    private static Retrofit retro;

    public static Retrofit konekRetrofit(){
        if (retro == null){
            retro = new Retrofit.Builder().baseUrl(baseURL).addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retro;
    }
}

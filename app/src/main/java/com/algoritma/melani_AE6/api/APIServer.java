package com.algoritma.melani_AE6.api;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIServer {
    // URL dasar untuk API server
    private static final String baseURL = "https://ensaseroja.my.id/";
    // Instance Retrofit yang akan digunakan secara global
    private static Retrofit retro;

    // Method untuk mendapatkan instance Retrofit
    public static Retrofit konekRetrofit(){
        // Cek apakah instance Retrofit sudah ada
        if (retro == null){
            // Jika belum, buat instance baru Retrofit
            retro = new Retrofit.Builder()
                    .baseUrl(baseURL)  // Set base URL
                    .addConverterFactory(GsonConverterFactory.create())  // Tambahkan converter GSON
                    .build();  // Build instance Retrofit
        }
        // Kembalikan instance Retrofit
        return retro;
    }
}

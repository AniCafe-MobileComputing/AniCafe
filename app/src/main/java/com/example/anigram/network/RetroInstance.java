package com.example.anigram.network;

import retrofit2.Retrofit;

//Retrofit Instanz Deklarierung
public class RetroInstance {

    private static Retrofit retrofit;

    public static Retrofit getRetroClient() {

        final String BASE_URL = "https://api.jikan.moe/v4/";

        if(retrofit == null ) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .build();
        }
        return retrofit;
    }
}

package com.example.anigram.network;

import android.content.Context;
import android.widget.ImageView;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

//Der API Service, welches für die API-Bibliothek Retrofit wichtig ist.
//Hier werden alle API Verbindungen festgehalten. (Abgesehen der API Requests die vom MAL4J Wrapper kommen)
public interface APIService {

    @GET("top/anime")
    Call<ResponseBody> getTrendingAnime();

    @GET("seasons/now")
    Call<ResponseBody> getSeasonalAnime();

    @GET("random/anime")
    Call<ResponseBody> getRandomAnime();

    @GET("anime/{animeId}/reviews")
    Call<ResponseBody> getReviewsForAnime(@Path(value = "animeId", encoded = true) String animeId);

    @GET("users/{username}/friends")
    Call<ResponseBody> getFriendsForUser(@Path(value = "username", encoded = true) String username);

    //API URL die notwendig für unsere GIF sind. (Im Anime-Details als Hintergrund)
    @GET("https://api.giphy.com/v1/gifs/search?api_key=96BcmE840FDCORuS5A10UeBgT5EZK3fE&limit=20")
    Call<ResponseBody> getDetailsGif(@Query("q") String query);

}

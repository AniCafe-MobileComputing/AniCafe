package com.example.anigram.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.network.APIService;
import com.example.anigram.network.RetroInstance;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ViewModel für das HomeFragment
public class HomeViewModel extends ViewModel {

    //Deklarierung der Variablen
    private APIService apiService;
    private MutableLiveData<ArrayList<CustomAnimeObject>> trendings;
    private MutableLiveData<ArrayList<CustomAnimeObject>> seasonals;

    //Konstruktur
    public HomeViewModel(){
        apiService = RetroInstance.getRetroClient().create(APIService.class);
        trendings = new MutableLiveData<>();
        seasonals = new MutableLiveData<>();
    }

    //Getter
    public MutableLiveData<ArrayList<CustomAnimeObject>> getTrendings() {
        return trendings;
    }

    public MutableLiveData<ArrayList<CustomAnimeObject>> getSeasonals() {
        return seasonals;
    }

    //Methode um API durch Retrofit aufzurufen.
    public void makeApiCalls() {
        //Setzt die beiden Calls welche notwendig sind -> für die beiden Listen im HomeFragment
        Call<ResponseBody> callSeasonal = apiService.getSeasonalAnime();
        Call<ResponseBody> callTrending = apiService.getTrendingAnime();

        callSeasonal.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                ArrayList<CustomAnimeObject> array = new ArrayList<CustomAnimeObject>();
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray standardArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < standardArray.length(); i++) {

                        JSONObject object = standardArray.getJSONObject(i);
                        String imageUrl = object.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");
                        String title = object.getJSONArray("titles").getJSONObject(0).getString("title");
                        Long id = object.getLong("mal_id");

                        CustomAnimeObject customAnimeObject = new CustomAnimeObject(imageUrl,title,id);
                        array.add(customAnimeObject);
                    }
                    seasonals.postValue(array); //Setzt den gebauten Array in die MutableLiveData Variable.
                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                seasonals.postValue(null);
            }
        });

        callTrending.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                ArrayList<CustomAnimeObject> array = new ArrayList<CustomAnimeObject>();
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray standardArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < standardArray.length(); i++) {
                        JSONObject object = standardArray.getJSONObject(i);
                        String imageUrl = object.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");
                        String title = object.getJSONArray("titles").getJSONObject(0).getString("title");
                        Long id = object.getLong("mal_id");

                        CustomAnimeObject customAnimeObject = new CustomAnimeObject(imageUrl,title,id);
                        array.add(customAnimeObject);
                    }
                    trendings.postValue(array); //Setzt den gebauten Array in die MutableLiveData Variable.
                } catch (Exception e) {
                    Log.d("ERROR", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                trendings.postValue(null);
            }
        });
    }
}
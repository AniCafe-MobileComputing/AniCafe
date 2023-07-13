package com.example.anigram.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.network.APIService;
import com.example.anigram.network.RetroInstance;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ViewModel für die RandomAnimeActivity
public class RandomAnimeViewModel extends ViewModel {

    //Deklarierung der Variablen
    private APIService apiService;
    private MutableLiveData<CustomAnimeObject> randomAnime;

    public RandomAnimeViewModel() {
        randomAnime = new MutableLiveData<CustomAnimeObject>();
        apiService = RetroInstance.getRetroClient().create(APIService.class);
    }

    public MutableLiveData<CustomAnimeObject> getRandomAnime() {
        return randomAnime;
    }

    //Methode um API durch Retrofit aufzurufen.
    public void makeApiCall() {
        //Setzt den Call vom APIService
        Call<ResponseBody> callRandomAnime = apiService.getRandomAnime();

        callRandomAnime.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONObject object = jsonObject.getJSONObject("data");
                    Long id = object.getLong("mal_id");
                    String title = object.getString("title");
                    String synopsis = object.getString("synopsis");
                    String image = object.getJSONObject("images").getJSONObject("jpg").getString("large_image_url");

                    randomAnime.postValue(new CustomAnimeObject(id, title, synopsis, image)); //Setzt das gebaute CustomAnimeObject in die MutableLiveData.
                } catch (Exception e) {
                    Log.d("error", e.toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }


}

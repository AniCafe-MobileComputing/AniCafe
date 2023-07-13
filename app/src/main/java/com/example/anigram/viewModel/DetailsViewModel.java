package com.example.anigram.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anigram.network.APIService;
import com.example.anigram.network.RetroInstance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import dev.katsute.mal4j.anime.Anime;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ViewModel für AnimeDetailActivity
public class DetailsViewModel extends ViewModel {

    //Variablen
    private APIService apiService;
    private MutableLiveData<String> gif_url;
    private Anime anime = null;

    //Konstruktur
    public DetailsViewModel(){
        apiService = RetroInstance.getRetroClient().create(APIService.class);
        gif_url = new MutableLiveData<>();
    }

    //Getter
    public MutableLiveData<String> getGifUrl() {
        return gif_url;
    }

    public Anime getAnime() { return anime; }

    //Setter
    public void setAnime(Anime anime) { this.anime = anime; }

    //Methode um API durch Retrofit aufzurufen.
    public void makeApiCall(String query) {

        //Ruft API-Link aus APIService und gibt einen Wert mit -> jetzigen AnimeNamen als Query für die API
        Call<ResponseBody> callGif = apiService.getDetailsGif(query.replace(" ", "+"));

        callGif.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Kriegt durch die Response die URL für das GIF.
                String url = null;
                JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray standardArray = jsonObject.getJSONArray("data");
                        JSONArray shuffledArray = shuffleJsonArray(standardArray);

                        for (int i = 0; i < standardArray.length(); i++) {
                            JSONObject object = shuffledArray.getJSONObject(i);
                            String slug = object.getString("slug").toLowerCase();
                            try {
                                if(slug.contains("crunchyroll") || slug.contains("xbox") || slug.contains("funimation") || slug.contains("toei") || slug.contains("iqiyi") || slug.contains("animation") || slug.contains("pixel") || slug.contains(query.split(":")[0].toLowerCase())){
                                    url = object.getJSONObject("images").getJSONObject("original").getString("url");
                                    gif_url.postValue(url);
                                    break;
                                }
                            } catch (Exception e) {continue;}
                        }
                    } catch (Exception ex) {}
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

    private JSONArray shuffleJsonArray (JSONArray array) throws JSONException {
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            Object object = array.get(j);
            array.put(j, array.get(i));
            array.put(i, object);
        }
        return array;
    }

}

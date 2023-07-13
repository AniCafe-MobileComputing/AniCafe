package com.example.anigram.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anigram.model.Friend;
import com.example.anigram.model.Reaction;
import com.example.anigram.model.Review;
import com.example.anigram.network.APIService;
import com.example.anigram.network.RetroInstance;
import com.example.anigram.utils.GlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import dev.katsute.mal4j.anime.Anime;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ViewModel für FriendsFragment
public class FriendsViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Friend>> friends;
    private APIService apiService;

    public FriendsViewModel() {
        apiService = RetroInstance.getRetroClient().create(APIService.class);
        friends = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Friend>> getFriends() {
        return friends;
    }

    //Methode um API durch Retrofit aufzurufen.
    public void makeApiCall() {
        //Ruft API-Link aus APIService.
        Call<ResponseBody> callRandomAnime = apiService.getFriendsForUser(GlobalVariables.authenticatedUser.getName());

        callRandomAnime.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //Baut durch die Response die Friends Array.
                ArrayList<Friend> array = new ArrayList<Friend>();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray standardArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < standardArray.length(); i++) {

                        JSONObject object = standardArray.getJSONObject(i);
                        String imageUrl = object.getJSONObject("user").getJSONObject("images").getJSONObject("jpg").getString("image_url");
                        String username = object.getJSONObject("user").getString("username");

                        array.add(new Friend(imageUrl, username));
                    }
                    friends.postValue(array); //Setzt Friends Array in die MutableLiveData.
                }catch (Exception e) {
                    Log.d("error", e.toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }


}

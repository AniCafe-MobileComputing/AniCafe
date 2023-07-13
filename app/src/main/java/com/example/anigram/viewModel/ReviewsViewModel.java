package com.example.anigram.viewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.model.Reaction;
import com.example.anigram.model.Review;
import com.example.anigram.network.APIService;
import com.example.anigram.network.RetroInstance;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//ViewModel für das ReviewFragment
public class ReviewsViewModel extends ViewModel {

    //Deklarierung der Variablen

    //String der Bezeichnungen der Reaktion auf ein Review
    private String[] reactionStrings = new String[] {"overall", "nice", "love_it", "funny", "confusing", "informative", "well_written", "creative"};
    private APIService apiService;
    private MutableLiveData<ArrayList<Review>> reviews;

    //Konstruktur
    public ReviewsViewModel() {
        apiService = RetroInstance.getRetroClient().create(APIService.class);
        reviews = new MutableLiveData<>();
    }

    //Getter
    public MutableLiveData<ArrayList<Review>> getReviews() {
        return reviews;
    }

    //Methode um API durch Retrofit aufzurufen.
    public void makeApiCall(float animeId) {
        //Setzung des API-Calls durch den APIService
        Call<ResponseBody> callRandomAnime = apiService.getReviewsForAnime(String.valueOf((int) animeId));

        callRandomAnime.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ArrayList<Review> array = new ArrayList<Review>();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response.body().string());
                    JSONArray standardArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < standardArray.length(); i++) {
                        JSONObject object = standardArray.getJSONObject(i);
                        JSONObject userObject = object.getJSONObject("user");
                        String profilePicture = userObject.getJSONObject("images").getJSONObject("jpg").getString("image_url");
                        String username = userObject.getString("username");
                        String reviewText = object.getString("review");
                        String ranking = object.getString("score");
                        String tag = object.getJSONArray("tags").getString(0);

                        JSONObject reactionsObject = object.getJSONObject("reactions");
                        JSONArray reactionsArray = reactionsObject.toJSONArray(reactionsObject.names());

                        ArrayList<Reaction> reactions = new ArrayList<Reaction>();
                        for (int j = 0; j < reactionsArray.length(); j++) {
                            reactions.add(new Reaction(reactionStrings[j], (int) reactionsArray.get(j)));
                        }

                        //Sortiert die Reaktion durch die Anzahl (Count)
                        Collections.sort(reactions, new Comparator<Reaction>() {
                            @Override
                            public int compare(Reaction r1, Reaction r2) {
                                return Integer.valueOf(r2.getCount()).compareTo(r1.getCount());
                            }
                        });

                        //Verkürzt die ArrayList -> damit 6 angezeigt werden -> Aus Platzgründen
                        reactions.subList(reactionsArray.length() - 2, reactionsArray.length()).clear();

                        array.add(new Review(profilePicture, username, reviewText, ranking, tag, reactions));
                    }
                    reviews.postValue(array); //Setzt das Reviews Array in die MutableLiveData.
                }catch (Exception e) {
                    Log.d("error", e.toString());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }

}

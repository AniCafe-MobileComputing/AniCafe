package com.example.anigram.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.anigram.ui.animeDetailFragments.DetailsFragment;
import com.example.anigram.ui.animeDetailFragments.ReviewsFragment;
import com.example.anigram.ui.animeDetailFragments.SuggestionsFragment;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.utils.AnimeDialogService;
import com.example.anigram.adapters.viewPager.ViewPagerFragmentAdapter_Anime_Details;
import com.example.anigram.viewModel.DetailsViewModel;
import com.example.anigram.viewModel.HomeViewModel;
import com.github.leandroborgesferreira.loadingbutton.customViews.CircularProgressButton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;


import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.anime.Anime;

//Activity um die DetailsAnime zu zeigen
public class AnimeDetailActivity extends AppCompatActivity {

    //Deklarierung der Variablen
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerFragmentAdapter_Anime_Details viewPagerAdapter;
    private AnimeDialogService animeDialogService;
    private CircularProgressButton addOrEditButton;
    private DetailsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collapsing_activity_anime_detail);

        //Referenz zur Klasse um es in Runnable Methoden zu benutzen.
        AnimeDetailActivity animeDetailActivity = this;

        //Initialisierung des ViewModels
        viewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        //Damit es im Hintergrund angesteuert wird
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run () {

                //Setze die Animation
                View view = findViewById(R.id.background_details);
                AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();

                //Grund fürs faden
                animationDrawable.setEnterFadeDuration(500);
                animationDrawable.setExitFadeDuration(2000);

                //Starte die Animation
                animationDrawable.start();

                //Setze den Service fürs Popup
                animeDialogService = new AnimeDialogService(animeDetailActivity);
                if(getIntent().getExtras() != null) {

                    //Setze im ViewModel den jetzigen Anime
                    viewModel.setAnime(GlobalVariables.mal.getAnime(getIntent().getExtras().getLong("anime")));

                    //Mach den API Aufruf
                    viewModel.makeApiCall(viewModel.getAnime().getTitle());

                    //Setze fürs Popup die Information des jetzigen Anime -> sodass beim Click nur getogglet werden muss
                    animeDialogService.setAnimeListStatus(viewModel.getAnime().getListStatus(), animeDetailActivity);

                    //Falls es von einem anderen Details kommt
                    if(getIntent().getStringExtra("fromDetails") != null) {
                        //Setze das Anime Pic -> zum AnimePicSuggestion um das Shared Element zu verdeutlichen.
                        findViewById(R.id.image_detail).setTransitionName("animePicSuggestions");
                    }
                }

                //Deklariere die UI - Elemente
                tabLayout = findViewById(R.id.tab_layout);
                viewPager = findViewById(R.id.view_pager_anime_details);
                addOrEditButton = (CircularProgressButton) findViewById(R.id.add_or_edit_anime_button);

                //Initialisiere den ViewPagerAdapter -> Sorgt sich um das TabLayout Menu -> Details, Reviews, Suggestions.
                viewPagerAdapter = new ViewPagerFragmentAdapter_Anime_Details(getSupportFragmentManager());

                //Observer wenn die GIF im API Call geladen hat.
                viewModel.getGifUrl().observe(animeDetailActivity, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if(s != null)
                            //Setze das background Image als GIF
                            Glide.with(animeDetailActivity).load(s).into((ImageView) findViewById(R.id.background_image_alternative));
                    }
                });

                //Rufe Hauptmethoden auf.
                checkAnimeInListing();
                initClickListeners();
                setupTabs();
                fillData();
            }
        });
    }

    //Setze alle ClickListeners
    private void initClickListeners() {

        //ClickListener für den Button oben rechts -> add/edit
        addOrEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Falls der Anime schon in seiner Liste ist -> zeige nur das Popup an
                if(GlobalVariables.mal.getAnime(viewModel.getAnime().getID()).getListStatus().getStatus() != null) {
                    animeDialogService.toggleEditScreen();
                } else {
                    addOrEditButton.startAnimation();

                    try {
                        //Rufe die Logik auf um den Anime in seine Liste hinzuzufügen.
                        animeDialogService.addAnime(viewModel.getAnime().getID());

                        //Handler mit vielen delay um das LoadingButton schöner aussehen zu lassen.
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addOrEditButton.doneLoadingAnimation(Color.parseColor("#370058"), ((BitmapDrawable) getDrawable(R.drawable.ic_done_white)).getBitmap());
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        addOrEditButton.revertAnimation();
                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                addOrEditButton.setText("EDIT");
                                            }
                                        }, 200);
                                    }
                                }, 1250);
                            }
                        }, 1250);
                    } catch (Exception e) {
                        Toast.makeText(addOrEditButton.getContext(), "Anime couldn't got added to your list", Toast.LENGTH_SHORT).show();
                        addOrEditButton.revertAnimation();
                    }
                }
            }
        });
    }

    //Prüfe ob der Anime in der Liste ist -> Setze statt ADD Button den EDIT Text
    private void checkAnimeInListing() {
        if(viewModel.getAnime().getListStatus().getStatus() != null) {
            addOrEditButton.setText("EDIT");
        }
    }

    //Lösche den Anime aus der Liste - Methode
    public void animeDeletedAction(Long animeId) {
        //Rufe mithilfe des Wrappers den API Call um den Anime aus der Liste zu entfernen.
        GlobalVariables.mal.deleteAnimeListing(animeId);

        //Single Liner -> Entferne den Anime wenn es einen Anime mit der id gibt
        GlobalVariables.animeList.removeIf(anime -> anime.getAnime().getID().equals(animeId));

        //Setze statt edit wieder add
        addOrEditButton.setText("ADD");

        //Mache die ButtonLoading Animation wieder rückgängig.
        addOrEditButton.revertAnimation();
    }

    //Initialisiere die Main Tabs - Methode -> Details, Reviews, Suggestions.
    private void setupTabs() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run () {
                //Setze in den Bundle die AnimeId, damit man bei jedem Activity Switch die ID mitgeben kann.
                Bundle data = new Bundle();
                data.putLong("animeId", viewModel.getAnime().getID());

                DetailsFragment detailsFragment = new DetailsFragment();
                detailsFragment.setArguments(data);

                ReviewsFragment reviewsFragment = new ReviewsFragment();
                reviewsFragment.setArguments(data);

                SuggestionsFragment suggestionsFragment = new SuggestionsFragment();
                suggestionsFragment.setArguments(data);

                //Setze die Titel/Fragments für die Tabs
                viewPagerAdapter.AddFragment(detailsFragment, "Details");
                viewPagerAdapter.AddFragment(reviewsFragment, "Reviews");
                viewPagerAdapter.AddFragment(suggestionsFragment, "Suggestions");

                viewPager.setAdapter(viewPagerAdapter);
                viewPager.setOffscreenPageLimit(3); //Performance Gründen
                tabLayout.setupWithViewPager(viewPager);
            }
        });
    }

    //Setze alle UI Elemente des Views
    void fillData() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run () {
                //Deklarierung und Initialisierung der UI Elemente
                ImageView bg = findViewById(R.id.background_image_alternative);
                ImageView pic = findViewById(R.id.image_detail);
                TextView title = findViewById(R.id.anime_title);
                TextView rankingPlacement = findViewById(R.id.anime_ranking_placement);
                TextView popularityPlacement = findViewById(R.id.anime_popularity_placement);
                TextView ranking = findViewById(R.id.anime_ranking);
                TextView users = findViewById(R.id.anime_users);
                TextView episodes = findViewById(R.id.anime_episodes);

                //Deklarierung der Anime (ausm ViewModel)
                Anime anime = viewModel.getAnime();

                //Anime Background
                Glide.with(getBaseContext()).load(anime.getMainPicture().getLargeURL()).into(bg);

                //Anime Picture
                Glide.with(getBaseContext()).load(anime.getMainPicture().getLargeURL()).into(pic);

                //Anime Text
                title.setText(anime.getTitle());

                //Placements
                if(anime.getRank() != null) rankingPlacement.setText("#" + anime.getRank().toString());
                if(anime.getPopularity()!= null) popularityPlacement.setText("#" + anime.getPopularity().toString());

                //Ranking
                if(anime.getMeanRating()!= null) ranking.setText(anime.getMeanRating().toString());

                //Users
                users.setText(anime.getUserScoringCount().toString() + " users");

                //episodes
                String timeLanguge = String.format(Locale.forLanguageTag("en-IN"), "%d hr. %02d min.", anime.getAverageEpisodeLength() / 3600, anime.getAverageEpisodeLength() / 60 % 60);
                episodes.setText(anime.getType().toString().equals("Movie") ? "Movie" + " (" + timeLanguge +")" : anime.getEpisodes().toString() + " Episodes" + " (" + anime.getStatus().toString() + ")");
            }
        });
    }

    public void backButton(View v) {
        onBackPressed();
    }
}

package com.example.anigram.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.utils.AuthorizationMALService;

import java.util.ArrayList;

import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.PaginatedIterator;
import dev.katsute.mal4j.anime.AnimeListStatus;

//Activity welche sich um das Login kümmert -> soll es angezeigt werden oder gibt es einen eingeloggten User?
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Sicherheits Setzung, damit API Abfragen gemacht werden können.
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //Schaut in die SharedPreferences (Data Storing) ob es schon einen eingeloggten User gibt.
        SharedPreferences preferences = getSharedPreferences("Anigram",Context.MODE_PRIVATE);
        String oAuthToken = preferences.getString("OAuthToken",null);
        if(oAuthToken != null) { //Falls es einen eingeloggten User gibt ->
            try {
                //Setze den Global MyAnimeList Client zum eingeloggten User -> Damit man mit seinem Token seine API Abfragen machen kann.
                GlobalVariables.mal = MyAnimeList.withToken(oAuthToken);

                //Setze den Global eingeloggten User
                GlobalVariables.authenticatedUser = GlobalVariables.mal.getAuthenticatedUser();

                //Setze im Thread (Hintergrund) die derzeitige AnimeListe aus (Im Thread: Weil es sehr groß werden kann)
                getAnimeUserListing();

                //Starte die MainActivity -> User ist bereit
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                finish();
            } catch (Exception e) {
                //Falls es einen Fehler gab beim einloggen -> setze ganz normal die Login View
                setContentView(R.layout.activity_login);
            }
        } else {
            //Falls es keinen eingeloggten User im Store gibt -> setze ganz normal die Login View
            setContentView(R.layout.activity_login);
        }
    }

    //Methode um die AnimeListe des Users lokal zu laden.
    private void getAnimeUserListing() {
        GlobalVariables.animeList = new ArrayList<AnimeListStatus>();
        Runnable runnable = new Runnable(){
            public void run() {
                PaginatedIterator<AnimeListStatus> paginatedIterator = GlobalVariables.mal.getUserAnimeListing().searchAll();
                for (PaginatedIterator<AnimeListStatus> it = paginatedIterator; it.hasNext(); ) {
                    AnimeListStatus anime = it.next();
                    GlobalVariables.animeList.add(anime);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    //Beim Click auf LoginButton rufe die WebViewActivity damit sich der User einloggen kann.
    public void openMALWebView(View v) {
        Intent intent = new Intent(LoginActivity.this, AuthorizationMALService.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
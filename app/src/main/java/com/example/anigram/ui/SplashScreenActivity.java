package com.example.anigram.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.anigram.R;

//Activity die unser SplashScreen anzeigt
public class SplashScreenActivity extends AppCompatActivity {

    //Deklarierung der Variablen
    private ImageView animationView;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Initialisiere das ImageView
        animationView = findViewById(R.id.cupAnimation);

        //Bekomme die Drawable (src Attribut)
        animationDrawable = (AnimationDrawable) animationView.getDrawable();

        //Setze den Fade
        animationDrawable.setEnterFadeDuration(0);
        animationDrawable.setExitFadeDuration(300);

        //Starte die Animation
        animationDrawable.start();

        //Starte die LoginActivity nach 2 Sekunden
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out); //Überschreibe standart transition mit fade in und fade out
                finish();
            }
        },2000);
    }
}
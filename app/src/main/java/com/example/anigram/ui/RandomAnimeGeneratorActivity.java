package com.example.anigram.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.anigram.R;
import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.viewModel.RandomAnimeViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

//Activity des RandomAnime
public class RandomAnimeGeneratorActivity extends AppCompatActivity {

    //Deklarierung der Variablen
    private ImageView animationView;
    private AnimationDrawable animationDrawable;
    private SensorManager sensorManager;
    private RandomAnimeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_anime_generator);

        //ViewModel wird gesetzt
        viewModel = new ViewModelProvider(this).get(RandomAnimeViewModel.class);

        //Animation des "wackelnden Handys" wird initialisiert und gestartet
        animationView = findViewById(R.id.image_phone);
        animationDrawable = (AnimationDrawable) animationView.getDrawable();
        animationDrawable.start();

        //Sensor Stuffs -> Inspiration: https://youtu.be/fPa9Sev7il8
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(sensorManager).registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);

        //Wenn ein neuer RandomAnime im ViewModel generiert wurde -> setze die UI Elemente
        viewModel.getRandomAnime().observe(this, new Observer<CustomAnimeObject>() {
            @Override
            public void onChanged(CustomAnimeObject randomAnime) {
                if(randomAnime != null) {
                    //Setze die Ergebnis View
                    setContentView(R.layout.activity_random_anime_generator_result);

                    //Deklariere und setze die UI Elemente mit dem generierten Anime
                    TextView titleView = findViewById(R.id.random_anime_title);
                    titleView.setText(randomAnime.getTitle());

                    TextView synopsisView = findViewById(R.id.random_anime_synopsis);
                    synopsisView.setText(randomAnime.getSynopsis());

                    ImageView imageView = findViewById(R.id.random_anime_picture);
                    Glide.with(titleView.getContext()).load(randomAnime.getImageUrl()).into(imageView);

                    //Registriere erneut die Motion für Shake
                    sensorManager.registerListener(mSensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                            SensorManager.SENSOR_DELAY_NORMAL);

                    //Wenn auf GotoPage Button geklickt wird -> Starte Transition zur Detail Activity des Anime
                    ((TextView) findViewById(R.id.goto_page)).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(RandomAnimeGeneratorActivity.this, AnimeDetailActivity.class);
                            intent.putExtra("anime", randomAnime.getId());

                            ActivityOptionsCompat options = ActivityOptionsCompat
                                    .makeSceneTransitionAnimation(RandomAnimeGeneratorActivity.this, imageView, imageView.getTransitionName());

                            startActivity(intent, options.toBundle());
                        }
                    });
                }
            }
        });
    }

    //Sensor Listener: Inspiration: https://youtu.be/fPa9Sev7il8
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float floatSum = Math.abs(x) + Math.abs(y) + Math.abs(z);
            if (floatSum > 30) {
                sensorManager.unregisterListener(mSensorListener,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)); //Aus Performance Gründen -> um mehrere Random Anime zu vermeiden
                viewModel.makeApiCall(); //Rufe die API aus um einen neuen Random Anime zu erzeugen.
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public void backButton(View v) {
        onBackPressed();
    }
}
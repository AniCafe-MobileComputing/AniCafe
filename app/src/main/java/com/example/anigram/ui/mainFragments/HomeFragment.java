package com.example.anigram.ui.mainFragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.anigram.adapters.recyclerViews.HomeAdapter;
import com.example.anigram.adapters.recyclerViews.ListAdapter;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.R;
import com.example.anigram.ui.AnimeDetailActivity;
import com.example.anigram.ui.LoginActivity;
import com.example.anigram.viewModel.HomeViewModel;
import com.example.anigram.viewModel.ListViewModel;
import com.saadahmedsoft.popupdialog.PopupDialog;
import com.saadahmedsoft.popupdialog.Styles;
import com.saadahmedsoft.popupdialog.listener.OnDialogButtonClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//Fragment für Main (Home mit Trendings und Seasonals)
public class HomeFragment extends Fragment implements HomeAdapter.ClickListener{

    //Deklarierung der UI Elemente
    private ImageView imageBlur;
    private Button logoutButton;

    //Deklarierung der Listen
    private ArrayList<CustomAnimeObject> seasonalModelList;
    private ArrayList<CustomAnimeObject> trendingModelList;

    //Deklarierung der utils.
    private int lastVisibleItemPosition = 0; //Hilfs-Variable für das Item in der Mitte
    private HomeAdapter seasonalAdapter;
    private HomeAdapter trendingAdapter;
    private RecyclerView seasonalRecycler;
    private RecyclerView trendingRecycler;
    private HomeViewModel viewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setzung des ViewModels für das Fragment
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //Wenn der SeasonalArray im ViewModel generiert wurde -> setze die list im RecyclerView
        viewModel.getSeasonals().observe(this, new Observer<ArrayList<CustomAnimeObject>>() {
            @Override
            public void onChanged(ArrayList<CustomAnimeObject> seasonalList) {
                if(seasonalList != null) {
                    initScrollAnimations(seasonalRecycler);
                    seasonalModelList = seasonalList;
                    seasonalAdapter.setList(seasonalList);
                }
            }
        });

        //Wenn der TrendingsArray im ViewModel generiert wurde -> setze die list im RecyclerView
        viewModel.getTrendings().observe(this, new Observer<ArrayList<CustomAnimeObject>>() {
            @Override
            public void onChanged(ArrayList<CustomAnimeObject> trendingList) {
                if(trendingList != null) {
                    initScrollAnimations(trendingRecycler);
                    trendingModelList = trendingList;
                    trendingAdapter.setList(trendingList);
                }
            }
        });

        //Mache den API-Call
        viewModel.makeApiCalls();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        //Setzung der UI-Elemente
        logoutButton = v.findViewById(R.id.logout_button);
        imageBlur = v.findViewById(R.id.home_image_blur_background);
        imageBlur.setColorFilter(-8810600); //Standart Color Filter auf den Hintergrund hinter der Liste

        ((TextView) v.findViewById(R.id.username)).setText(GlobalVariables.authenticatedUser.getName());

        //Deklariere die Recycler mit der Hilfs-Recycler Methode die für beide Listen zuständig sind.
        seasonalRecycler = initRecycler("seasonal", v, seasonalModelList);
        trendingRecycler = initRecycler("trending", v, trendingModelList);

        //Rufe Hauptmethode auf
        logoutInit();
        return v;
    }

    //Methode um die Funktion des Logout Buttons zu setzen
    private void logoutInit() {
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Rufe den Popup - "bist du dir sicher" auf.
                PopupDialog.getInstance(getContext())
                        .setStyle(Styles.ANDROID_DEFAULT)
                        .setHeading("Logout")
                        .setDescription("Are you sure you want to logout?"+
                                " This action cannot be undone")
                        .showDialog(new OnDialogButtonClickListener() {
                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onPositiveClicked(Dialog dialog) {
                                //Setze den MyAnimeList Client auf null, da kein Token mehr verfügbar ist.
                                GlobalVariables.mal = null;

                                //Setze den Store in der App auf null.
                                SharedPreferences preferences = getActivity().getSharedPreferences("Anigram" , Context.MODE_PRIVATE);
                                preferences.edit().putString("OAuthToken", null).apply();

                                //Starte die Login Activity
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out); //Überschreibe Transition auf fade.
                            }

                            @Override
                            public void onNegativeClicked(Dialog dialog) {
                                super.onNegativeClicked(dialog);
                            }
                        });
            }
        });
    }

    //Methode um RecyclerView zu erstellen und zwischen beiden Listen zu unterscheiden -> spart Code -> erhöht komplexitität.
    private RecyclerView initRecycler(String s, View v, ArrayList arrayList){
        if(s.equals("seasonal")) seasonalAdapter = new HomeAdapter(arrayList, this);
        else if(s.equals("trending")) trendingAdapter = new HomeAdapter(arrayList, this);

        RecyclerView recyclerView = v.findViewById(s.equals("seasonal") ? R.id.recycler_seasonal : R.id.recycler_trending);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(s.equals("seasonal") ? seasonalAdapter : trendingAdapter);

        //SnapHelper ist für den scroll-Effekt durch die RecyclerView zuständig -> smooth soft only one item scroll
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        return recyclerView;
    }

    //Scroll Animation -> Scale up and scale down für das Item in der Mitte
    private void initScrollAnimations(RecyclerView recyclerView) {

        try {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    //Bekomme den Manager
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                    //Bekomme das visible Item für den User.
                    int visibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();

                    //Falls visible Item neu ist -> um Wiederholungen zu vermeiden -> Performance.
                    if (visibleItemPosition != lastVisibleItemPosition) {

                        //Setze dies als neues visible Item
                        lastVisibleItemPosition = visibleItemPosition;

                        //Errechne welches Item links ist und scale es down
                        View vLeft = recyclerView.getLayoutManager().findViewByPosition(visibleItemPosition);
                        if (vLeft != null)
                            //Animate scale - down
                            vLeft.animate().scaleX(0.85f).scaleY(0.85f).setDuration(300);

                        //Errechne welches Item rechts ist und scale es down
                        View vRight = recyclerView.getLayoutManager().findViewByPosition(visibleItemPosition + 2);
                        if (vRight != null)
                            //Animate scale - down
                            vRight.animate().scaleX(0.85f).scaleY(0.85f).setDuration(300);

                        //Errechne welches Item in der mitte ist und scale es down
                        View vCenter = recyclerView.getLayoutManager().findViewByPosition(visibleItemPosition + 1);
                        if (vCenter != null) {
                            //Animate scale - up
                            vCenter.animate().scaleX(1f).scaleY(1f).setDuration(300);

                            //Das Item in der Mitte wird als Farbe benutzt hinter der Liste.
                            try {
                                ImageView image = (ImageView) vCenter.findViewById(R.id.home_image_item);
                                Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
                                int color = getDominantColor(bitmap);
                                imageBlur.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                            } catch (Exception e) {}
                        }
                    }
                }
            });

            //Animiere das erste Item auf scale up
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    View v = recyclerView.getLayoutManager().findViewByPosition(1);
                    if (v != null) v.animate().scaleX(1f).scaleY(1f).setDuration(300);
                }
            }, 50);
        } catch (Exception e) {}
    }

    //Methode um die dominante Farbe zu bekommen | Inspiration: https://stackoverflow.com/questions/8471236/finding-the-dominant-color-of-an-image-in-an-android-drawable
    private int getDominantColor(Bitmap bitmap) {
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
        final int color = newBitmap.getPixel(0, 0);
        newBitmap.recycle();
        return color;
    }

    //Click Listener -> Transition zu Details Seite
    @Override
    public void gotoAnimeDetails(ImageView view, CustomAnimeObject anime) {
        Intent intent = new Intent(getActivity(), AnimeDetailActivity.class);
        intent.putExtra("anime", anime.getId());

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), view, "animePicture");

        startActivity(intent, options.toBundle());
    }

}
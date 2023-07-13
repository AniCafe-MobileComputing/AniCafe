package com.example.anigram.ui.mainFragments;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anigram.adapters.recyclerViews.ListAdapter;
import com.example.anigram.adapters.recyclerViews.SearchAdapter;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.ui.AnimeDetailActivity;
import com.example.anigram.ui.RandomAnimeGeneratorActivity;
import com.example.anigram.viewModel.DetailsViewModel;
import com.example.anigram.viewModel.ListViewModel;
import com.example.anigram.viewModel.SearchViewModel;

import java.util.List;

import dev.katsute.mal4j.anime.Anime;

//Fragment für Main (Search)
public class SearchFragment extends Fragment implements SearchAdapter.ClickListener {

    //Deklarierung der UI - Elemente
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    //Deklarierung der Utils.
    private SearchAdapter searchAdapter;
    private SearchViewModel viewModel;


    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setzung des ViewModels für das Fragment
        viewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        //Setzung des ListAdapters -> Für die RecyclerView
        searchAdapter = new SearchAdapter(viewModel.getSuggestionsList(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_search, container, false);

        //Setzung der UI - Elemente
        recyclerView = v.findViewById(R.id.recycler_search);
        progressBar = v.findViewById(R.id.search_progress_bar);
        searchView = v.findViewById(R.id.searchView);

        //Rufe die Hauptmethoden auf
        initRecyclerView();
        initSearchView();
        initShakeButtonPress(v);

        return v;
    }

    //Methode um die RecyclerView zu erzeugen
    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(searchAdapter);
    }

    //Wenn auf den Shake Button geklickt wurde -> starte neue Activity
    private void initShakeButtonPress(View v) {
        ImageView img = (ImageView) v.findViewById(R.id.openShakeButton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openShakeActivity();
            }
        });
    }

    //Setze die SearchView
    private void initSearchView() {
        //Verstecke die Tastatur
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Wenn eine Suche eingegeben wurde
                progressBar.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run () {
                        try {
                            //Rufe die Logik auf um mit dem API-Wrappers die Suche zu beginnen
                            viewModel.searchAnimeList(query);

                            //Wenn die Suche leer ausging
                            if(viewModel.getSearchList().isEmpty()) {
                                Toast.makeText(getContext(), "No Anime found", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                            } else {
                                //Wenn Suche erfolgreich war
                                searchView.clearFocus();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setVisibility(View.VISIBLE);
                                searchAdapter.setSearchedList(viewModel.getSearchList());
                            }
                        } catch (Exception e) {
                            //Fehlerhafte Suche -> API down oder kein Internet
                            searchView.clearFocus();
                            progressBar.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            Toast.makeText(getContext(), "No Anime found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Simuliere wenn auf x gedrückt wurde oder die Eingabe gelöscht wurde.
                if (searchView.getQuery().length() == 0) {
                    //Zeige wieder die Standart Suggestions Liste
                    searchAdapter.setSearchedList(viewModel.getSuggestionsList());
                    searchView.clearFocus();
                }
                return false;
            }
        });
    }

    //Methode um die Intent zu deklarieren
    private void openShakeActivity(){
        Intent intent = new Intent(getActivity(), RandomAnimeGeneratorActivity.class);
        startActivity(intent);

        //Überschreibe die Animation mit Fade
        getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    //Wenn auf ein Anime geklickt wurde -> Starte Details mit Transition
    @Override
    public void gotoAnimeDetails(ImageView view, Anime anime) {
        Intent intent = new Intent(getContext(), AnimeDetailActivity.class);
        intent.putExtra("anime", anime.getID());

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(),view, ViewCompat.getTransitionName(view));

        startActivity(intent, options.toBundle());
    }


}
package com.example.anigram.ui.profileFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.anigram.ui.AnimeDetailActivity;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.utils.AnimeDialogService;
import com.example.anigram.adapters.recyclerViews.ListAdapter;
import com.example.anigram.viewModel.DetailsViewModel;
import com.example.anigram.viewModel.ListViewModel;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import dev.katsute.mal4j.anime.AnimeListStatus;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

//Fragment für Profile (Anime - Liste)
public class ListFragment extends Fragment implements ListAdapter.ClickListener {

    //Deklarierung der UI Elemente
    private ListAdapter listAdapter;
    private SearchView searchView;
    private PowerSpinnerView powerSpinnerView_status;
    private PowerSpinnerView powerSpinnerView_sort;

    //Deklarierung von Utils.
    private AnimeDialogService animeDialogService;
    private ListViewModel viewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setzung des ViewModels für das Fragment
        viewModel = new ViewModelProvider(this).get(ListViewModel.class);

        //Setzung des ListAdapters -> Für die RecyclerView
        listAdapter = new ListAdapter(viewModel.getFilteredList(), this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_list, container, false);

        //Deklarierung der RecyclerView
        RecyclerView recyclerView = v.findViewById(R.id.recycler_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        //Library: https://github.com/wasabeef/recyclerview-animators
        ScaleInAnimationAdapter scaleInAnimationAdapter = new ScaleInAnimationAdapter(listAdapter);
        scaleInAnimationAdapter.setDuration(500);
        scaleInAnimationAdapter.setInterpolator(new OvershootInterpolator());
        scaleInAnimationAdapter.setFirstOnly(false);
        recyclerView.setAdapter(scaleInAnimationAdapter);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());

        //Setzung der UI - Elemente
        powerSpinnerView_status = v.findViewById(R.id.dropdown_status);
        powerSpinnerView_sort = v.findViewById(R.id.dropdown_sort);
        searchView = v.findViewById(R.id.search_view_list);

        //Setzung des Services fürs das Popup
        animeDialogService = new AnimeDialogService(getContext());

        //Aufruf der Hauptmethoden
        searchList();
        spinnerStatus();
        spinnerSort();

        return v;
    }

    private void searchList() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Wenn eine Eingabe mit Enter ausgeführt wurde
                filterThroughEverything();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (searchView.getQuery().length() == 0){
                    //Simulierung des drückens auf x Zeichen -> Löschen aller Zeichen
                    filterThroughEverything();
                }
                return false;
            }
        });
    }

    //Methode um den Status - Spinner
    private  void spinnerStatus(){
        //Setze den Standart Wert für den Spinner
        powerSpinnerView_status.selectItemByIndex(0);
        powerSpinnerView_status.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                if (newIndex != oldIndex) {
                    //Wenn was neues ausgewählt wurde -> sortiere durch die Liste
                    filterThroughEverything();
                }
            }
        });
    }

    //Methode um den Sort - Spinner
    private  void spinnerSort(){
        powerSpinnerView_sort.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
            @Override public void onItemSelected(int oldIndex, @Nullable String oldItem, int newIndex, String newItem) {
                if(newIndex != oldIndex) {
                    //Wenn was neues ausgewählt wurde -> sortiere durch die Liste
                    filterThroughEverything();
                }
            }
        });
    }

    public void filterThroughEverything() {
        //Rufe die ViewModel Methode auf um zu filtern -> gebe alle Werte aus den Spinner und EditText mit
        viewModel.filter(searchView.getQuery().toString(), powerSpinnerView_status.getSelectedIndex(), powerSpinnerView_sort.getSelectedIndex());

        //Wenn die Sortierung leer ist -> kein Anime gefunden.
        if (viewModel.getFilteredList().isEmpty()) {
            Toast.makeText(getContext(), "No Anime found", Toast.LENGTH_SHORT).show();
        } else {
            //Ansonsten verstecke die Tastatur
            searchView.clearFocus();
            //Gebe dem Adapter die neue Liste -> refresh
            listAdapter.setSearchedList(viewModel.getFilteredList());
        }
    }

    //ClickListener -> Starte die Details Seite
    @Override
    public void gotoAnimeDetails(ImageView view, AnimeListStatus anime) {
        Intent intent = new Intent(getContext(), AnimeDetailActivity.class);
        intent.putExtra("anime", anime.getAnime().getID());

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(),view, ViewCompat.getTransitionName(view));

        startActivity(intent, options.toBundle());
    }

    //Methode um ein Anime aus der Liste zu entfernen
    public void deleteAnimeFromList(Long animeId, int position) {

        //Rufe mithilfe des Wrappers den API Call um den Anime aus der Liste zu entfernen.
        GlobalVariables.mal.deleteAnimeListing(animeId);

        //Single Liner -> Entferne den Anime wenn es einen Anime mit der id gibt
        GlobalVariables.animeList.removeIf(anime -> anime.getAnime().getID().equals(animeId));

        //Entferne die Items aus dem Array und aus der RecyclerView
        viewModel.getFilteredList().remove(position);
        listAdapter.removeItem(position);
        searchView.clearFocus();
    }

    //Wenn auf ein Edit Button geklickt wurde -> gebe dem EditDialog bescheid um welchen Anime es sich handelt und toggle die Sicht (sichtbar)
    @Override
    public void openEditAnime(AnimeListStatus animeListStatus, int position) {
        animeDialogService.setAnimeListStatus(animeListStatus, position, this);
        animeDialogService.toggleEditScreen();
    }
}
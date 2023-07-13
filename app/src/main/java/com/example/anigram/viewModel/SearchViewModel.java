package com.example.anigram.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.anigram.utils.GlobalVariables;

import java.util.List;

import dev.katsute.mal4j.anime.Anime;

//ViewModel fpr das SearchFragment
public class SearchViewModel extends ViewModel {

    //Deklarierung der Variablen
    private List<Anime> searchList;
    private List<Anime> suggestionsList;

    //Konstruktur
    public SearchViewModel() {
        //Standart SuggestionsList wird durch unseren API-Wrappers gesetzt.
        suggestionsList = GlobalVariables.mal.getAnimeSuggestions().includeNSFW(false).withLimit(55).search();
    }

    //Rufe die Methode vom Fragment auf -> wenn ein Anime gesucht wird.
    public void searchAnimeList(String query) {
        //Methode unseres API-Wrappers
        searchList =
                GlobalVariables.mal.getAnime()
                        .withQuery(query)
                        .withLimit(50)
                        .includeNSFW(true)
                        .search();
    }

    //Getter
    public List<Anime> getSearchList() {
        return searchList;
    }

    public List<Anime> getSuggestionsList() {
        return suggestionsList;
    }
}

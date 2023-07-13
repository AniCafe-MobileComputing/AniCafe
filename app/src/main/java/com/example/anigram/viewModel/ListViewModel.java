package com.example.anigram.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.anigram.utils.GlobalVariables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;

import dev.katsute.mal4j.anime.AnimeListStatus;

//ViewModel für das ListFragment
public class ListViewModel extends ViewModel {

    //Deklarierung der Variablen

    //Status Strings um die richtige Bezeichnung zu haben.
    private String[] ANIME_UPDATE_STATUS_ENUMS = new String[] {"all", "completed", "on_hold", "plan_to_watch", "watching", "dropped"};
    private ArrayList<AnimeListStatus> filteredList;

    //Konstruktur
    public ListViewModel() {
        filteredList = GlobalVariables.animeList;
    }

    //Getter
    public ArrayList<AnimeListStatus> getFilteredList() {
        return filteredList;
    }

    //Die Logik für die Filterung
    public void filter(String searchViewQuery, int statusSpinnerSelected, int  sortSpinnerSelected) {
        filteredList = GlobalVariables.animeList;

        //Standard Sort -> Title (Alphabetisch)
        Collections.sort(filteredList, (s1, s2) -> {
            return s1.getAnime().getTitle().toLowerCase().compareTo(s2.getAnime().getTitle().toLowerCase());
        });

        //SEARCH
        if(searchViewQuery.length() > 0)
            filteredList = (ArrayList<AnimeListStatus>) filteredList.stream().filter((e) -> e.getAnime().getTitle().toLowerCase().contains(searchViewQuery)).collect(Collectors.toList());

        //STATUS
        if (statusSpinnerSelected != 0)
            filteredList = (ArrayList<AnimeListStatus>) filteredList.stream().filter(e -> e.getRawStatus().equals(ANIME_UPDATE_STATUS_ENUMS[statusSpinnerSelected])).collect(Collectors.toList());

        //SORT
        switch (sortSpinnerSelected){
            case 1:
                Collections.sort(filteredList, (s1, s2) -> {
                    return s2.getWatchedEpisodes().compareTo(s1.getWatchedEpisodes());
                });
                break;
            case 2:
                filteredList = (ArrayList<AnimeListStatus>) filteredList.stream().sorted(Comparator.nullsLast(new Comparator<AnimeListStatus>() {
                    @Override
                    public int compare(AnimeListStatus s1, AnimeListStatus s2) {
                        if (s1.getAnime().getMeanRating() == null && s2.getAnime().getMeanRating() == null) {
                            return 0;
                        } else if(s1.getAnime().getMeanRating() == null) {
                            return -1;
                        } else if(s2.getAnime().getMeanRating() == null) {
                            return 1;
                        } else {
                            return s2.getAnime().getMeanRating().compareTo(s1.getAnime().getMeanRating());
                        }
                    }
                })).collect(Collectors.toList());
                break;
            case 3:
                filteredList = (ArrayList<AnimeListStatus>) filteredList.stream().sorted(Comparator.nullsLast(new Comparator<AnimeListStatus>() {
                    @Override
                    public int compare(AnimeListStatus s1, AnimeListStatus s2) {
                        if (s1.getScore() == null && s2.getScore() == null) {
                            return 0;
                        } else if(s1.getScore() == null) {
                            return -1;
                        } else if(s2.getScore() == null) {
                            return 1;
                        } else {
                            return s2.getScore().compareTo(s1.getScore());
                        }
                    }
                })).collect(Collectors.toList());
                break;
            case 4:
                Collections.sort(filteredList, (s1, s2) -> {
                    return s1.getRawStatus().compareTo(s2.getRawStatus());
                });
                break;
        }
    }

}

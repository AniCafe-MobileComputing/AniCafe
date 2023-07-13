package com.example.anigram.viewModel;

import androidx.lifecycle.ViewModel;

import com.example.anigram.utils.GlobalVariables;

import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.AnimeRecommendation;
import dev.katsute.mal4j.anime.RelatedAnime;

//ViewModel unsereres SUggestionFragments
public class SuggestionsViewModel extends ViewModel {

    //Deklarierung der Variablen
    private RelatedAnime[] related;
    private AnimeRecommendation[] recommendations;

    //
    public SuggestionsViewModel() {}

    //Setze die AnimeId vom derzeitigen Anime in der das SuggestionsTab angezeigt wird.
    public void setAnime(float animeId) {
        //Setze den Anime durch unseren API-Wrappers
        Anime anime = GlobalVariables.mal.getAnime((long) animeId);

        //Bekomme die Listen durch unseren Wrapper.
        related = anime.getRelatedAnime();
        recommendations = anime.getRecommendations();
    }

    //Getter
    public RelatedAnime[] getRelated() {
        return related;
    }

    public AnimeRecommendation[] getRecommendations() {
        return recommendations;
    }

}

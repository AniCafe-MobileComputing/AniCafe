package com.example.anigram.ui.animeDetailFragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import com.example.anigram.adapters.recyclerViews.ListAdapter;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.viewModel.ListViewModel;

import java.util.Arrays;
import java.util.List;

import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.property.Genre;

//Fragment für Anime Detail (Details)
public class DetailsFragment extends Fragment {

    //Deklarierung der Variablen -> Kein ViewModel nötig, da es sich nur um eine Variabel handelt.
    private Long animeId;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Erhalte mitgegebene Argumente vom vorherigen Intent.
        Bundle data = getArguments();
        if(data != null) animeId = data.getLong("animeId"); //Setze die animeId
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_details, container, false);

        //Speichere die Variable Anime ab und erzeuge es vom Wrapper mit der gegebenenen id.
        Anime anime = GlobalVariables.mal.getAnime(animeId);

        //Setzung der UI-Elemente
        TextView releaseDate = view.findViewById(R.id.text_date);
        TextView studio = view.findViewById(R.id.text_studio);
        TextView members = view.findViewById(R.id.text_members);
        TextView synopsis = view.findViewById(R.id.text_synopsis);

        //Setzung der Texte für die UI - Elemente
        if(anime.getCreatedAt() != null) releaseDate.setText(anime.getCreatedAt().toLocaleString());
        if(anime.getStudios().length > 0) studio.setText(anime.getStudios()[0].getName());
        if(anime.getUserListingCount() != null) members.setText(anime.getUserListingCount().toString());
        if(anime.getSynopsis() != null) synopsis.setText(anime.getSynopsis());

        //Genre Builder
        List<Genre> genres = Arrays.asList(anime.getGenres());
        GridLayout gl = view.findViewById( R.id.gridGenre);

        for (Genre genre: genres) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.genre_item, null);
            TextView textView = v.findViewById(R.id.genre_name);
            textView.setText(genre.getName());

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.columnSpec = GridLayout.spec( GridLayout.UNDEFINED, 1, GridLayout.FILL ); //for stretch a child to column use GridLayout.FILL
            gl.addView( v, lp );
        }

        return view;
    }
}
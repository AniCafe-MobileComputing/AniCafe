package com.example.anigram.ui.animeDetailFragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anigram.adapters.recyclerViews.ListAdapter;
import com.example.anigram.ui.AnimeDetailActivity;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.R;
import com.example.anigram.viewModel.ListViewModel;
import com.example.anigram.viewModel.SuggestionsViewModel;
import com.google.android.material.imageview.ShapeableImageView;

import dev.katsute.mal4j.anime.Anime;
import dev.katsute.mal4j.anime.AnimeRecommendation;
import dev.katsute.mal4j.anime.RelatedAnime;
import dev.katsute.mal4j.property.Recommendation;
import eightbitlab.com.blurview.BlurView;

//Fragment für Anime Detail (Suggestions)
public class SuggestionsFragment extends Fragment {

    //Deklarierung der Variablen
    private Long animeId;
    private SuggestionsViewModel viewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setzung des ViewModels für das Fragment
        viewModel = new ViewModelProvider(this).get(SuggestionsViewModel.class);

        //Erhalte mitgegebene Argumente vom vorherigen Intent.
        Bundle data = getArguments();
        if(data != null) {
            animeId = data.getLong("animeId"); //Setze die animeId
            viewModel.setAnime(animeId); //Generiere mithilfe des Wrappers den Anime in der viewModel
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_detail_suggestions, container, false);

        //Setze die LinearLayouts - Methoden
        fillRelated(view);
        fillRecommendations(view);

        return view;
    }

    private void fillRecommendations(View view) {
        //Wenn die Recommendations Array gefüllt ist
        if(viewModel.getRecommendations().length > 0) {
            LinearLayout linearLayoutView = view.findViewById(R.id.recommendations_layout);
            for (Recommendation recommendation : viewModel.getRecommendations()) {

                //Setze das suggestion_item in die view
                View v = getLayoutInflater().inflate(R.layout.suggestions_item, null);

                //Initialisiere die UI-Elemente
                ShapeableImageView picture = v.findViewById(R.id.suggestion_image);
                TextView title = v.findViewById(R.id.suggestion_text);

                //da die recommendation anime durch den Wrapper leider komische Strings sind, müssen wir diese Werte durch splits trennen.
                String id = recommendation.toString().split("id=")[1].split(",")[0];
                String titleFromRecommendation = recommendation.toString().split("title='")[1].split("'")[0];
                String pictureFromRecommendation = recommendation.toString().split("medium='")[1].split("'")[0];

                //Setze die Werte in die UI
                title.setText(titleFromRecommendation);
                Glide.with(view.getContext()).load(pictureFromRecommendation).into(picture);

                //Füge dem Linear Layout die View
                linearLayoutView.addView(v);

                //Setze ein onClickListener auf das Bild
                picture.setOnClickListener(pictureView -> gotoAnimeDetails(id, picture));
            }
        } else {
            //Falls es kein Suggested recommended anime gibt -> verstecke das dazugehörige Linear Layout
            ((LinearLayout) view.findViewById(R.id.linearLayout_suggestions_like)).setVisibility(View.GONE);
        }
    }

    private void fillRelated(View view) {
        //Wenn das Related Array gefüllt ist
        if(viewModel.getRelated().length > 0) {
            LinearLayout linearLayoutView = view.findViewById(R.id.related_layout);
            for (RelatedAnime relatedAnime: viewModel.getRelated()) {

                //Setze das suggestion_item in die view
                View v = getLayoutInflater().inflate(R.layout.suggestions_item, null);

                //Initialisiere die UI-Elemente
                ShapeableImageView picture = v.findViewById(R.id.suggestion_image);
                TextView title = v.findViewById(R.id.suggestion_text);

                //da die related anime durch den Wrapper leider komische Strings sind, müssen wir diese Werte durch splits trennen.
                String id = relatedAnime.toString().split("id=")[1].split(",")[0];
                String titleFromRecommendation = relatedAnime.toString().split("title='")[1].split("'")[0];
                String pictureFromRecommendation = relatedAnime.toString().split("medium='")[1].split("'")[0];

                //Setze die Werte in die UI
                title.setText(titleFromRecommendation);
                Glide.with(view.getContext()).load(pictureFromRecommendation).into(picture);

                //Füge dem Linear Layout die View
                linearLayoutView.addView(v);

                //Setze ein onClickListener auf das Bild
                picture.setOnClickListener(pictureView -> gotoAnimeDetails(id, picture));
            }
        } else {
            ((LinearLayout) view.findViewById(R.id.linearLayout_suggestions_related)).setVisibility(View.GONE);
        }
    }

    //Wenn auf ein Anime geklickt wurde -> Starte Details mit Transition
    private void gotoAnimeDetails(String animeId, ShapeableImageView view) {
        Intent intent = new Intent(getContext(), AnimeDetailActivity.class);
        intent.putExtra("anime", Long.parseLong(animeId));
        intent.putExtra("fromDetails", "true");

        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));

        startActivity(intent, options.toBundle());
    }
}
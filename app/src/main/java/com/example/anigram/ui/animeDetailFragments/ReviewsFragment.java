package com.example.anigram.ui.animeDetailFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.anigram.R;
import com.example.anigram.adapters.recyclerViews.ReviewsAdapter;
import com.example.anigram.model.Review;
import com.example.anigram.viewModel.ReviewsViewModel;

import java.util.ArrayList;

//Fragment für Anime Detail (Reviews)
public class ReviewsFragment extends Fragment {

    //Deklarierung der UI-Elemente
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ReviewsAdapter reviewsAdapter;

    //Deklarierung der utils.
    private ReviewsViewModel viewModel;
    private ArrayList<Review> reviewsModelList;
    private Long animeId;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setzung des ViewModels für das Fragment
        viewModel = new ViewModelProvider(this).get(ReviewsViewModel.class);

        //Erhalte mitgegebene Argumente vom vorherigen Intent.
        Bundle data = getArguments();
        if(data != null) {
            animeId = data.getLong("animeId"); //Setze die animeId
            viewModel.makeApiCall(animeId); //Mache den Reviews API-Call durch das ViewModel
        }

        //Wenn der reviewsArray im ViewModel generiert wurde -> setze die list im RecyclerView
        viewModel.getReviews().observe(this, new Observer<ArrayList<Review>>() {
            @Override
            public void onChanged(ArrayList<Review> reviewsList) {
                if(reviewsList != null) {
                    reviewsModelList = reviewsList;
                    reviewsAdapter.setList(reviewsList);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_details_reviews, container, false);

        //Setzung des UI-Elementes
        recyclerView = view.findViewById(R.id.recycler_reviews);

        //Deklarierung der RecyclerView
        initRecyclerView();

        return view;
    }

    //Methode um die RecylcerView zu erzeugen
    public void initRecyclerView(){
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        reviewsAdapter = new ReviewsAdapter(reviewsModelList);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewsAdapter);

        //SnapHelper ist für den scroll-Effekt durch die RecyclerView zuständig -> smooth soft only one item scroll
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
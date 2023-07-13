package com.example.anigram.ui.profileFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anigram.adapters.recyclerViews.ListAdapter;
import com.example.anigram.adapters.recyclerViews.ReviewsAdapter;
import com.example.anigram.model.Friend;
import com.example.anigram.R;
import com.example.anigram.adapters.recyclerViews.FriendsAdapter;
import com.example.anigram.model.Review;
import com.example.anigram.utils.GlobalVariables;
import com.example.anigram.viewModel.FriendsViewModel;
import com.example.anigram.viewModel.ListViewModel;
import com.example.anigram.viewModel.ReviewsViewModel;

import java.util.ArrayList;
import java.util.Collections;

//Fragment für Profile (Freunde - Liste)
public class FriendsFragment extends Fragment implements FriendsAdapter.OnFriendClickInterface {

    //Deklarierung der Variablen
    private RecyclerView recyclerView;
    private ArrayList<Friend> friendsModelList;
    private FriendsAdapter friendsAdapter;
    private FriendsViewModel viewModel;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setzung des ViewModels für das Fragment
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);

        //Setzung des FriendsAdapter -> Für die RecyclerView
        friendsAdapter = new FriendsAdapter(friendsModelList, this);

        //Wenn der FriendsArray im ViewModel generiert wurde -> setze die list im RecyclerView
        viewModel.getFriends().observe(this, new Observer<ArrayList<Friend>>() {
            @Override
            public void onChanged(ArrayList<Friend> friendsList) {
                if(friendsList != null) {
                    friendsModelList = friendsList;
                    Collections.sort(friendsModelList, (s1, s2) -> {
                        return s1.getUsername().toLowerCase().compareTo(s2.getUsername().toLowerCase());
                    });
                    friendsAdapter.setList(friendsList);
                }
            }
        });

        //Mache den API-Call
        viewModel.makeApiCall();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_friends, container, false);

        //Methodenaufruf -> RecyclerView
        initRecyclerView(v);

        return v;
    }

    //Methode um die RecyclerView zu erzeugen
    private void initRecyclerView(View v){
        recyclerView = v.findViewById(R.id.recycler_friends);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(friendsAdapter);
    }

    //Wenn auf ein Freund geklickt wurde -> Starte die Activity mit einer URL -> Starte Hauptbrowser vom Smartphone
    @Override
    public void gotoFriendPage(Friend friend) {
        String url = "https://myanimelist.net/profile/"+friend.getUsername();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}
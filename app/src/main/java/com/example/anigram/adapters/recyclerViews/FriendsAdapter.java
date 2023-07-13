package com.example.anigram.adapters.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anigram.model.Friend;
import com.example.anigram.R;
import com.example.anigram.model.Review;
import com.example.anigram.viewHolders.FriendViewHolder;

import java.util.ArrayList;
import java.util.List;

//RecyclerView Adapter für FriendsList
public class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {

    //List von Friends
    private List<Friend> friends;

    //Interface um OnClick zu simulieren
    private OnFriendClickInterface onFriendClickInterface;


    //Standart Constructor
    public FriendsAdapter(List<Friend> friends, OnFriendClickInterface onFriendClickInterface) {
        this.friends = friends;
        this.onFriendClickInterface = onFriendClickInterface;
    }

    //Methode um die Liste zu setzen
    public void setList(ArrayList<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    //Standard onCreateViewHolder Methode -> um die einzelne View zu deklarieren
    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        return new FriendViewHolder(view);
    }

    //Standart onBindViewHolder Methode -> wenn ein Item gesetzt wird
    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Setzt den derzeitigen Freund in die jetzige View
        Glide.with(holder.imageView.getContext()).load(friends.get(position).getProfilePicture()).into(holder.imageView);
        holder.textView.setText(friends.get(position).getUsername());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFriendClickInterface.gotoFriendPage(friends.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.friends != null) {
            return this.friends.size();
        }
        return 0;
    }

    //Erstellung der Interface um die bei der Fragment Klasse zu benutzen
    public interface OnFriendClickInterface{
        void gotoFriendPage(Friend friend);
    }
}


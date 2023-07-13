package com.example.anigram.adapters.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anigram.R;
import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.viewHolders.HomeViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeViewHolder> {

    //Liste der Anime
    private List<CustomAnimeObject> anime;

    //Interface um OnClick zu simulieren
    private ClickListener clickListener;


    //Standart Constructor
    public HomeAdapter(List<CustomAnimeObject> anime, ClickListener clickListener) {
        this.anime = anime;
        this.clickListener = clickListener;
    }

    //Methode um die Liste zu setzen
    public void setList(ArrayList<CustomAnimeObject> anime) {
        this.anime = anime;
        notifyDataSetChanged();
    }

    //Standard onCreateViewHolder Methode -> um die einzelne View zu deklarieren
    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
        return new HomeViewHolder(view);
    }

    //Standard onBindViewHolder Methode -> wenn ein Item gesetzt wird
    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Setzt den derzeitigen Anime in die entsprechende View
        Glide.with(holder.imageView.getContext()).load(anime.get(position).getImageUrl()).into(holder.imageView);
        holder.textView.setText(anime.get(position).getTitle());

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.gotoAnimeDetails(holder.imageView, anime.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(this.anime != null) {
            return this.anime.size();
        }
        return 0;
    }

    //Erstellung der Interface um die bei der Fragment Klasse zu benutzen
    public interface ClickListener{
        void gotoAnimeDetails(ImageView view, CustomAnimeObject anime);
    }
}

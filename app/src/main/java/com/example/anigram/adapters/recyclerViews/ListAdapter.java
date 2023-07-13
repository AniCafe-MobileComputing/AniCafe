package com.example.anigram.adapters.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anigram.R;
import com.example.anigram.viewHolders.ListViewHolder;

import java.util.List;

import dev.katsute.mal4j.anime.AnimeListStatus;
import dev.katsute.mal4j.property.Genre;

public class ListAdapter extends RecyclerView.Adapter<ListViewHolder> {

    //Deklarierung der Variablen
    private java.util.List<AnimeListStatus> anime;
    private ClickListener clickListener;

    //Standart Konstruktur
    public ListAdapter(List<AnimeListStatus> anime, ClickListener clickListener) {
        this.anime = anime;
        this.clickListener = clickListener;
    }

    //Setzt die derzeitige Liste -> in dem Fall immer wenn sogar die Liste gefiltert wird
    public void setSearchedList(List<AnimeListStatus> searchedList) {
        this.anime = searchedList;
        notifyDataSetChanged(); //gibt der recyclerview bescheid und aktualisiert diese
    }

    //benachrichtigt die recyclerview wenn ein Item gelöscht wird (löschen aus der Liste wird im Fragment gemacht)
    public void removeItem(int position) {
        notifyItemRemoved(position);
    }

    //Standart onCreateViewHolder Methode -> setzt die .xml als einzelnes item
    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ListViewHolder(view);
    }

    //Pro Array wird quasi diese Methode ausgeführt
    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Setzt für das derzeitige Item die entsprechenden Werte aus dem Array
        AnimeListStatus animeListStatus = anime.get(position);
        Glide.with(holder.animePicture.getContext()).load(animeListStatus.getAnime().getMainPicture().getLargeURL()).into(holder.animePicture);
        Glide.with(holder.backgroundImage.getContext()).load(animeListStatus.getAnime().getMainPicture().getLargeURL()).into(holder.backgroundImage);
        holder.rating.setText(String.valueOf(animeListStatus.getAnime().getMeanRating()));
        holder.episodes.setText(String.valueOf(animeListStatus.getWatchedEpisodes()) + " Eps");
        holder.animeTitle.setText(animeListStatus.getAnime().getTitle());

        if(animeListStatus.getScore() >= 0) holder.ratingSelf.setText(animeListStatus.getScore().toString());

        //Erstellt den Meta Text beim item (das graue unten)
        String genreStringBuilder = "";
        for (int i = 0; i < animeListStatus.getAnime().getGenres().length; i++) {
            Genre genre = animeListStatus.getAnime().getGenres()[i];
            genreStringBuilder += genre.getName();
            if(i < animeListStatus.getAnime().getGenres().length-1) genreStringBuilder += ", ";
        }
        String metaString = animeListStatus.getAnime().getType().toString() + " (" + animeListStatus.getAnime().getEpisodes() + " eps) - " + genreStringBuilder ;
        holder.meta.setText(metaString);

        //Setzt bedingt den Badge
        if(animeListStatus.getStatus().toString().toLowerCase().equals("completed")) { holder.listStatus.setText("CMP"); holder.listStatus.setBackground(ContextCompat.getDrawable(holder.animePicture.getContext(), R.drawable.background_chip_completed));}
        else if(animeListStatus.getStatus().toString().toLowerCase().equals("onhold")) { holder.listStatus.setText("OH"); holder.listStatus.setBackground(ContextCompat.getDrawable(holder.animePicture.getContext(), R.drawable.background_chip_onhold));}
        else if(animeListStatus.getStatus().toString().toLowerCase().equals("watching")) { holder.listStatus.setText("CW"); holder.listStatus.setBackground(ContextCompat.getDrawable(holder.animePicture.getContext(), R.drawable.background_chip_currentlywatching));}
        else if(animeListStatus.getStatus().toString().toLowerCase().equals("plantowatch")) { holder.listStatus.setText("PTW"); holder.listStatus.setBackground(ContextCompat.getDrawable(holder.animePicture.getContext(), R.drawable.background_chip_plantowatch));}
        else if(animeListStatus.getStatus().toString().toLowerCase().equals("dropped")) { holder.listStatus.setText("DP"); holder.listStatus.setBackground(ContextCompat.getDrawable(holder.animePicture.getContext(), R.drawable.background_chip_dropped));}

//        blurBackground(holder.rootView);

        //Setzt die onClickListener -> ruft Anime Detail auf wenn auf ein Bild geklickt wird
        holder.animePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.gotoAnimeDetails(holder.animePicture, anime.get(position));
            }
        });

        //Ruft den Dialog auf wenn auf edit Button geklickt wird
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.openEditAnime(anime.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return anime.size();
    }

    //Interface Erstellung
    public interface ClickListener{
        void gotoAnimeDetails(ImageView view, AnimeListStatus anime);
        void openEditAnime(AnimeListStatus animeListStatus, int position);
    }

}


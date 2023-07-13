package com.example.anigram.viewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigram.R;

//ViewHolder um UI Elemente festzuhalten -> Für RecyclerView
public class ListViewHolder extends RecyclerView.ViewHolder {

    //Deklarierung UI-Elemente
    public ImageView backgroundImage, animePicture, editButton;
    public TextView animeTitle, rating, episodes, meta, listStatus, ratingSelf;
    public ViewGroup rootView;

    //Konstruktur
    public ListViewHolder(@NonNull View itemView) {
        super(itemView);
        backgroundImage = itemView.findViewById(R.id.list_background_image);
        animePicture = itemView.findViewById(R.id.list_main_picture);
        listStatus = itemView.findViewById(R.id.list_status);
        animeTitle = itemView.findViewById(R.id.list_title);
        rating = itemView.findViewById(R.id.list_rating);
        ratingSelf = itemView.findViewById(R.id.list_self_rating);
        episodes = itemView.findViewById(R.id.list_watched_episodes);
        editButton = itemView.findViewById(R.id.list_edit_button);
        meta = itemView.findViewById(R.id.list_meta);
        rootView = (ViewGroup) itemView.findViewById(R.id.list_item);
    }
}

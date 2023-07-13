package com.example.anigram.viewHolders;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigram.R;

//ViewHolder um UI Elemente festzuhalten -> Für RecyclerView
public class SearchViewHolder extends RecyclerView.ViewHolder {

    //Deklarierung UI-Elemente
    public ImageView imageView;
    public TextView textView;
    public ViewGroup rootView;

    //Konstruktur
    public SearchViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.search_grid_picture);
        textView = itemView.findViewById(R.id.search_grid_text);
        rootView = (ViewGroup) itemView.findViewById(R.id.item_search);
    }
}
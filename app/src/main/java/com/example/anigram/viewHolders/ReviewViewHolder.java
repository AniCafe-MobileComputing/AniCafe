package com.example.anigram.viewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigram.R;
import com.google.android.material.imageview.ShapeableImageView;

//ViewHolder um UI Elemente festzuhalten -> Für RecyclerView
public class ReviewViewHolder extends RecyclerView.ViewHolder {

    //Deklarierung UI-Elemente
    public ShapeableImageView profilePicture;
    public TextView username, ranking, text, tag;
    public LinearLayout chipGroup;

    //Konstruktur
    public ReviewViewHolder(@NonNull View itemView) {
        super(itemView);
        this.profilePicture = itemView.findViewById(R.id.review_profilePicture);
        this.username = itemView.findViewById(R.id.review_username);
        this.ranking = itemView.findViewById(R.id.review_ranking);
        this.text = itemView.findViewById(R.id.review_text);
        this.tag = itemView.findViewById(R.id.review_tag);
        this.chipGroup = itemView.findViewById(R.id.chip_group);
    }
}

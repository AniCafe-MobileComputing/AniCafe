package com.example.anigram.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anigram.R;

//ViewHolder um UI Elemente festzuhalten -> Für RecyclerView
public class FriendViewHolder extends RecyclerView.ViewHolder {

    //Deklarierung UI-Elemente
    public ImageView imageView;
    public TextView textView;

    //Konstruktur
    public FriendViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.friend_image_item);
        textView = itemView.findViewById(R.id.friend_text_image);
    }
}
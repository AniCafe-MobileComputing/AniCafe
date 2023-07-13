package com.example.anigram.adapters.recyclerViews;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anigram.R;
import com.example.anigram.model.CustomAnimeObject;
import com.example.anigram.model.Reaction;
import com.example.anigram.model.Review;
import com.example.anigram.viewHolders.ReviewViewHolder;

import java.util.ArrayList;

public class ReviewsAdapter  extends RecyclerView.Adapter<ReviewViewHolder> {

    //Variablen
    private ArrayList<Review> reviews;

    //Standart Konstruktur
    public ReviewsAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    //Setzt die Liste und aktualisiert diese entsprechend
    public void setList(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    //Gibt der RecyclerView die .xml view als einzelnes Item
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_item, parent, false);
        return new ReviewViewHolder(view);
    }

    //Pro Array wird quasi diese Methode ausgeführt
    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {

        //Setzt für die entsprechende View das derzeitige Item -> Array.position ist die derzeitige Review
        holder.username.setText(reviews.get(position).getUsername());
        Glide.with(holder.profilePicture.getContext()).load(reviews.get(position).getProfilePicture()).into(holder.profilePicture);
        holder.ranking.setText(reviews.get(position).getRanking());
        holder.text.setText(reviews.get(position).getReview());
        holder.tag.setText(reviews.get(position).getTag());

        //Erstellt die ChipGroup (Custom LinearLayout) -> Die Liste die unten in jeder Review steht
        holder.chipGroup.removeAllViews();
        for (Reaction reaction: reviews.get(position).getReactions()) {
            if(reaction.getCount() > 0) {
                CardView chip = (CardView) LayoutInflater.from(holder.username.getContext()).inflate(R.layout.single_chip, holder.chipGroup,false);
                ((TextView)chip.findViewById(R.id.card_text)).setText(String.valueOf(reaction.getCount()));
                ((TextView)chip.findViewById(R.id.card_text)).setTooltipText(reaction.getTitle());

                switch (reaction.getTitle()) {
                    case "overall":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_overall,0,0,0);
                        break;
                    case "love_it":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_love_it,0,0,0);
                        break;
                    case "well_written":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_well_written,0,0,0);
                        break;
                    case "funny":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_funny,0,0,0);
                        break;
                    case "informative":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_informative,0,0,0);
                        break;
                    case "confusing":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_confusing,0,0,0);
                        break;
                    case "creative":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_creative,0,0,0);
                        break;
                    case "nice":
                        ((TextView)chip.findViewById(R.id.card_text)).setCompoundDrawablesWithIntrinsicBounds(R.drawable.reaction_nice,0,0,0);
                        break;
                }
                holder.chipGroup.addView(chip);
            }
        }
    }

    @Override
    public int getItemCount() {
        if(this.reviews != null) {
            return this.reviews.size();
        }
        return 0;
    }
}


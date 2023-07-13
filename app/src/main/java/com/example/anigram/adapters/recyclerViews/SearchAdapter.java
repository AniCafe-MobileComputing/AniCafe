package com.example.anigram.adapters.recyclerViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.anigram.R;
import com.example.anigram.viewHolders.SearchViewHolder;

import java.util.List;

import dev.katsute.mal4j.anime.Anime;
import eightbitlab.com.blurview.BlurView;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    //Deklarierung der Variablen
    private List<Anime> anime;
    private ClickListener clickListener;
    private int lastPosition = -1; //Hilfs Variable um zu gucken ob die Animation für eine Position schon stattgefunden hat

    //Standart Konstruktur
    public SearchAdapter(List<Anime> anime, ClickListener clickListener) {
        this.anime = anime;
        this.clickListener = clickListener;
    }

    //Methode um die Liste zu setzen und die RecyclerView zu aktualisieren
    public void setSearchedList(List<Anime> searchedList) {
        this.anime = searchedList;
        lastPosition = -1;
        notifyDataSetChanged();
    }

    //OnCreate Methode -> Sagt nur welche View pro Item benutzt wird
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    //Wird aufgerufen, wenn das Item dem User gezeigt wird
    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, @SuppressLint("RecyclerView") int position) {

        //Setzt simple die derzeitige Position im Array in die entsprechende View
        Glide.with(holder.imageView.getContext()).load(anime.get(position).getMainPicture().getLargeURL()).into(holder.imageView);
        holder.textView.setText(anime.get(position).getTitle());

        //Ruft die Methode auf, die für die Animation verantwortlich ist
        setAnimation(holder.rootView, position);

        //Setzt den onClickListener auf das Anime Bild
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.gotoAnimeDetails(holder.imageView, anime.get(position));
            }
        });
    }

    //Methode die das Item reinfaden / rotaten lässt
    private void setAnimation(View viewToAnimate, int position) {
        //In anderen Worten, falls das Item noch nicht angezeigt wurde -> rufe die Animation auf. Dies wird gemacht um zu verhindern, dass ein Item mehrfach die Animation anzeigt.
        if (position > lastPosition) {
            lastPosition = position;
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_anim);
            viewToAnimate.startAnimation(animation);
        }
    }

    @Override
    public int getItemCount() {
        return anime.size();
    }

    //Interface Deklarierung
    public interface ClickListener{
        void gotoAnimeDetails(ImageView view, Anime anime);
    }

}

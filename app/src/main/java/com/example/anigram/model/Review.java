package com.example.anigram.model;

import com.example.anigram.ui.animeDetailFragments.ReviewsFragment;

import java.util.ArrayList;

//Objekt welches für die einzelne Review unter den Details steht.
public class Review {

    //Variablen
    private String profilePicture; //Url des Profilbildes
    private String username;
    private String review; //Der ganze Text
    private String ranking;
    private String tag;
    private ArrayList<Reaction> reactions; //ArrayList der Reaktionen

    //Konstruktur
    public Review(String profilePicture, String username, String review, String ranking, String tag, ArrayList<Reaction> reactions) {
        this.profilePicture = profilePicture;
        this.username = username;
        this.review = review;
        this.ranking = ranking;
        this.tag = tag;
        this.reactions = reactions;
    }

    //Getter
    public String getProfilePicture() {
        return profilePicture;
    }

    public String getUsername() {
        return username;
    }

    public String getReview() {
        return review;
    }

    public String getRanking() {
        return ranking;
    }

    public String getTag() {
        return tag;
    }

    public ArrayList<Reaction> getReactions() {return reactions;}
}

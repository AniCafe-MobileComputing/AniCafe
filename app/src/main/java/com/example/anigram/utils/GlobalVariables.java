package com.example.anigram.utils;

import java.util.ArrayList;

import dev.katsute.mal4j.MyAnimeList;
import dev.katsute.mal4j.anime.AnimeListStatus;
import dev.katsute.mal4j.user.User;

public class GlobalVariables  {


    //Globale Variablen
    public static String CLIENT_ID = "fd2abb04ba75f6b24959fb3aa0f3283a"; //API Client ID
    public static MyAnimeList mal; //MyAnimeList API Client
    public static User authenticatedUser; //Der angemeldete Benutzer
    public static ArrayList<AnimeListStatus> animeList; //Offline AnimeList -> sorgt für schnelleres Laden der ganzen List.
    
}

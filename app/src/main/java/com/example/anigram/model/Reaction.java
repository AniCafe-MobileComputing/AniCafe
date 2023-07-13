package com.example.anigram.model;

//Objekt welches für die einzelne Reaktion verwendet wird -> Reaktion ist was in einem Review ist.
public class Reaction {

    //Deklarierung der Variablen
    private String title;
    private int count; //Count -> Reaktionen der Benutzer auf die Review

    //Konstruktur
    public Reaction(String title, int count) {
        this.title = title;
        this.count = count;
    }

    //Getter
    public String getTitle() {
        return title;
    }

    public int getCount() {
        return count;
    }

}

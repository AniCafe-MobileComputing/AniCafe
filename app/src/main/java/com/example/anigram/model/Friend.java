package com.example.anigram.model;

//Objekt welches für die Friendslist verwendet wird.
public class Friend {

    //Deklarierung der Variablen (Selbsterklärend)
    private String profilePicture;
    private String username;

    //Konstruktur
    public Friend(String profilePicture, String username) {
        this.profilePicture = profilePicture;
        this.username = username;
    }

    //Getter
    public String getUsername() {
        return username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}

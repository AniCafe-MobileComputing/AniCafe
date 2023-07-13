package com.example.anigram.model;

//Das Objekt welches die Anime für z.B. Trendings, Seasonal, Random speichert
public class CustomAnimeObject {

        //Deklarierung der Variablen
        private Long id; //Die ID für den Anime in MyAnimeList
        private String title; //Den Anime Titel
        private String synopsis; //Die Zusammenfassung des Anime
        private String imageUrl; //Die Bild URL vom Anime

        //Konstruktur ohne Zusammenfassung
        public CustomAnimeObject(String imageUrl, String title, Long id) {
            this.imageUrl = imageUrl;
            this.title = title;
            this.id = id;
        }

        //Konstruktur mit Zusammenfassung -> für RandomAnime
        public CustomAnimeObject(Long id, String title, String synopsis, String imageUrl){
            this.id = id;
            this.title = title;
            this.synopsis = synopsis;
            this.imageUrl = imageUrl;
        }

        //Getter
        public String getImageUrl() {
            return imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public Long getId() {
            return id;
        }

        public String getSynopsis() { return synopsis; }
}

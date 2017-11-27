package com.apps.nishtha.lyrics.Model;

/**
 * Created by nishtha on 21/11/17.
 */

public class FavModel {
    private int id;
    private String lyrics;
    private String title;
    private String artist;

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getLyrics() {
        return lyrics;
    }

    public FavModel(String lyrics, String title, String artist) {
        this.lyrics = lyrics;
        this.title = title;
        this.artist=artist;
    }
}

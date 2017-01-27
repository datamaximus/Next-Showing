package com.jasonwiram.nextshowing.Model;

import java.util.HashMap;

public class Genre {
    public HashMap<Integer, String> genreIds = new HashMap<>();

    public Genre() {
        genreIds.put(28, "Action");
        genreIds.put(12, "Adventure");
        genreIds.put(16, "Animation");
        genreIds.put(35, "Comedy");
        genreIds.put(80, "Crime");
        genreIds.put(99, "Documentary");
        genreIds.put(18, "Drama");
        genreIds.put(10751, "Family");
        genreIds.put(14, "Fantasy");
        genreIds.put(36, "History");
        genreIds.put(27, "Horror");
        genreIds.put(10402, "Music");
        genreIds.put(9648, "Mystery");
        genreIds.put(10749, "Romance");
        genreIds.put(878, "Science Fiction");
        genreIds.put(10770, "TV Movie");
        genreIds.put(53, "Thriller");
        genreIds.put(10752, "War");
        genreIds.put(37, "Western");
    }
}
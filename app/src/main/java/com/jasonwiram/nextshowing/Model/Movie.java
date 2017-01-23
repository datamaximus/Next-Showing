package com.jasonwiram.nextshowing.Model;

public class Movie {
    private String mTitle;
    private String mOverview;
    private String mPoster;
    private String mRating;
    private int[] mGenres;
    private double mPopularity;
    private int mId;
    private String mReleaseDate;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = "http://image.tmdb.org/t/p/w780/" + poster;
    }

    public String getRating() {
        return mRating;
    }

    public void setRating(double rating) {
        mRating = "Rating: " + Double.toString(rating);
    }

    public int[] getGenres() {
        return mGenres;
    }

    public void setGenres(int[] genres) {
        mGenres = genres;
    }

    public double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(double popularity) {
        mPopularity = popularity;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = "Year: " + releaseDate.substring(0,4);
    }
}

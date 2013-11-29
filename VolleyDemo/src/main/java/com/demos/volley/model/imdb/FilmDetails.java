package com.demos.volley.model.imdb;

import com.google.gson.annotations.SerializedName;

public class FilmDetails {
    @SerializedName("Title") private String mTitle;
    @SerializedName("Year") private int mYear;
    @SerializedName("Genre") private String mGenre;
    @SerializedName("Actors") private String mActors;
    @SerializedName("Plot") private String mSummary;
    @SerializedName("imdbRating") private float mRating;

    public String getTitle() {
        return mTitle;
    }

    public String getGenre() {
        return mGenre;
    }

    public String getActors() {
        return mActors;
    }

    public String getSummary() {
        return mSummary;
    }

    public float getRating() {
        return mRating;
    }

    public int getYear() {
        return mYear;
    }
}

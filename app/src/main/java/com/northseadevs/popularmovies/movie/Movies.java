package com.northseadevs.popularmovies.movie;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Movies {

    @SerializedName("results")
    private ArrayList<Movie> movies = new ArrayList<>();

    public ArrayList<Movie> getMovies(){
        return movies;
    }
}

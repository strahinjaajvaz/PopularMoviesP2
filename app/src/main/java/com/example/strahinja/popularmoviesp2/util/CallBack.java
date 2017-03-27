package com.example.strahinja.popularmoviesp2.util;

import com.example.strahinja.popularmoviesp2.model.Movie.Movie;

import java.util.List;

public interface CallBack {
        void setMovieList(List<Movie> movies, boolean isFavourite);
        void removeMovie(Movie movie);
        void addMovie(Movie movie);
        void setPosition(int position);
}

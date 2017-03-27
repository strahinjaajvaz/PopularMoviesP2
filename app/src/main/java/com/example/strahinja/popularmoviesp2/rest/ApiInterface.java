package com.example.strahinja.popularmoviesp2.rest;

import com.example.strahinja.popularmoviesp2.model.Movie.MoviesResponse;
import com.example.strahinja.popularmoviesp2.model.Review.ReviewResponse;
import com.example.strahinja.popularmoviesp2.model.Trailer.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies (@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies (@Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailerResponse> getTrailersForMovie (@Path("id") long id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewResponse> getReviewsForMovie (@Path("id") long id, @Query("api_key") String apiKey);
}

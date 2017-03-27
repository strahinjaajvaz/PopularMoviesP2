package com.example.strahinja.popularmoviesp2.rest;


import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.strahinja.popularmoviesp2.BuildConfig;
import com.example.strahinja.popularmoviesp2.R;
import com.example.strahinja.popularmoviesp2.activity.MovieListActivity;
import com.example.strahinja.popularmoviesp2.adapter.MovieReviewAdapter;
import com.example.strahinja.popularmoviesp2.adapter.MovieTrailerAdapter;
import com.example.strahinja.popularmoviesp2.data.MovieContract.MovieEntry;
import com.example.strahinja.popularmoviesp2.model.Movie.Movie;
import com.example.strahinja.popularmoviesp2.model.Movie.MoviesResponse;
import com.example.strahinja.popularmoviesp2.model.Review.Review;
import com.example.strahinja.popularmoviesp2.model.Review.ReviewResponse;
import com.example.strahinja.popularmoviesp2.model.Trailer.Trailer;
import com.example.strahinja.popularmoviesp2.model.Trailer.TrailerResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FetchMoviesTask {

    public static final String TOP_RATED_TAG = "top_rated";
    public static final String POPULAR_TAG = "popular";
    public static final String FAVOURITE_TAG = "favourite";

    private Context mContext;
    private View mView;

    public FetchMoviesTask(Context context, View view) {
        mContext = context;
        mView = view;
    }

    public void fetchMovieData(boolean topRated) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call;

        if (topRated)
            call = apiService.getTopRatedMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
        else
            call = apiService.getPopularMovies(BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<MoviesResponse>() {

                         @Override
                         public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                             List<Movie> movies = response.body().getResults();
                             ((MovieListActivity) mContext).setMovieList(movies, false);
                         }

                         @Override
                         public void onFailure(Call<MoviesResponse> call, Throwable t) {
                         }
                     }
        );
    }

    public void fetchFavouritesData(){
        List<Movie> movies = new ArrayList<>();
        Cursor movieCursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI,
                MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);

        if(movieCursor != null && movieCursor.moveToFirst()){
            do{
                String posterPath = movieCursor.getString(MovieEntry.COLUMN_POSTER_INDEX);
                String overview = movieCursor.getString(MovieEntry.COLUMN_OVERVIEW_INDEX);
                String releaseDate = movieCursor.getString(MovieEntry.COLUMN_RELEASE_DATE_INDEX);
                long movieId = movieCursor.getLong(MovieEntry.COLUMN_MOVIE_ID_INDEX);
                String title = movieCursor.getString(MovieEntry.COLUMN_TITLE_INDEX);
                double voteAverage = movieCursor.getDouble(MovieEntry.COLUMN_VOTE_AVERATE_INDEX);
                movies.add(new Movie(posterPath, overview, releaseDate,movieId, title, voteAverage));
            }while(movieCursor.moveToNext());
        }

        ((MovieListActivity) mContext).setMovieList(movies, true);
    }

    public void fetchTrailerData(long movieId) {
        final RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.trailer_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext(), LinearLayoutManager.HORIZONTAL, false));

        ApiInterface apiServies = ApiClient.getClient().create(ApiInterface.class);
        Call<TrailerResponse> call = apiServies.getTrailersForMovie(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                List<Trailer> trailers = response.body().getResults();
                recyclerView.setAdapter(new MovieTrailerAdapter(trailers, mView.getContext()));
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {

            }
        });
    }

    public void fetchReviewData(long movieId) {
        final RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.review_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext(), LinearLayoutManager.VERTICAL, false));

        ApiInterface apiServies = ApiClient.getClient().create(ApiInterface.class);
        Call<ReviewResponse> call = apiServies.getReviewsForMovie(movieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                List<Review> reviews = response.body().getResults();
                recyclerView.setAdapter(new MovieReviewAdapter(reviews, mView.getContext()));
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

            }
        });
    }
}

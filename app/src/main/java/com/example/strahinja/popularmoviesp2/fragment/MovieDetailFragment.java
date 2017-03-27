package com.example.strahinja.popularmoviesp2.fragment;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.strahinja.popularmoviesp2.R;
import com.example.strahinja.popularmoviesp2.activity.MovieListActivity;
import com.example.strahinja.popularmoviesp2.data.MovieContract.MovieEntry;
import com.example.strahinja.popularmoviesp2.model.Movie.Movie;
import com.example.strahinja.popularmoviesp2.rest.FetchMoviesTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment {

    public static final String MOVIE_TAG = "movieDetails";

    private Movie mMovie;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(MOVIE_TAG)) {
            mMovie = new Gson().fromJson(getArguments().getString(MOVIE_TAG), Movie.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (mMovie != null) {
            ((TextView) rootView.findViewById(R.id.movie_title)).setText(mMovie.getTitle());
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/" + mMovie.getPosterPath())
                    .error(R.drawable.noimagefound).into((ImageView)rootView.findViewById(R.id.image_poster));
            ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(mMovie.getReleaseDate());
            ((TextView) rootView.findViewById(R.id.movie_rating)).setText("" + mMovie.getVoteAverage());
            ((TextView) rootView.findViewById(R.id.movie_description)).setText(mMovie.getOverview());

            FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getContext(), rootView);
            fetchMoviesTask.fetchReviewData(mMovie.getId());
            fetchMoviesTask.fetchTrailerData(mMovie.getId());

            ((ImageButton) rootView.findViewById(R.id.star)).setImageResource(isFavourite(mMovie) ?R.drawable.star_favourite: R.drawable.star);

            rootView.findViewById(R.id.star).setOnClickListener((v) -> {
                if(isFavourite(mMovie)){
                    ((ImageButton) rootView.findViewById(R.id.star)).setImageResource(R.drawable.star);
                    removeFromFavourites(mMovie);
                } else {
                    ((ImageButton) rootView.findViewById(R.id.star)).setImageResource(R.drawable.star_favourite);
                    addToFavourites(mMovie);
                }
            });
        }

        return rootView;
    }

    private boolean isFavourite(Movie movie) {
        Cursor movieCursor = getContext().getContentResolver().query(
                MovieEntry.CONTENT_URI,
                new String[]{MovieEntry.COLUMN_MOVIE_ID},
                MovieEntry.COLUMN_MOVIE_ID + " = " + movie.getId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else  return false;
    }

    private void addToFavourites(Movie movie){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, movie.getOverview());
        contentValues.put(MovieEntry.COLUMN_POSTER, movie.getPosterPath());
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(MovieEntry.COLUMN_TITLE, movie.getTitle());
        contentValues.put(MovieEntry.COLUMN_VOTE_AVERATE, movie.getVoteAverage());

        getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues);
        if(MovieListActivity.mTwoPane)
            ((MovieListActivity) getActivity()).addMovie(movie);
        Toast.makeText(getContext(), R.string.added_to_favourites_toast_message, Toast.LENGTH_SHORT).show();
    }

    private void removeFromFavourites(Movie movie){
        int mRowsDeleted = 0;

        String mSelectionClause = MovieEntry.COLUMN_MOVIE_ID + " LIKE ?";
        String[] mSelectionArgs = {""+ movie.getId()};

        mRowsDeleted = getContext().getContentResolver().delete(
                MovieEntry.CONTENT_URI,
                mSelectionClause,
                mSelectionArgs
        );
        if(MovieListActivity.mTwoPane)
            ((MovieListActivity) getActivity()).removeMovie(movie);
        Toast.makeText(getContext(), R.string.removed_from_favourites_toast_message, Toast.LENGTH_SHORT).show();
    }
}

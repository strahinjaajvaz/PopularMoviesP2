package com.example.strahinja.popularmoviesp2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.strahinja.popularmoviesp2.R;
import com.example.strahinja.popularmoviesp2.fragment.MovieDetailFragment;
import com.example.strahinja.popularmoviesp2.model.Movie.Movie;
import com.example.strahinja.popularmoviesp2.rest.FetchMoviesTask;
import com.example.strahinja.popularmoviesp2.util.CallBack;
import com.example.strahinja.popularmoviesp2.util.Utilities;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieListActivity extends AppCompatActivity implements CallBack {
    public static boolean mTwoPane;
    private RecyclerView mRecycleView;
    private RecyclerView.Adapter mMovieAdapter;

    private String mSearchFor = FetchMoviesTask.TOP_RATED_TAG;
    private static String ADAPTER_POSITION = "adapter_position";
    private int mPosition = 0;

    private static boolean initialRun = true;

    private static View mLoadingSpinner;
    private static List<Movie> mMoviesList;

    @Override
    protected void onStart() {
        super.onStart();

        if (initialRun) {
            initialRun = false;
            mSearchFor = Utilities.getSearchCriteria(this);
            selectSort();
            return;
        }

        String tempSearch = Utilities.getSearchCriteria(this);
        // we know the value has been changed
        if (!tempSearch.equals(mSearchFor) || tempSearch.equals(FetchMoviesTask.FAVOURITE_TAG)) {
            mSearchFor = tempSearch;
            selectSort();
            return;
        }
        // if it is the same value as before
        setMovieList(mMoviesList, mSearchFor.equals(FetchMoviesTask.FAVOURITE_TAG));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition != -1)
            outState.putInt(ADAPTER_POSITION, mPosition);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        mLoadingSpinner = findViewById(R.id.loading_spinner);

        if (Utilities.checkNetwork(this)) {
            LinearLayout noConnectionView = (LinearLayout) findViewById(R.id.no_connection);
            noConnectionView.setVisibility(View.GONE);

            mRecycleView = (RecyclerView) findViewById(R.id.movie_list);
            assert mRecycleView != null;

            if (savedInstanceState != null && savedInstanceState.containsKey(ADAPTER_POSITION))
                mPosition = savedInstanceState.getInt(ADAPTER_POSITION);

            mTwoPane = findViewById(R.id.movie_detail_container) != null;
        } else {
            mLoadingSpinner.setVisibility(View.GONE);
        }
    }

    private void selectSort() {
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(this, null);
        switch (mSearchFor) {
            case "top_rated": {
                fetchMoviesTask.fetchMovieData(true);
                setTitle("Top Rated");
                break;
            }
            case "popular": {
                fetchMoviesTask.fetchMovieData(false);
                setTitle("Popular");
                break;
            }
            case "favourite": {
                fetchMoviesTask.fetchFavouritesData();
                setTitle("Favourite");
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.settings_id: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public void addMovie(Movie movie) {
        if (mTwoPane) {
            mMoviesList.add(movie);
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removeMovie(Movie movie) {
        if (mTwoPane) {
            for (Movie m : mMoviesList) {
                if (m.getId() == movie.getId()) {
                    mMoviesList.remove(m);
                    break;
                }
            }
            mMovieAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void setMovieList(List<Movie> movies, boolean isFavourite) {
        mMoviesList = movies;
        if (mMoviesList != null) {

            if (isFavourite && mMoviesList.size() == 0) {
                findViewById(R.id.no_connection).setVisibility(View.VISIBLE);
                ((ImageView) findViewById(R.id.no_connection_image)).setImageResource(R.drawable.ic_do_not_disturb_black_24dp);
                ((TextView) findViewById(R.id.no_connection_text)).setText(R.string.no_favourite_movies_string);
            }
            mLoadingSpinner.setVisibility(View.GONE);
            mMovieAdapter = new MoviesAdapter(mMoviesList, this);
            mRecycleView.setLayoutManager(new GridLayoutManager(this, 2));
            mRecycleView.setAdapter(mMovieAdapter);
            if (mPosition != -1)
                mRecycleView.scrollToPosition(mPosition);
        }
    }

    @Override
    public void setPosition(int position) {
        mPosition = position;
    }

    public class MoviesAdapter
            extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

        private final List<Movie> mValues;
        Context mContext;

        public MoviesAdapter(List<Movie> items, Context context) {
            mValues = items;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_list_content, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final String BASE_POSTER_PATH = "http://image.tmdb.org/t/p/w185/";

            holder.mItem = mValues.get(position);

            Picasso.with(holder.mView.getContext()).load(BASE_POSTER_PATH + holder.mItem.getPosterPath()).error(R.drawable.noimagefound).into(holder.mImageView);
            holder.mView.setOnClickListener((v) -> {
                ((MovieListActivity) mContext).setPosition(position);
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(MovieDetailFragment.MOVIE_TAG, new Gson().toJson(holder.mItem));
                    MovieDetailFragment fragment = new MovieDetailFragment();
                    fragment.setArguments(arguments);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragment)
                            .commit();

                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(MovieDetailFragment.MOVIE_TAG, new Gson().toJson(holder.mItem));

                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView mImageView;
            public View mView;

            public Movie mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.image_poster);
            }
        }
    }
}

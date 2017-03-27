package com.example.strahinja.popularmoviesp2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.example.strahinja.popularmoviesp2.fragment.MovieDetailFragment;
import com.example.strahinja.popularmoviesp2.R;

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putString(MovieDetailFragment.MOVIE_TAG,
                    getIntent().getStringExtra(MovieDetailFragment.MOVIE_TAG));
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
    }
}

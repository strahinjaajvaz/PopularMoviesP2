package com.example.strahinja.popularmoviesp2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.strahinja.popularmoviesp2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_VOTE_AVERATE = "vote_average";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String[] MOVIE_COLUMNS = {
                COLUMN_POSTER,
                COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE,
                COLUMN_MOVIE_ID,
                COLUMN_TITLE,
                COLUMN_VOTE_AVERATE
        };

        public static final int COLUMN_POSTER_INDEX = 0;
        public static final int COLUMN_OVERVIEW_INDEX = 1;
        public static final int COLUMN_RELEASE_DATE_INDEX = 2;
        public static final int COLUMN_MOVIE_ID_INDEX = 3;
        public static final int COLUMN_TITLE_INDEX = 4;
        public static final int COLUMN_VOTE_AVERATE_INDEX = 5;

    }
}
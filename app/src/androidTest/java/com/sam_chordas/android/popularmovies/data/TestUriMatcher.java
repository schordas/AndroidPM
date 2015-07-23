package com.sam_chordas.android.popularmovies.data;

import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by sam_chordas on 7/10/15.
 */
public class TestUriMatcher extends AndroidTestCase {

    private static final String DETAIL_QUERY = "Up";
    private static final long TEST_DATE = 1419033600L;
    private static final long TEST_LOCATION_ID = 10L;

    private static final Uri TEST_MOVIE_DIR = MovieContract.MovieEntry.CONTENT_URI;
    private static final Uri TEST_MOVIE_WITH_DETAIL_DIR = MovieContract.MovieEntry
            .buildMovieDetail(DETAIL_QUERY);
    private static final Uri TEST_MOVIE_WITH_DATE_DIR = MovieContract.MovieEntry
            .buildMovieWithStartDate(DETAIL_QUERY, TEST_DATE);

    private static final Uri TEST_DETAIL_ENTRY_DIR = MovieContract.DetailEntry.CONTENT_URI;
}

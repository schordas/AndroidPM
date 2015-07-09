package com.sam_chordas.android.popularmovies.data;

import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by sam_chordas on 7/9/15.
 */
public class MovieContract {

    public static long normalizeDate(long startDate){
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }


    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_DETS_KEY = "details_id";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_VOTE_AVG = "vote_avg";

        public static final String COLUMN_POSTER_URL = "poster_url";

        public static final String COLUMN_DATE = "date";
    }

    public static final class DetailEntry implements BaseColumns{
        public static final String TABLE_NAME = "details";

        public static final String COLUMN_TRLR_KEY = "trailer_id";

        public static final String COLUMN_REV_KEY = "review_id";

        public static final String COLUMN_RUNTIME = "runtime";
    }

    public static final class TrailerEntry implements BaseColumns{
        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_URL_KEY = "url_key";
    }

    public static final class ReviewEntry implements BaseColumns{
        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_REVIEW = "review";


    }
}

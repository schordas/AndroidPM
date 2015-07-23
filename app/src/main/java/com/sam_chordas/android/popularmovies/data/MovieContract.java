package com.sam_chordas.android.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.format.Time;

/**
 * Created by sam_chordas on 7/9/15.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.sam_chordas.android.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String DETAILS = "details";
    public static final String TRAILERS = "trailers";
    public static final String REVIEWS = "reviews";

    public static long normalizeDate(long startDate){
        Time time = new Time();
        time.set(startDate);
        int julianDay = Time.getJulianDay(startDate, time.gmtoff);
        return time.setJulianDay(julianDay);
    }


    public static final class MovieEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES)
                .build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + PATH_MOVIES;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY +"/" + PATH_MOVIES;

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieDetail(String detail){

            return CONTENT_URI.buildUpon().appendPath(detail).build();
        }

        public static Uri buildMovieWithStartDate(String movie, long startDate){
            long normalizedDate = normalizeDate(startDate);
            return CONTENT_URI.buildUpon().appendPath(movie)
            .appendQueryParameter(COLUMN_DATE,
                    Long.toString(normalizedDate)).build();
        }

        public static String getDetailFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static long getStartDateFromUri(Uri uri){
            String dateStr = uri.getQueryParameter(COLUMN_DATE);
            if (dateStr != null && dateStr.length() > 0){
                return Long.parseLong(dateStr);
            }
            else return 0;
        }

        public static String getReviewFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }

        public static String getTrailerFromUri(Uri uri){
            return uri.getPathSegments().get(1);
        }


        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_DETS_KEY = "details_id";

        public static final String COLUMN_MOVIE_ID = "movie_id";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_RELEASE_DATE = "release_date";

        public static final String COLUMN_VOTE_AVG = "vote_avg";

        public static final String COLUMN_POSTER_URL = "poster_url";

        public static final String COLUMN_DATE = "date";

        public static final String COLUMN_CREATED_AT = "created_at";
    }

    public static final class DetailEntry implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(DETAILS)
                .build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + DETAILS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + DETAILS;

        public static Uri buildDetaillUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMovieTrailers(String trailer){
            return CONTENT_URI.buildUpon().appendPath(trailer).build();
        }

        public static Uri buildMovieReviews(String review){
            return CONTENT_URI.buildUpon().appendPath(review).build();
        }
        public static final String TABLE_NAME = "details";

        public static final String COLUMN_TRLR_KEY = "trailer_id";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_REV_KEY = "review_id";

        public static final String COLUMN_RUNTIME = "runtime";
    }

    public static final class TrailerEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TRAILERS)
                .build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + TRAILERS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + TRAILERS;

        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }



        public static final String TABLE_NAME = "trailers";

        public static final String COLUMN_TRAILER_NAME = "trailer_name";

        public static final String COLUMN_URL_KEY = "url_key";

        public static final String COLUMN_MOVIE_ID = "id";
    }

    public static final class ReviewEntry implements BaseColumns{
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(REVIEWS)
                .build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + REVIEWS;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + REVIEWS;

        public static Uri buildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static final String TABLE_NAME = "reviews";

        public static final String COLUMN_REVIEW_NAME = "review_name";

        public static final String COLUMN_AUTHOR = "author";

        public static final String COLUMN_REVIEW = "review";

        public static final String COLUMN_MOVIE_ID = "id";


    }
}

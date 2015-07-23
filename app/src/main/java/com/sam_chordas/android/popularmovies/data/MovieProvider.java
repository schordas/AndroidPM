package com.sam_chordas.android.popularmovies.data;


import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by sam_chordas on 7/10/15.
 */
public class MovieProvider extends ContentProvider{
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    static final int EMPTY_CHECK = 1;
    static final int DATE_CHECK = 2;
    static final int MOVIE = 100;
    static final int MOVIE_WITH_DETAIL = 101;
    static final int MOVIE_WITH_DATE = 102;
    static final int DETAIL = 200;
    static final int DETAIL_WITH_REVIEW = 201;
    static final int DETAIL_WITH_TRAILER = 202;
    static final int REVIEW = 300;
    static final int TRAILER = 400;

    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    private static final SQLiteQueryBuilder sMovieDetailQueryBuilder;
    private static final SQLiteQueryBuilder sDetailReviewQueryBuilder;
    private static final SQLiteQueryBuilder sDetailTrailerQueryBuilder;

    static{
        sMovieDetailQueryBuilder = new SQLiteQueryBuilder();
        sDetailReviewQueryBuilder = new SQLiteQueryBuilder();
        sDetailTrailerQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder = new SQLiteQueryBuilder();

        sMovieDetailQueryBuilder.setTables(
                MovieContract.MovieEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.DetailEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_DETS_KEY +
                        " + " + MovieContract.DetailEntry.TABLE_NAME +
                        "." + MovieContract.DetailEntry.COLUMN_MOVIE_ID);
        sDetailReviewQueryBuilder.setTables(
                MovieContract.DetailEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.ReviewEntry.TABLE_NAME +
                        " ON " + MovieContract.DetailEntry.TABLE_NAME +
                        "." + MovieContract.DetailEntry.COLUMN_REV_KEY +
                        " + " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIE_ID);
        sDetailTrailerQueryBuilder.setTables(
                MovieContract.DetailEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.TrailerEntry.TABLE_NAME +
                        " ON " + MovieContract.DetailEntry.TABLE_NAME +
                        "." + MovieContract.DetailEntry.COLUMN_REV_KEY +
                        " + " + MovieContract.TrailerEntry.TABLE_NAME +
                        "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID);
    }


    private static final String sMovieSelection = MovieContract.MovieEntry.TABLE_NAME + " = ?";

    private static final String sDetailSelection = MovieContract.DetailEntry.TABLE_NAME +
            "." + MovieContract.DetailEntry.COLUMN_MOVIE_ID + " = ?";

    private static final String sTrailerSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.DetailEntry.COLUMN_TRLR_KEY + " = ? ";
    private static final String sReviewSelection = MovieContract.DetailEntry.TABLE_NAME +
            "." + MovieContract.DetailEntry.COLUMN_REV_KEY + " = ?";

//    private Cursor getMovie(Uri uri, String)

    private Cursor getDetailByMovie(Uri uri, String[] projection, String sort){
        String detail = MovieContract.MovieEntry.getDetailFromUri(uri);
        String[] selectionArgs;
        String selection;

        selection = sDetailSelection;
        selectionArgs = new String[]{detail};


        return sMovieDetailQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sort);
    }

    private Cursor getReviewByDeatail(Uri uri, String[] projection, String sort){
        String review = MovieContract.MovieEntry.getReviewFromUri(uri);

        return sDetailReviewQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sReviewSelection,
                new String[] {review},
                null, null,
                sort);
    }

    private Cursor getTrailerDetail(Uri uri, String[] projection, String sort){
        String trailer = MovieContract.MovieEntry.getTrailerFromUri(uri);

        return sDetailTrailerQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sTrailerSelection,
                new String[]{trailer},
                null, null,
                sort);
    }

    static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, "raw_query", EMPTY_CHECK);
        matcher.addURI(authority, "date_check", DATE_CHECK);
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*", DETAIL);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*/" + MovieContract.DETAILS, DETAIL);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*/" + MovieContract.DETAILS
                + MovieContract.TRAILERS, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*/" + MovieContract.DETAILS
                + MovieContract.REVIEWS, DETAIL_WITH_REVIEW);
        return matcher;
    }

    @Override
    public boolean onCreate(){
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri){
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case DETAIL:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case MOVIE_WITH_DETAIL:
                return MovieContract.MovieEntry.COLUMN_DETS_KEY;
            default:
                throw new UnsupportedOperationException("Unkown uri:" + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sort){
        Cursor retCursor;
        switch(sUriMatcher.match(uri)){
            case MOVIE_WITH_DETAIL: {
                retCursor = getDetailByMovie(uri, projection, sort);
                break;
            }
            case EMPTY_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM " +
                        MovieContract.MovieEntry.TABLE_NAME, null);
                break;
            }
            case DATE_CHECK:{
                retCursor = mOpenHelper.getReadableDatabase().rawQuery(
                        "SELECT " + MovieContract.MovieEntry.COLUMN_CREATED_AT + " FROM " +
                                MovieContract.MovieEntry.TABLE_NAME + " LIMIT " + 1, null
                );
                break;
            }
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sort);
                break;
            }
            case DETAIL:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.DetailEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sort);
                break;
            }
            case TRAILER:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sort);
                break;
            }
            case REVIEW:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sort);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match){
            case MOVIE: {
                normalizeDate(values);
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if (_id > 0){
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else{
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case DETAIL: {
                long _id = db.insert(MovieContract.DetailEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                   // returnUri = MovieContract.DetailEntry.buildDetaillUri(_idD);
                }
            }
            case TRAILER:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs){

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        //int [] tableRowsDeleted = new int [4];
        ArrayList<Integer> tableRowsDeleted = new ArrayList<Integer>();


        if (null == selection) selection = "1";
        switch (match){
            case MOVIE:
                tableRowsDeleted.add(db.delete(MovieContract.ReviewEntry.TABLE_NAME, selection,
                        selectionArgs));
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.ReviewEntry.TABLE_NAME + "'");
                tableRowsDeleted.add(db.delete(MovieContract.TrailerEntry.TABLE_NAME, selection,
                        selectionArgs));
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.TrailerEntry.TABLE_NAME + "'");
                tableRowsDeleted.add(db.delete(MovieContract.DetailEntry.TABLE_NAME, selection,
                        selectionArgs));
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.DetailEntry.TABLE_NAME + "'");
                tableRowsDeleted.add(db.delete(MovieContract.MovieEntry.TABLE_NAME, selection,
                        selectionArgs));
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE  NAME = '" +
                        MovieContract.MovieEntry.TABLE_NAME + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri " + uri);
        }

        if (tableRowsDeleted.size() == 4){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return tableRowsDeleted.get(3);
    }

    public void normalizeDate(ContentValues values) {
        if (values.containsKey(MovieContract.MovieEntry.COLUMN_DATE)) {

            long dateValue = values.getAsLong(MovieContract.MovieEntry.COLUMN_DATE);
            values.put(MovieContract.MovieEntry.COLUMN_DATE, MovieContract.normalizeDate(dateValue));
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match){
            case MOVIE:
                normalizeDate(values);
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if(rowsUpdated != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    public Uri customInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long trailer_id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values[0]);
        long review_id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values[1]);
        values[2].put(MovieContract.DetailEntry.COLUMN_TRLR_KEY, trailer_id);
        values[2].put(MovieContract.DetailEntry.COLUMN_REV_KEY, review_id);
        long detail_id = db.insert(MovieContract.DetailEntry.TABLE_NAME, null, values[2]);
        values[3].put(MovieContract.MovieEntry.COLUMN_DETS_KEY, detail_id);
        long movie_id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values[3]);

        Uri returnUri;

        if (movie_id > 0){
            returnUri = MovieContract.MovieEntry.buildMovieUri(movie_id);
        } else{
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int returnCount = 0;
        Log.i(LOG_TAG, "match: " + match);
        switch (match) {
            case MOVIE: {
                db.beginTransaction();
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case TRAILER:{
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                        if (_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case REVIEW:{
                db.beginTransaction();
                try{
                    for(ContentValues value : values){
                        long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                        if (_id != 1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();

                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

//        long [] _ids = new long[4];
//        String [] entries = new String[4];
//        entries[0] = MovieContract.TrailerEntry.TABLE_NAME;
//        entries[1] = MovieContract.ReviewEntry.TABLE_NAME;
//        entries[2] = MovieContract.DetailEntry.TABLE_NAME;
//        entries[3] = MovieContract.MovieEntry.TABLE_NAME;
//
//        switch (match){
//            case MOVIE:
//                Log.d(LOG_TAG, "Beginning Transaction");
//                db.beginTransaction();
//                int returnCount = 0;
//                Log.i(LOG_TAG, "Values Length: " + values.length);
//                try {
//                    for (int i = 0; i < values.length; i++){
//                        ContentValues value = values[i];
//                        //normalizeDate(value);
//                        long _id = db.insert(entries[i], null, value);
//                        Log.i(LOG_TAG, "_Id: " + _id);
//                        if (_id != -1){
//                            _ids[i] = _id;
//                            returnCount++;
//                            Log.i(LOG_TAG, "Return Count: " + returnCount);
//                            if(i == 2){
//                                values[i].put(MovieContract.DetailEntry.COLUMN_TRLR_KEY, _ids[0]);
//                                values[i].put(MovieContract.DetailEntry.COLUMN_REV_KEY, _ids[1]);
//                                db.update(MovieContract.DetailEntry.TABLE_NAME, values[i],
//                                        null, null);
//                            }
//                            if(i == 3){
//                                values[i].put(MovieContract.MovieEntry.COLUMN_DETS_KEY, _ids[2]);
//                                db.update(MovieContract.MovieEntry.TABLE_NAME, values[i],
//                                        null, null);
//                            }
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            default:
//                return super.bulkInsert(uri, values);
//        }


    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}

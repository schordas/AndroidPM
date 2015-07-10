package com.sam_chordas.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by sam_chordas on 7/9/15.
 */
public class MovieDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + " (" +

                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                MovieContract.MovieEntry.COLUMN_DETS_KEY + " INTEGER NOT NULL," +

                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +

                MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +

                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +

                MovieContract.MovieEntry.COLUMN_VOTE_AVG + " REAL NOT NULL," +

                MovieContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +

                MovieContract.MovieEntry.COLUMN_POSTER_URL + " TEXT NOT NULL," +

                MovieContract.MovieEntry.COLUMN_DATE + " INTEGER NOT NULL," +

                " FOREIGN KEY (" + MovieContract.MovieEntry.COLUMN_DETS_KEY + ") REFERENCES " +
                MovieContract.DetailEntry.TABLE_NAME + " (" + MovieContract.DetailEntry._ID +
                "), " + " UNIQUE (" + MovieContract.MovieEntry.COLUMN_DATE +
                ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_DETAILS_TABLE = "CREATE TABLE " +
                MovieContract.DetailEntry.TABLE_NAME + " (" +
                MovieContract.DetailEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.DetailEntry.COLUMN_RUNTIME + " REAL NOT NULL, " +
                MovieContract.DetailEntry.COLUMN_REV_KEY + " INTEGER NOT NULL, " +
                MovieContract.DetailEntry.COLUMN_TRLR_KEY + " INTEGER NOT NULL," +
                " FOREIGN KEY (" + MovieContract.DetailEntry.COLUMN_REV_KEY + ") REFERENCES " +
                MovieContract.ReviewEntry.TABLE_NAME + " (" + MovieContract.ReviewEntry._ID +
                ")," + " FOREIGN KEY (" + MovieContract.DetailEntry.COLUMN_TRLR_KEY + ") REFERENCES "
                + MovieContract.TrailerEntry.TABLE_NAME + " (" + MovieContract.TrailerEntry._ID +
                "));";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE " +
                MovieContract.ReviewEntry.TABLE_NAME + " (" +
                MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW + " TEXT NOT NULL);";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " +
                MovieContract.TrailerEntry.TABLE_NAME + " (" +
                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.TrailerEntry.COLUMN_URL_KEY + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.DetailEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

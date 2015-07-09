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
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE" +
                MovieContract.MovieEntry.TABLE_NAME + "(" +

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
                ") ON CONFLICT REPLACE)";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.DetailEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

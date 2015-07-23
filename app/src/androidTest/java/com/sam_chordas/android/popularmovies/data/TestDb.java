package com.sam_chordas.android.popularmovies.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

/**
 * Created by sam_chordas on 7/9/15.
 */
public class TestDb extends AndroidTestCase {
    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase(){
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.DetailEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.ReviewEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.TrailerEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without the movie entry and detail entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_DETS_KEY);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_POSTER_URL);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_VOTE_AVG);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_DATE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        }
        while(c.moveToNext());

        assertTrue("Error: The db doesn't contain all of the required movie entry columns",
                movieColumnHashSet.isEmpty());



        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> detailColumnHashSet = new HashSet<String>();
        detailColumnHashSet.add(MovieContract.DetailEntry._ID);
        detailColumnHashSet.add(MovieContract.DetailEntry.COLUMN_RUNTIME);
        detailColumnHashSet.add(MovieContract.DetailEntry.COLUMN_MOVIE_ID);
        detailColumnHashSet.add(MovieContract.DetailEntry.COLUMN_REV_KEY);
        detailColumnHashSet.add(MovieContract.DetailEntry.COLUMN_TRLR_KEY);



        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns


        final HashSet<String> reviewColumnHashSet = new HashSet<String>();
        reviewColumnHashSet.add(MovieContract.ReviewEntry._ID);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_REVIEW_NAME);
        reviewColumnHashSet.add(MovieContract.ReviewEntry.COLUMN_REVIEW);




        final HashSet<String> trailerColumnHashSet = new HashSet<String>();
        trailerColumnHashSet.add(MovieContract.TrailerEntry._ID);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME);
        trailerColumnHashSet.add(MovieContract.TrailerEntry.COLUMN_URL_KEY);
        db.close();
    }

    public long insertTrailer(){
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createTrailerValues();

        long trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, testValues);

        assertTrue(trailerRowId != -1);

        Cursor cursor = db.query(MovieContract.TrailerEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from trailer query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Trailer Query Validation Failed",
                cursor, testValues);

        assertFalse("Error: More than one record returned from Trailer query",
                cursor.moveToNext());

        cursor.close();
        db.close();

        return trailerRowId;
    }



    public long insertDetail(){
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues =
                TestUtilities.createFakeDetailValues(insertReview(), insertTrailer());

        long detailRowId;
        detailRowId = db.insert(MovieContract.DetailEntry.TABLE_NAME, null, testValues);

        assertTrue(detailRowId != -1);

        Cursor cursor = db.query(MovieContract.DetailEntry.TABLE_NAME,
                null, // leaving "columns" null just returns all the columns
                null, //cols for "where" clause
                null, // values for "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null); // sort order

        assertTrue("Error: No Records returned from detail query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Detail Query Validation Failed",
                cursor, testValues);

        assertFalse("Error: More than one record returned from detail query",
                cursor.moveToNext());

        cursor.close();
        db.close();

        return detailRowId;

    }

    public long insertReview(){
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues testValues = TestUtilities.createReviewValues();

        long reviewRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, testValues);

        assertTrue(reviewRowId != -1);

        Cursor cursor = db.query(MovieContract.ReviewEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from review query", cursor.moveToFirst());

        TestUtilities.validateCurrentRecord("Error: Review Query Validation Failed",
                cursor, testValues);

        assertFalse("Error: More than one record returned from review query",
                cursor.moveToNext());

        cursor.close();
        db.close();

        return reviewRowId;
    }

    public void testMovieTable(){

        long detailRowId = insertDetail();

        assertFalse("Error: Detail Not Inserted Correctly", detailRowId == -1L);

        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues movieValues = TestUtilities.createMovieValues(detailRowId);
        assertTrue(detailRowId != -1);

        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, movieValues);

        assertTrue(movieRowId != -1);

        Cursor movieCursor = db.query(MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: No Records returned from detail query", movieCursor.moveToFirst());

        TestUtilities.validateCurrentRecord("testInsertReadDb movieEntry failed to validate",
                movieCursor, movieValues);

        assertFalse("Error: More than one record returned from movie query",
                movieCursor.moveToNext());

        movieCursor.close();
        dbHelper.close();
    }

//    public void testDetailTable(){
//        insertDetail();
//    }


}

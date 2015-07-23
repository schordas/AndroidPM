package com.sam_chordas.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.test.AndroidTestCase;

import com.sam_chordas.android.popularmovies.utils.PollingCheck;

import java.util.Map;
import java.util.Set;

/**
 * Created by sam_chordas on 7/9/15.
 */
public class TestUtilities  extends AndroidTestCase {
    static final String TEST_DETAIL = "1";
    static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    /*
        Students: Use this to create some default weather values for your database tests.
     */
    static ContentValues createTrailerValues(){
        ContentValues trailerValues = new ContentValues();
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, "Trailer 1");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_URL_KEY, "fakeURL");

        return trailerValues;
    }

    static ContentValues[] createToyStory(){
        ContentValues reviewValues = new ContentValues();
        ContentValues trailerValues = new ContentValues();
        ContentValues detailValues = new ContentValues();
        ContentValues movieValues = new ContentValues();
        ContentValues [] allValues = new ContentValues[4];

        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_NAME, "Review1");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "Buzz");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW, "Best Movie EVER!");

        trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME, "Trailer1");
        trailerValues.put(MovieContract.TrailerEntry.COLUMN_URL_KEY, "fakeurl");

        detailValues.put(MovieContract.DetailEntry.COLUMN_MOVIE_ID, 123);
        detailValues.put(MovieContract.DetailEntry.COLUMN_RUNTIME, "120min");
        detailValues.put(MovieContract.DetailEntry.COLUMN_REV_KEY, 25);
        detailValues.put(MovieContract.DetailEntry.COLUMN_TRLR_KEY, 8);

        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 1234);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Toy Story");
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Pixar's first!");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, "fake_poster_url");
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, "9.9");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "1995");


        allValues[0] = trailerValues;
        allValues[1] = reviewValues;
        allValues[2] = detailValues;
        allValues[3] = movieValues;

        return allValues;
    }

    static ContentValues createReviewValues(){
        ContentValues reviewValues = new ContentValues();
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW_NAME, "Review 1");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, "WhoWritesThisThings");
        reviewValues.put(MovieContract.ReviewEntry.COLUMN_REVIEW, "You will see it no matter what I say");

        return reviewValues;
    }

    static ContentValues createMovieValues(long locationRowId) {
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_DETS_KEY, locationRowId);
        movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, TEST_DATE);
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, 1234);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, "Jaws");
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "CAGE GO IN THE WATER?...SHARK IN THE WATER.");
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, "something");
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "June 1975");
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, 9.5);

        return movieValues;
    }

    /*
        Students: You can uncomment this helper function once you have finished creating the
        LocationEntry part of the WeatherContract.
     */
    static ContentValues createFakeDetailValues(long reviewRowId, long trailerRowId) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.DetailEntry.COLUMN_RUNTIME, 120);
        testValues.put(MovieContract.DetailEntry.COLUMN_MOVIE_ID, 1456);
        testValues.put(MovieContract.DetailEntry.COLUMN_REV_KEY, reviewRowId);
        testValues.put(MovieContract.DetailEntry.COLUMN_TRLR_KEY, trailerRowId);

        return testValues;
    }

    /*
        Students: You can uncomment this function once you have finished creating the
        LocationEntry part of the WeatherContract as well as the WeatherDbHelper.
     */
    static long insertFakeDetailValues(Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper= new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues reviewValues = TestUtilities.createReviewValues();
        ContentValues trailerValues = TestUtilities.createTrailerValues();
        long reviewRowId = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, reviewValues);
        long trailerRowId = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, trailerValues);

        ContentValues testValues = TestUtilities.createFakeDetailValues(reviewRowId, trailerRowId);

        long detailRowId;
        detailRowId = db.insert(MovieContract.DetailEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Fake Values Values", detailRowId != -1);

        return detailRowId;
    }

    /*
        Students: The functions we provide inside of TestProvider use this utility class to test
        the ContentObserver callbacks using the PollingCheck class that we grabbed from the Android
        CTS tests.

        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}

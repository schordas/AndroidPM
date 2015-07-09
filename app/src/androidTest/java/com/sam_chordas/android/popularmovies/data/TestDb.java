package com.sam_chordas.android.popularmovies.data;

import android.test.AndroidTestCase;

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

    public void testMovieTable(){

    }

    public long insertDetail(){
        return -1L;
    }
}

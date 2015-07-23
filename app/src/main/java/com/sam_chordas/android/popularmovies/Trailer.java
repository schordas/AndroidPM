package com.sam_chordas.android.popularmovies;

/**
 * Created by sam_chordas on 7/20/15.
 */
public class Trailer {
    public Trailer(){

    }

    public Trailer(String trailerName, String trailerURL){
        this.trailerName = trailerName;
        this.trailerURL = trailerURL;
    }

    public String trailerName;
    public String trailerURL;
}

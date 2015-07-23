package com.sam_chordas.android.popularmovies;

/**
 * Created by sam_chordas on 7/20/15.
 */
public class Review {
    public Review(){

    }

    public Review(String author, String content){
        this.author = author;
        this.content = content;
    }

    public String author;
    public String content;

}

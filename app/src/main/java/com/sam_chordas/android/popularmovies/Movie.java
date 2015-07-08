package com.sam_chordas.android.popularmovies;

/**
 * Created by sam_chordas on 7/7/15.
 */
public class Movie {

    public int id;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public double popularity;
    public double voteAverage;
    public String posterUrl;

    public Movie(){

    }

    public Movie(int id){
        this.id = id;
    }

    public Movie(String originalTitle){
        this.originalTitle = originalTitle;
    }

    public Movie (int id, String originalTitle, String releaseDate, String overview,
                  double popularity, double voteAverage, String posterUrl){
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.posterUrl = posterUrl;
    }

    public void setAll(int id, String originalTitle, String releaseDate, String overview,
                       double popularity, double voteAverage, String posterUrl){
        this.id = id;
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.posterUrl = posterUrl;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }
}

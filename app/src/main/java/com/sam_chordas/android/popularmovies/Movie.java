package com.sam_chordas.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by sam_chordas on 7/7/15.
 */
public class Movie implements Parcelable{

    public int id;
    public String originalTitle;
    public String overview;
    public String releaseDate;
    public double popularity;
    public double voteAverage;
    public int runtime;
    public String posterUrl;
    public String [] trailerUrlArr;
    public ArrayList<Trailer> trailers = new ArrayList<>();
    public ArrayList<Review> reviews = new ArrayList<>();

    public Movie(){

    }

    public Movie(int id){
        this.id = id;
    }

    public Movie(String originalTitle){
        this.originalTitle = originalTitle;
    }

    private Movie(Parcel in){
        id = in.readInt();
        originalTitle = in.readString();
        releaseDate = in.readString();
        posterUrl = in.readString();
        voteAverage = in.readDouble();
        runtime = in.readInt();
        trailers = in.readArrayList(Trailer.class.getClassLoader());
        reviews = in.readArrayList(Review.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(posterUrl);
        dest.writeInt(id);
        dest.writeInt(runtime);
        dest.writeDouble(voteAverage);
        dest.writeList(trailers);
        dest.writeList(reviews);


    }

    public final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

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

    public void setTrailer(String trailerName, String trailerURL){
        this.trailers.add(new Trailer(trailerName, trailerURL));
    }

    public void setReview(String reviewAuthor, String reviewContent){
        this.reviews.add(new Review(reviewAuthor, reviewContent));
    }

    public void setRunTime(int runtime){this.runtime = runtime;}
}

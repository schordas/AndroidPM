package com.sam_chordas.android.popularmovies;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by sam_chordas on 7/7/15.
 */
public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    public static Movie parseMovieData(JSONObject json){
        Movie movie = new Movie();
        try{
            movie.setId(json.getInt("id"));
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting Id: ", e);
        }
        try{
            movie.setOriginalTitle(json.getString("original_title"));
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting Original Title: ", e);
        }

        try{
            movie.setVoteAverage(json.getDouble("vote_average"));
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting Vote Average: ", e);
        }

        try{
            movie.setOverview(json.getString("overview"));
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting Overview: ", e);
        }

        try{
            movie.setReleaseDate(json.getString("release_date"));
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting Release Date: ", e);
        }

        try{
            movie.setPosterUrl(json.getString("poster_path"));
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting Poster Path: ", e);
        }

        return movie;
    }
}

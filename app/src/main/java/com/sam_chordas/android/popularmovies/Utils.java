package com.sam_chordas.android.popularmovies;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by sam_chordas on 7/7/15.
 */
public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();

    //public static String parseMovieData(JSONObject json, int i)
    public static Movie parseMovieData(JSONObject json, int i, Movie movie){
        if (movie == null){
            movie = new Movie();
        }
        if (i == 1){ //details
            return parseDetails(json, movie);
        }else if (i == 2){
            return parseTrailers(json, movie);
        }else if (i ==3){
            return parseReviewData(json, movie);
        }


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

    public static Movie parseReviewData(JSONObject JSON, Movie movie){
        try{
            movie.setReview(JSON.getString("author"), JSON.getString("content"));
        }catch (Exception e){
            Log.e(LOG_TAG, "Error getting review data: ", e);
        }

        return movie;
    }

    public static Movie parseDetails(JSONObject JSON, Movie movie){
        try{
            movie.setRunTime(JSON.getInt("runtime"));
        }catch(Exception e){
            Log.e(LOG_TAG, "Error getting runtime: ", e);
        }
        return movie;
    }

    public static Movie parseTrailers(JSONObject JSON, Movie movie){
        try{
            movie.setTrailer(JSON.getString("name"), JSON.getString("key"));
        }catch(Exception e){
            Log.e(LOG_TAG, "Error getting trailer data: ", e);
        }
        return movie;
    }
}

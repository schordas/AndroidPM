package com.sam_chordas.android.popularmovies.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.util.Log;

import com.sam_chordas.android.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by sam_chordas on 7/21/15.
 */
public class MovieService extends IntentService{

    private static final String WEB_PAGE_FAILED = "Unable to retrieve web page";

    private static final String LOG_TAG = MovieService.class.getSimpleName();
    public MovieService(){
        super("MovieService");
    }
    public MovieService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {


        URL url = null;
        int urlType = intent.getIntExtra("type", -1);
        if (urlType == -1){
            return;
        }
        int id = intent.getIntExtra("id", -1);
        if (id == 1){
            return;
        }
        try{
            url = new URL(intent.getStringExtra("url"));
        }catch (Exception e){
            Log.e(LOG_TAG, "Error - URL conversion: ", e);
        }
        InputStream inStrm = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String retJSON = null;
        //String [] JSONArr = new String[4];

        // Connecting/parsing sources:
        // http://developer.android.com/training/basics/network-ops/connecting.html
        // https://gist.github.com/udacityandroid/d6a7bb21904046a91695
        // http://www.vogella.com/tutorials/AndroidJSON/article.html
        //for (int i = 0; i < urls.length; i++){
            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inStrm = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inStrm == null){
                    return;
                }
                reader = new BufferedReader(new InputStreamReader(inStrm));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                if (buffer.length() == 0){
                    return;
                }
                retJSON = buffer.toString();
                parseJSON(retJSON, urlType);
            } catch (IOException e){
                Log.e(LOG_TAG, "Error: ", e);

                return;
            } finally{
                if (urlConnection != null){
                    urlConnection.disconnect();
                }

                if (reader != null){
                    try{
                        reader.close();
                    } catch (final IOException e){
                        Log.e(LOG_TAG, "Error closing", e);
                    }
                }
            }
        //}


    }

    public void parseJSON(String JSON, int urlType){

        if (JSON != null && !JSON.equals(WEB_PAGE_FAILED)){
            Log.i(LOG_TAG, "In post execute");
            JSONObject jsonObject = null;

            try{
                jsonObject = new JSONObject(JSON);
            } catch (Exception e){
                Log.e(LOG_TAG, "Error: ", e);
                //e.printStackTrace();
            }
            //JSONObject results = null;
//            JSONArray results = null;
//            try{
//                results = jsonObject.getJSONArray("results");
//            }catch(Exception e){
//                Log.e(LOG_TAG, "Error retrieving resultsArr: ", e);
//            }

            if (urlType == 0){
                getMovieDataFromJson(jsonObject);
            }else if(urlType == 1){
                getDetailDataFromJson(jsonObject);
            }else if(urlType == 2){
                getTrailersDataFromJson(jsonObject);
            } else if(urlType == 3){
                getReviewsDataFromJson(jsonObject);
            } else{
                return;
            }



        }
        else{
            Log.e(LOG_TAG, WEB_PAGE_FAILED);
        }
    }

    public void getMovieDataFromJson(JSONObject jsonObject){
        JSONArray results = null;
        try {
            results = jsonObject.getJSONArray("results");
        } catch (Exception e){
            Log.e(LOG_TAG, "Error retrieving results array: ", e);
        }
        JSONObject JSON;

        ArrayList<ContentValues> values = new ArrayList<>();
        for (int i = 0; i < results.length(); i++){

            JSON = null;
            try {
                JSON = results.getJSONObject(i);
                values.add(new ContentValues());
            }catch(Exception e){
                Log.e(LOG_TAG, "Error Getting Results: ", e);
            }

            try{
                int id = JSON.getInt("id");
                values.get(i).put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
            } catch (Exception e){
                Log.e(LOG_TAG, "Error Getting Id: ", e);
            }
            try{
                values.get(i).put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                        JSON.getString("original_title"));
                values.get(i).put(MovieContract.MovieEntry.COLUMN_DETS_KEY,
                        JSON.getString("original_title"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error Getting Original Title: ", e);
            }

            try{
                values.get(i).put(MovieContract.MovieEntry.COLUMN_VOTE_AVG,
                        JSON.getDouble("vote_average"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error Getting Vote Average: ", e);
            }

            try{
                values.get(i).put(MovieContract.MovieEntry.COLUMN_OVERVIEW, JSON.getString("overview"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error Getting Overview: ", e);
            }

            try{
                values.get(i).put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE,
                        JSON.getString("release_date"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error Getting Release Date: ", e);
            }

            try{
                values.get(i).put(MovieContract.MovieEntry.COLUMN_POSTER_URL,
                        JSON.getString("poster_path"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error Getting Poster Path: ", e);
            }
        }
        ContentValues [] valuesArr = new ContentValues[values.size()];
        valuesArr = values.toArray(valuesArr);

        this.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, valuesArr);
    }

    public void getDetailDataFromJson(JSONObject jsonObject){
        ContentValues value = new ContentValues();
        try{
            String title = jsonObject.getString("original_title");
            value.put(MovieContract.DetailEntry.COLUMN_REV_KEY,
                    title);
            value.put(MovieContract.DetailEntry.COLUMN_TRLR_KEY,
                    title);
        } catch (Exception e){
            Log.e(LOG_TAG, "Error Getting original title: ", e);
        }
        try{
            value.put(MovieContract.DetailEntry.COLUMN_RUNTIME,
                    jsonObject.getInt("runtime"));
        } catch(Exception e){
            Log.e(LOG_TAG, "Error getting runtime: ", e);
        }

        this.getContentResolver().insert(MovieContract.DetailEntry.CONTENT_URI, value);
    }

    public void getTrailersDataFromJson(JSONObject jsonObject){
        JSONArray results = null;
        try {
            results = jsonObject.getJSONArray("results");
        } catch (Exception e){
            Log.e(LOG_TAG, "Error retrieving results array: ", e);
        }
        JSONObject JSON;
        ContentValues [] values = new ContentValues[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSON = null;
            try {
                JSON = results.getJSONObject(i);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error Getting Results: ", e);
            }

            try {
                values[i].put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID,
                        jsonObject.getInt("id"));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error getting ID: ", e);
            }
            try {
                values[i].put(MovieContract.TrailerEntry.COLUMN_URL_KEY,
                        JSON.getString("id"));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error Getting URL: ", e);
            }
            try{
                values[i].put(MovieContract.TrailerEntry.COLUMN_TRAILER_NAME,
                        JSON.getString("name"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error getting trailer name: ", e);
            }
        }

        this.getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI, values);
    }

    public void getReviewsDataFromJson(JSONObject jsonObject){
        JSONArray results = null;
        try {
            results = jsonObject.getJSONArray("results");
        } catch (Exception e){
            Log.e(LOG_TAG, "Error retrieving results array: ", e);
        }
        JSONObject JSON;
        ContentValues [] values = new ContentValues[results.length()];

        for (int i = 0; i < results.length(); i++) {
            JSON = null;
            try {
                JSON = results.getJSONObject(i);
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error Getting Results: ", e);
            }

            try {
                values[i].put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
                        jsonObject.getInt("id"));
            } catch (Exception e) {
                Log.e(LOG_TAG, "Error getting ID: ", e);
            }
            try {
                values[i].put(MovieContract.ReviewEntry.COLUMN_AUTHOR,
                        JSON.getString("author"));
            } catch (Exception e){
                Log.e(LOG_TAG, "Error getting review Author: ", e);
            }
            try{
                values[i].put(MovieContract.ReviewEntry.COLUMN_REVIEW,
                        JSON.getString("content"));
            }catch (Exception e){
                Log.e(LOG_TAG, "Error getting review content: ", e);
            }

            values[i].put(MovieContract.ReviewEntry.COLUMN_REVIEW_NAME,
                    "Review " + i +1);
        }

        this.getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI, values);
    }

}

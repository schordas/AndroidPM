package com.sam_chordas.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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
 * A placeholder fragment containing a simple view.
 */
public class MovieGridActivityFragment extends Fragment {

    private static final String LOG_TAG = MovieGridActivity.class.getSimpleName();

    private static final String WEB_PAGE_FAILED = "Unable to retrieve web page";

    private MovieAdapter mMovieAdapter;



    public MovieGridActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);


        GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                Bundle args = new Bundle();
                args.putInt("id", movie.id);
                args.putString("originalTitle", movie.originalTitle);
                args.putString("overview", movie.overview);
                args.putString("posterUrl", movie.posterUrl);
                args.putString("releaseDate", movie.releaseDate);
                args.putDouble("voteAverage", movie.voteAverage);
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtras(args);
                getActivity().startActivity(intent);
            }
        });

        gridView.setAdapter(mMovieAdapter);

        // Check for network connection
        // Thanks Google!
        // http://developer.android.com/training/basics/network-ops/connecting.html
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        if (networkInfo != null){
            GetMoviesTask task = new GetMoviesTask();
            task.execute("http://api.themoviedb.org/3/discover/" +
                    "movie?sort_by=popularity.desc&api_key=da9e1ef0ef1889f59cc176eeae76099b");
        }
        else{
            // show a dialog
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }


    public class GetMoviesTask extends AsyncTask<String, Void, String>{

        @Override
        public void onPreExecute(){

        }

        @Override
        public String doInBackground(String...url){
            if (url.length == 0 || url[0].length() == 0){
                return null;
            }
            Log.i(LOG_TAG, "In background execute");
            try{
                return downloadUrl(url[0]);
            } catch (IOException e){
                return WEB_PAGE_FAILED;
            }
        }

        @Override
        public void onPostExecute(String JSON){
            super.onPostExecute(JSON);
            mMovieAdapter.clear();
            // Parse and create objects.
            if (JSON != null && !JSON.equals(WEB_PAGE_FAILED)){
                Log.i(LOG_TAG, "In post execute");
                JSONObject jsonObject = null;
                Movie movie = null;

                try{
                    jsonObject = new JSONObject(JSON);
                } catch (Exception e){
                    Log.e(LOG_TAG, "Error: ", e);
                    //e.printStackTrace();
                }
                //JSONObject results = null;
                JSONArray results = null;
                try{
                    results = jsonObject.getJSONArray("results");
                    Log.i(LOG_TAG, results.toString());
                }catch(Exception e){
                    Log.e(LOG_TAG, "Error retrieving resultsArr: ", e);
                }
//                try{
//                    results = jsonObject.getJSONObject("results");
//                } catch (Exception e){
//                    Log.e(LOG_TAG, "Error retrieving results: ", e);
//                }
                //Iterator<String> keys = results.keys();
                for(int i = 0; i < results.length(); i++){
                    //String key = keys.next();
                    try{
                        movie = Utils.parseMovieData(results.getJSONObject(i));

                    }catch(Exception e){
                        Log.e(LOG_TAG, "Error Getting Results: ", e);
                    }
                    if (movie != null){
                        Log.d(LOG_TAG, "parsed movie object just fine");
                        mMovieAdapter.add(movie);

                    }else{
                        Log.d(LOG_TAG, "movie is null");
                    }

                }

//                if (mMovies.isEmpty()){
//                    Toast toast = Toast.makeText(getActivity(), "No Movies", Toast.LENGTH_SHORT);
//                    toast.show();
//                }


            }
            else{
                Log.i(LOG_TAG, "JSON: " + JSON);
            }
        }


        private String downloadUrl(String url) throws IOException{
            InputStream inStrm = null;
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String popMoviesJSONStr = null;
            Log.i(LOG_TAG, "Downloading url");

            // Connecting/parsing sources:
            // http://developer.android.com/training/basics/network-ops/connecting.html
            // https://gist.github.com/udacityandroid/d6a7bb21904046a91695
            // http://www.vogella.com/tutorials/AndroidJSON/article.html

            try{
                URL popMoviesUrl = new URL(url);
                urlConnection = (HttpURLConnection) popMoviesUrl.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                inStrm = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inStrm == null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inStrm));

                String line;
                while((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                if (buffer.length() == 0){
                    return null;
                }
                popMoviesJSONStr = buffer.toString();
            } catch (IOException e){
                Log.e(LOG_TAG, "Error: ", e);

                return null;
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

            return popMoviesJSONStr;
        }
    }

}

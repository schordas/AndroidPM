package com.sam_chordas.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.sam_chordas.android.popularmovies.data.MovieContract;
import com.sam_chordas.android.popularmovies.service.MovieService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;



/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridActivityFragment extends Fragment
implements LoaderManager.LoaderCallbacks<Cursor>{

    @InjectView(R.id.movie_grid)
    GridView mGridView;

//    private ConnectivityManager mConnMgr = (ConnectivityManager)
//            getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//    private Boolean isConnected = mConnMgr.getActiveNetworkInfo() !=null ? true : false;
    private static final int MOVIES = 0;
    private static final int DETAIL = 1;
    private static final int REVIEWS = 2;
    private static final int TRAILERS = 3;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    public static final int MOVIE_LOADER = 0;

    private static final String LOG_TAG = MovieGridActivity.class.getSimpleName();




    private MovieAdapter mMovieAdapter;
    private MovieCursorAdapter mMovieCursorAdapter;
    private Cursor mCursor;
    private int mPostion = GridView.INVALID_POSITION;



    public MovieGridActivityFragment() {
    }

    public interface Callback{
        public void onItemSelected(Uri uri);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final Bundle tempSavedInstanceState = savedInstanceState;

        View rootView = inflater.inflate(R.layout.fragment_movie_grid, container, false);
        getActivity().getContentResolver().delete(
                MovieContract.MovieEntry.CONTENT_URI, null, null);

        ButterKnife.inject(this, rootView);
        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        mMovieCursorAdapter = new MovieCursorAdapter(getActivity(), null, 0);
        final ViewTreeObserver vto = mGridView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout(){
                if(mGridView.getWidth() > 10){
                    if(dbIsEmpty()){
                        String url = getString(R.string.popularity_base_url) + "&" +
                                getString(R.string.movie_api_key);

                        //loadMovies(MOVIES, -1, url);
                    }
                    mGridView.setAdapter(mMovieCursorAdapter);
                    mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                           // Movie movie = mMovieAdapter.getItem(position);
                            Bundle args = new Bundle();

                            Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                            if (cursor != null){
                                ((Callback) getActivity()).onItemSelected(
                                        MovieContract.MovieEntry.buildMovieUri(id));
                            }
                            mPosition = position;
//                            args.putInt("id", movie.id);
//                            args.putString("originalTitle", movie.originalTitle);
//                            args.putString("overview", movie.overview);
//                            args.putString("posterUrl", movie.posterUrl);
//                            args.putString("releaseDate", movie.releaseDate);
//                            args.putDouble("voteAverage", movie.voteAverage);
//                            Intent intent = new Intent(getActivity(), MovieDetail.class);
//                            intent.putExtras(args);
//                            getActivity().startActivity(intent);
                        }
                    });
                    if (tempSavedInstanceState != null && tempSavedInstanceState.containsKey(SELECTED_KEY)) {
                        // The listview probably hasn't even been populated yet.  Actually perform the
                        // swapout in onLoadFinished.
                        mPosition = tempSavedInstanceState.getInt(SELECTED_KEY);
                    }
                }
                ViewTreeObserver obs = mGridView.getViewTreeObserver();
                obs.removeOnGlobalLayoutListener(this);
            }
        });

        //GridView gridView = (GridView) rootView.findViewById(R.id.movie_grid);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Movie movie = mMovieAdapter.getItem(position);
//                Bundle args = new Bundle();
//                args.putInt("id", movie.id);
//                args.putString("originalTitle", movie.originalTitle);
//                args.putString("overview", movie.overview);
//                args.putString("posterUrl", movie.posterUrl);
//                args.putString("releaseDate", movie.releaseDate);
//                args.putDouble("voteAverage", movie.voteAverage);
//                Intent intent = new Intent(getActivity(), MovieDetail.class);
//                intent.putExtras(args);
//                getActivity().startActivity(intent);
//            }
//        });

        //gridView.setAdapter(mMovieAdapter);

        // Check for network connection
        // Thanks Google!
        // http://developer.android.com/training/basics/network-ops/connecting.html
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Boolean isConnected = networkInfo !=null ? true : false;


        if (isConnected && !dbIsUpToDate()){

                String url = getString(R.string.popularity_base_url) + "&" +
                        getString(R.string.movie_api_key);

                loadMovies(MOVIES, -1, url);
            // check and update database if necessary
//            GetMoviesTask task = new GetMoviesTask();
//            task.execute(new String [] {"http://api.themoviedb.org/3/discover/" +
//                    "movie?sort_by=popularity.desc&api_key=da9e1ef0ef1889f59cc176eeae76099b",
//                    "http://api.themoviedb.org/3/movie/", "http://api.themoviedb.org/3",
//            "http://api.themoviedb.org/3"});
        }
        else{
            // TODO:
            // show a dialog
        }

        return rootView;
    }

    public Boolean dbIsEmpty(){
        mCursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_POSTER_URL},
                null,
                null,
                null
//                MovieContract.MovieEntry._ID + " = ?",
//                new String[]{positionStr},
//                null
        );
        if (mCursor== null){
            return true;
        }
//        mCursor.moveToFirst();
        return false;
    }

    public Boolean dbIsUpToDate(){
        Cursor c = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_CREATED_AT}, null, null,null);
        if (c == null){
            return false;
        } else if(c.moveToFirst()) {

            Date currentDate = new Date();

            Timestamp currentTime = new Timestamp(currentDate.getTime());

            Log.d(LOG_TAG, "Cursor to String: " + c.toString());
            if (currentTime.after(Timestamp.valueOf(c.getString(0)))) {
                return true;
            }
        }
        // check if date is correct
        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
        Uri movieUri = MovieContract.MovieEntry.CONTENT_URI;
        return new CursorLoader(getActivity(),
                movieUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        mMovieCursorAdapter.swapCursor(data);

        if (mPosition != ListView.INVALID_POSITION){
            mGridView.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        mMovieCursorAdapter.swapCursor(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    private void loadMovies(int APIRequestType, int id, String url) {
        Intent intent = new Intent(getActivity(), MovieService.class);
        if(id >= 0){
            intent.putExtra("id", id);
        }
        intent.putExtra("type", APIRequestType);
        intent.putExtra("url", url);
//        if (movie == null){
//            movie = new Movie();
//        }
        //intent.putExtra("movie", movie);
        getActivity().startService(intent);
    }
    private void deleteMovies(){
        // TODO:
        // wipe the DB
    }


//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
//        return new CursorLoader(getActivity(),
//                null,
//                null,
//                null,
//                null,
//                null);
//    }


//    public class GetMoviesTask extends AsyncTask<String, Void, String []>{
//
//        @Override
//        public void onPreExecute(){
//
//        }
//
//        @Override
//        public String [] doInBackground(String...url){
//            if (url.length == 0 || url[0].length() == 0){
//                return null;
//            }
//            Log.i(LOG_TAG, "In background execute");
//            try{
//                return downloadUrl(url);
//            } catch (IOException e){
//                return null;
//            }
//        }
//
//        @Override
//        public void onPostExecute(String [] JSONArr){
//            super.onPostExecute(JSONArr);
//            mMovieAdapter.clear();
//            // Parse and create objects.
//            if (JSONArr != null){
//                Log.i(LOG_TAG, "In post execute");
//                JSONObject jsonObject = null;
//                for (int i = 0; i < JSONArr.length; i++){
//                    Movie movie = null;
//                    try{
//                        jsonObject = new JSONObject(JSONArr[i]);
//                    } catch (Exception e){
//                        Log.e(LOG_TAG, "Error: ", e);
//                        //e.printStackTrace();
//                    }
//                    //JSONObject results = null;
//                    JSONArray results = null;
//                    try{
//                        results = jsonObject.getJSONArray("results");
//                        Log.i(LOG_TAG, results.toString());
//                    }catch(Exception e){
//                        Log.e(LOG_TAG, "Error retrieving resultsArr: ", e);
//                    }
//                    for(int k = 0; k < results.length(); k++){
//                        //St    ring key = keys.next();
//                        try{
//                            movie = Utils.parseMovieData(results.getJSONObject(k), i, movie);
//
//                        }catch(Exception e){
//                            Log.e(LOG_TAG, "Error Getting Results: ", e);
//                        }
//                        if (movie != null){
//                            Log.d(LOG_TAG, "parsed movie object just fine");
//                            mMovieAdapter.add(movie);
//
//                        }else{
//                            Log.d(LOG_TAG, "movie is null");
//                        }
//
//                    }
//
//                }

//                try{
//                    results = jsonObject.getJSONObject("results");
//                } catch (Exception e){
//                    Log.e(LOG_TAG, "Error retrieving results: ", e);
//                }
                //Iterator<String> keys = results.keys();


//                if (mMovies.isEmpty()){
//                    Toast toast = Toast.makeText(getActivity(), "No Movies", Toast.LENGTH_SHORT);
//                    toast.show();
//                }
//            }
//            else{
//                Log.i(LOG_TAG, "Error: JSONArr is null");
//            }
//        }


//        private String [] downloadUrl(String [] urls) throws IOException{
//            InputStream inStrm = null;
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            Log.i(LOG_TAG, "Downloading url");
//            String [] JSONArr = new String[4];
//
//            // Connecting/parsing sources:
//            // http://developer.android.com/training/basics/network-ops/connecting.html
//            // https://gist.github.com/udacityandroid/d6a7bb21904046a91695
//            // http://www.vogella.com/tutorials/AndroidJSON/article.html
//            for (int i = 0; i < urls.length; i++){
//                try{
//                    URL popMoviesUrl = new URL(urls[i]);
//                    urlConnection = (HttpURLConnection) popMoviesUrl.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.connect();
//
//                    inStrm = urlConnection.getInputStream();
//                    StringBuffer buffer = new StringBuffer();
//
//                    if (inStrm == null){
//                        return null;
//                    }
//                    reader = new BufferedReader(new InputStreamReader(inStrm));
//
//                    String line;
//                    while((line = reader.readLine()) != null){
//                        buffer.append(line);
//                    }
//
//                    if (buffer.length() == 0){
//                        return null;
//                    }
//                    JSONArr[i] = buffer.toString();
//                } catch (IOException e){
//                    Log.e(LOG_TAG, "Error: ", e);
//
//                    return null;
//                } finally{
//                    if (urlConnection != null){
//                        urlConnection.disconnect();
//                    }
//
//                    if (reader != null){
//                        try{
//                            reader.close();
//                        } catch (final IOException e){
//                            Log.e(LOG_TAG, "Error closing", e);
//                        }
//                    }
//                }
//            }
//
//
//            return JSONArr;
//        }
 //   }

}

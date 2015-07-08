package com.sam_chordas.android.popularmovies;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by sam_chordas on 7/7/15.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private static final String baseUrl = "http://image.tmdb.org/t/p/";
    private static final String w185 = "w185/";
    private static final String w500 = "w500/";

    private List<Movie> mMovies;

    public static class ViewHolder{
        private final ImageView posterImage;

        public ViewHolder(View view){
            posterImage = (ImageView) view.findViewById(R.id.poster_image);
        }
    }

    public MovieAdapter(Activity context, List<Movie> movies){
        super(context, 0, movies);
        mMovies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder viewHolder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.movie_list_item, parent, false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
        }

        Movie movie = getItem(position);
        StringBuilder posterUrl = new StringBuilder();

        // TODO:
        // Complete the query and jason parsing elsewhere
        // Then move on to creating image and setting parameters if necessary

        if (movie.posterUrl != null){
            posterUrl.append(baseUrl);
            posterUrl.append(w500);
            posterUrl.append(movie.posterUrl);


            Picasso.with(getContext()).load(posterUrl.toString()).into(viewHolder.posterImage);
        }

        return row;
    }

}

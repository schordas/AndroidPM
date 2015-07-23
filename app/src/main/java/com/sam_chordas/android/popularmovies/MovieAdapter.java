package com.sam_chordas.android.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
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
    private static final String w342 = "w342/";
    private Context mContext;

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
        mContext = context;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ViewHolder viewHolder = null;
        ImageView imageView;
        if (row == null){
            //LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            //row = inflater.inflate(R.layout.movie_list_item, parent, false);
            //viewHolder = new ViewHolder(row);
            imageView = new ImageView(mContext);
            GridView gridView = (GridView) parent;
            GridView.LayoutParams params = new GridView.LayoutParams(
                    GridView.LayoutParams.WRAP_CONTENT,
                    GridView.LayoutParams.WRAP_CONTENT);
            params.width = (parent.getWidth()/gridView.getNumColumns());
            params.height = (int)Math.round(params.width*1.5);
            imageView.setLayoutParams(params);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(0,0,0,0);
            //row.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) row.getTag();
            imageView = (ImageView) convertView;
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


            Picasso.with(getContext()).load(posterUrl.toString()).into(imageView);
        }

        return imageView;
    }

}

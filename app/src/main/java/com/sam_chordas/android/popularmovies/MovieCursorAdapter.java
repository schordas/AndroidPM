package com.sam_chordas.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by sam_chordas on 7/20/15.
 */
public class MovieCursorAdapter extends CursorAdapter {

    private ArrayList<Movie> mMovies;

    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private static final String baseUrl = "http://image.tmdb.org/t/p/";
    private static final String w185 = "w185/";
    private static final String w500 = "w500/";
    private static final String w342 = "w342/";

    public static class ViewHolder {
        public final ImageView iconView;
        public ViewHolder(View view){
            iconView = (ImageView) view.findViewById(R.id.poster_image);
        }
    }

    public MovieCursorAdapter(Context context, Cursor c, int flags){
        super(context, c, flags);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent){
        int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        layoutId = R.layout.movie_list_item;

        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int position = cursor.getPosition();
        String positionStr = String.valueOf(position);
//        Log.i(LOG_TAG, "Position: " + position);

//        cursor = context.getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
//                new String[]{MovieContract.MovieEntry.COLUMN_POSTER_URL},
//                null,
//                null,
//                null
////                MovieContract.MovieEntry._ID + " = ?",
////                new String[]{positionStr},
////                null
//                );

//        DatabaseUtils.dumpCursor(cursor);

//        cursor = context.getContentResolver().query(Uri.parse("raw_query"), null,
//                null, null, null);


        //Movie movie = mMovies.get(cursor.getPosition());
        StringBuilder posterUrl = new StringBuilder();
        if (cursor == null){
            return;
        }else{
//            Log.i(LOG_TAG, "Cursor column: " + cursor.getColumnIndex("poster_url"));

//            cursor.
            posterUrl.append(baseUrl);
            posterUrl.append(w500);
            Log.i(LOG_TAG, "Image url: " + cursor.getString(6));
            posterUrl.append(cursor.getString(6));

//            cursor.moveToNext();

            Picasso.with(context).load(posterUrl.toString()).into(viewHolder.iconView);
        }
    }
}

package com.example.w0276812.movietrailers;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by w0276812 on 12/7/2015.
 */
public class CustomListAdapter extends ArrayAdapter<Movie> {
    private final Activity context;
    private final Movie[] movies;

    public CustomListAdapter(Activity context, Movie[] mvs) {
        super(context, R.layout.list_item, mvs);
        this.context = context;
        this.movies = mvs;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView title = (TextView) rowView.findViewById(R.id.listItemTitle);
        ImageView thumb = (ImageView) rowView.findViewById(R.id.listItemThumbnail);
        RatingBar rating = (RatingBar) rowView.findViewById(R.id.ratingBar);

        title.setText(movies[position].getTitle());
        int imgId = parent.getResources().getIdentifier(movies[position].getAlias(),"drawable",parent.getContext().getPackageName());
        thumb.setImageResource(imgId);
        rating.setRating(movies[position].getRating() / 2.0f); // div by 2 because model/db stores out of 10, displayed as (ex) 2.5 of 5 stars.
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(255, 255, 50), PorterDuff.Mode.SRC_ATOP); // full stars
        stars.getDrawable(1).setColorFilter(Color.rgb(255, 255, 180), PorterDuff.Mode.SRC_ATOP); // partially full star border
        stars.getDrawable(0).setColorFilter(Color.rgb(255, 255, 180), PorterDuff.Mode.SRC_ATOP); // empty star border

        return rowView;
    }


}

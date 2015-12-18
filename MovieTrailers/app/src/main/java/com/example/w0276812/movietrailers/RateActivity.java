package com.example.w0276812.movietrailers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;


public class RateActivity extends Activity {

    private Movie movie;
    private DBAdapter data;

    // widgets
    private TextView title;
    private ImageView thumbnail;
    private TextView description;
    private RatingBar rating;
    private Button saveButton;
    private Button deleteButton;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        data = new DBAdapter(this);
        fetchMovie();
        connectWidgets();
        connectListeners();

    }

    private void connectListeners() {

        // changing the rating enables save.
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                saveButton.setEnabled(true);
            }
        });

        // save rating to db.  Show a toast.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newRating = (int) (rating.getRating() * 2);
                movie.setRating(newRating);
                data.open();
                data.updateMovie(movie);
                data.close();

                saveButton.setEnabled(false);
                Toast.makeText(getBaseContext(), "Rating updated", Toast.LENGTH_SHORT).show();
            }
        });

        // delete this movie from db.  Return to list view, show a toast.
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete from the database
                data.open();
                data.deleteMovie(movie);
                data.close();
                // close to send back to list activity
                finish();
            }
        });

        // play the trailer.
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // package up some data about the movie.
                Intent watchThis = new Intent(RateActivity.this, WatchActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("id", movie.getId());
                extras.putString("title", movie.getTitle());
                extras.putString("description", movie.getDescription());
                extras.putString("alias", movie.getAlias());
                extras.putString("url", movie.getUrl());
                extras.putInt("rating", movie.getRating());
                watchThis.putExtras(extras);

                // send to the rate activity.
                startActivityForResult(watchThis, 1);

            }
        });
    }

    private void fetchMovie() {
        Bundle info = getIntent().getExtras();
        movie = null;
        if (info != null) {
            movie = new Movie(info.getInt("id"),info.getString("title"), info.getString("description"),
                    info.getString("alias"), info.getString("url"), info.getInt("rating"));
        }
    }

    private void connectWidgets() {
        // connect to xml
        title = (TextView)findViewById(R.id.rateTitle);
        thumbnail = (ImageView)findViewById(R.id.rateThumbnail);
        description = (TextView)findViewById(R.id.rateDesc);
        rating = (RatingBar)findViewById(R.id.activeBar);
        saveButton = (Button)findViewById(R.id.saveRating);
        deleteButton = (Button)findViewById(R.id.removeItem);
        playButton = (Button)findViewById(R.id.playButton);

        // fill with movie attributes
        title.setText(movie.getTitle());
        description.setText(movie.getDescription());
        rating.setRating(movie.getRating() / 2.0f);
        int imgId = getResources().getIdentifier(movie.getAlias(),"drawable",getPackageName());
        thumbnail.setImageResource(imgId);

        // change star color
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(255, 255, 50), PorterDuff.Mode.SRC_ATOP); // full stars
        stars.getDrawable(1).setColorFilter(Color.rgb(255, 255, 180), PorterDuff.Mode.SRC_ATOP); // partially full star border
        stars.getDrawable(0).setColorFilter(Color.rgb(255, 255, 180), PorterDuff.Mode.SRC_ATOP); // empty star border

        // until rating gets changed
        saveButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.example.w0276812.movietrailers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;


public class ListActivity extends Activity {

    private DBAdapter data;
    private Movie[] movies;
    int currentId;

    private Button addButton;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        data = new DBAdapter(this);
        currentId = -1;

        // check if the database is empty
        data.open();
        if (data.getAllMovies().getCount() < 1) {
            fillDatabase();
        }
        data.close();

        movies = fetchMovies(); // get from the database
        connectWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // check for updated ratings.  Note any deletions.
        if (currentId > -1) {
            String currentTitle = "";
            if (currentId < movies.length) {
                currentTitle = movies[currentId].getTitle();
            }
            int expectedCount = movies.length;
            movies = fetchMovies();
            CustomListAdapter adapter = new CustomListAdapter(this, movies);
            list.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (expectedCount > movies.length) {
                // a movie was removed
                Toast.makeText(getBaseContext(), currentTitle + " trailer removed", Toast.LENGTH_SHORT).show();
            }
            currentId = -1;
        }

    }

    private Movie[] fetchMovies() {
        data.open();
        Cursor cur = data.getAllMovies();
        Movie[] movies = new Movie[0]; // in case of db trouble
        if (cur.moveToFirst()) {
            movies = new Movie[cur.getCount()];
            int idx = 0;
            while (!cur.isAfterLast()) {
                // do your stuff
                int id = cur.getInt(cur.getColumnIndex(DBAdapter.KEY_ROWID));
                String title = cur.getString(cur.getColumnIndex(DBAdapter.KEY_TITLE));
                String description = cur.getString(cur.getColumnIndex(DBAdapter.KEY_DESCRIPTION));
                String alias = cur.getString(cur.getColumnIndex(DBAdapter.KEY_ALIAS));
                String url = cur.getString(cur.getColumnIndex(DBAdapter.KEY_URL));
                int rating = cur.getInt(cur.getColumnIndex(DBAdapter.KEY_RATING));

                movies[idx] = new Movie(id, title, description, alias, url, rating);
                cur.moveToNext();
                idx++;
            }
        }
        data.close();
        return movies;
    }

    private void connectWidgets() {
        addButton = (Button)findViewById(R.id.addTrailer);
        list = (ListView)findViewById(R.id.trailerList);

        CustomListAdapter adapter = new CustomListAdapter(this, movies);
        list.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addOne = new Intent(ListActivity.this, AddActivity.class);
                startActivityForResult(addOne, 1);
                currentId = 1000;
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                // package up some data about the movie.
                Intent rateThis = new Intent(ListActivity.this, RateActivity.class);
                Bundle extras = new Bundle();
                extras.putInt("id", movies[idx].getId());
                extras.putString("title", movies[idx].getTitle());
                extras.putString("description", movies[idx].getDescription());
                extras.putString("alias", movies[idx].getAlias());
                extras.putString("url", movies[idx].getUrl());
                extras.putInt("rating", movies[idx].getRating());
                rateThis.putExtras(extras);

                // send to the rate activity.
                currentId = idx;
                startActivityForResult(rateThis, 1);
            }
        });
    }

    public static Movie[] getMoviesFromResource(Context context, int resourceId) {
        ArrayList<Movie> resMovies = new ArrayList<Movie>();
        try {
            InputStream inStream = context.getResources().openRawResource(resourceId);
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            String str = null;
            int idx = 0;
            Movie mv;
            while ((str = br.readLine()) != null) {
                String item[] = str.split("\\s*::\\s*");
                String urlBase = "https://www.youtube.com/watch?v=";
                mv = new Movie(0, item[0], item[3], item[1], urlBase + item[2], 5);
                resMovies.add(mv);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.w("FILEIO", e.getMessage());
        }
        return resMovies.toArray(new Movie[0]);
    }

    // presumes that "data" is already open.  Will be closed after.
    private void fillDatabase() {
        Movie mvs[] = getMoviesFromResource(this, R.raw.default_movies);
        //for (int idx = 0; idx < mvs.length; idx++) {
        for (Movie mv : mvs) {
            data.insertMovie(mv);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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

//FOR REFERENCE!!
////add a contact- CREATE
//        db.open();
//        long id = db.insertContact("Bob Loblaw","Bob@Loblaws.ca");
//        id = db.insertContact("Sue Me","Sue@Me.ca");
//        db.close();
//
// //get all contacts - READ
//        db.open();
//        Cursor c = db.getAllContacts();
//        if(c.moveToFirst())
//        {
//        do{
//        DisplayContact(c);
//        }while(c.moveToNext());
//        }
//        db.close();
//
////get a single contact - READ
//        db.open();
//        c = db.getContact(2);
//        if(c.moveToFirst())
//        DisplayContact(c);
//        else
//        Toast.makeText(this,"No contact found",Toast.LENGTH_LONG).show();
//
//        db.close();
//
////update a contact - UPDATE
//        db.open();
//        if(db.updateContact(1,"Bob Loblaw2","Bob@Loblaws2.ca"))
//        Toast.makeText(this,"Update successful",Toast.LENGTH_LONG).show();
//        else
//        Toast.makeText(this,"Update failed",Toast.LENGTH_LONG).show();
//        db.close();
//
////delete a contact - DELETE
//        db.open();
//        if(db.deleteContact(1))
//        Toast.makeText(this,"Delete successful",Toast.LENGTH_LONG).show();
//        else
//        Toast.makeText(this,"Delete failed",Toast.LENGTH_LONG).show();
//
//        db.close();

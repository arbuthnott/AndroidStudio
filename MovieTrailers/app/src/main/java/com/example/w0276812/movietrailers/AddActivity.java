package com.example.w0276812.movietrailers;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class AddActivity extends Activity {

    private Movie allMovies[];
    private ListView addList;
    private final DBAdapter data = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        allMovies = ListActivity.getMoviesFromResource(this, R.raw.movies);
        // extract the titles
        String titles[] = new String[allMovies.length];
        for (int idx = 0; idx < allMovies.length; idx++) {
            titles[idx] = allMovies[idx].getTitle();
        }

        addList = (ListView)findViewById(R.id.listToAdd);
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titles);
        addList.setAdapter(adapter);

        addList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int idx, long l) {
                Movie pickedMovie = allMovies[idx];
                data.open();
                if (data.inDataBase(pickedMovie)) {
                    Toast.makeText(getBaseContext(), "This Trailer is already in your list", Toast.LENGTH_SHORT).show();
                } else {
                    data.insertMovie(pickedMovie);
                    Toast.makeText(getBaseContext(), pickedMovie.getTitle() + " added to list", Toast.LENGTH_SHORT).show();
                }
                data.close();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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

package com.example.chris.imageanimator;

import android.app.*;
import android.os.*;
import android.support.v4.app.FragmentActivity;
import android.view.*;
import android.widget.*;

public class MainActivity extends Activity {

    private ListFragment list_frag;
    private ImageFragment image_frag;
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeFrags();
        resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_frag.resetList();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //list_frag.loadPrefs();
        // This method works on button clicks.  At the start, can't seem to use it late enough
        // for the list view to already exist!
    }

    private void initializeFrags() {
        // connect the fragments
        list_frag = new ListFragment();
        image_frag = new ImageFragment();

        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.list_placeholder, list_frag);
        transaction.commit();

        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.replace(R.id.image_placeholder, image_frag);
        transaction2.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

package com.example.chris.imageanimator;

/**
 * Created by Chris on 2015-11-15.
 */

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] titles;
    private final int[] imageIds;
    private boolean[] activeItems = {true, true, true, true};

    public CustomListAdapter(Activity ctxt, String[] ttls, int[] imgIds) {
        super(ctxt, R.layout.list_item, ttls);
        context = ctxt;
        titles = ttls;
        imageIds = imgIds;
        loadPrefs();
    }

    public void loadPrefs() {
        // synch with shared prefs.
        for (int idx = 0; idx < getCount(); idx++) {
            boolean enabled = context.getPreferences(Context.MODE_PRIVATE).getBoolean("Item " + idx, true);
            Log.d("PREFS", "Loaded preference Item " + idx + ": " + enabled);
            setEnabled(idx, enabled);
        }
    }

    public void setEnabled(int pos, boolean enabled) {
        activeItems[pos] = enabled;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.textView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        txtTitle.setText(titles[position]);
        imageView.setImageResource(imageIds[position]);

        // grey out item if inactive.
        if (!activeItems[position]) {
            rowView.setAlpha(0.3f);
        }
        return rowView;
    }

    @Override
    public boolean isEnabled(int position) {
        return activeItems[position];
    }
}

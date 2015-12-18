package com.example.chris.imageanimator;

/**
 * Created by Chris on 2015-11-14.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.*;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class ListFragment extends Fragment {

    private CustomListAdapter adapter;
    private String[] listTitles;
    private int[] imageIds = {
            R.mipmap.space_lobster,
            R.mipmap.space_octo,
            R.mipmap.space_skull,
            R.mipmap.space_ufo
    };

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle saveInstanceState) {
        // inflate layout for this fragment
        View mainView = inflater.inflate(R.layout.list_fragment, container, false);
        // populate the contained list
        listTitles = getResources().getStringArray(R.array.image_names);
        final ListView list = (ListView)mainView.findViewById(R.id.listView);
        adapter = new CustomListAdapter(getActivity() , listTitles, imageIds);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // disable this list item
                setEnabled(position, false);
                // show a toast
                Toast.makeText(parent.getContext(), "You clicked " + listTitles[position], Toast.LENGTH_SHORT).show();
                // change the image
                ImageFragment imgFrag = (ImageFragment) getFragmentManager().findFragmentById(R.id.image_placeholder);
                imgFrag.setImage(imageIds[position]);
            }
        });
        return mainView;
    }

    public void setEnabled(int pos, boolean enabled) {
        // pass info to the adapter
        adapter.setEnabled(pos, enabled);
        // ensure view is greyed out if appropriate.
        ListView list = (ListView)getView().findViewById(R.id.listView);
        if (enabled) {
            list.getChildAt(pos).setAlpha(1f);
        } else {
            list.getChildAt(pos).setAlpha(0.3f);
        }
        // save to preferences.
        getActivity().getPreferences(Context.MODE_PRIVATE).edit().putBoolean("Item " + pos, enabled);
        Log.d("PREFS","Saving preference Item " + pos + ": " + enabled);
    }

    public void resetList() {
        for (int idx=0; idx < adapter.getCount(); idx++) {
            setEnabled(idx, true);
        }
    }
}

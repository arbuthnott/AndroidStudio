package com.example.chris.imageanimator;

/**
 * Created by Chris on 2015-11-14.
 */

import android.app.Fragment;
import android.os.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle saveInstanceState) {
        // inflate layout for this fragment
        return inflater.inflate(R.layout.image_fragment, container, false);
    }

    public void setImage(int imgId) {
        ImageView imgView = (ImageView)getView().findViewById(R.id.mainImage);
        imgView.setImageResource(imgId);
        imgView.setVisibility(View.VISIBLE);
        // start the animation
        Animation growAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.spin_in);
        imgView.startAnimation(growAnim);
        imgView.setVisibility(View.INVISIBLE);
    }
}

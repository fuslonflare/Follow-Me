package com.example.phuwarin.followme.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.MapsActivity;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class PickRouteFragment extends Fragment {

    /*** Listener Zone ***/
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            int x = group.indexOfChild(group.findViewById(checkedId));
            MapsActivity.showRoute(x);
        }
    };
    private RadioGroup radioGroupRoute;

    public PickRouteFragment() {
        super();
    }

    public static PickRouteFragment newInstance() {
        PickRouteFragment fragment = new PickRouteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_route, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        radioGroupRoute = (RadioGroup) rootView.findViewById(R.id.rg_route);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // Save Instance State Here
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    // Restore Instance State Here
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
        int size = MapsActivity.getSizeOfRoute();
        for (int i = 2; i >= size; i--) {
            radioGroupRoute.getChildAt(i).setVisibility(View.GONE);
        }
        radioGroupRoute.check(radioGroupRoute.getChildAt(0).getId());
        radioGroupRoute.setOnCheckedChangeListener(onCheckedChangeListener);
    }
}

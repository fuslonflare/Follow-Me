package com.example.phuwarin.followme.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.PickDestinationActivity;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class PickDestinationFragment extends Fragment {


    /***
     *** Listener Zone ******/
    PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            showToast(place.getName().toString());
            PickDestinationActivity.setMarker(place);
        }

        @Override
        public void onError(Status status) {
            showToast("Error: " + status.getStatusMessage());
        }
    };

    public PickDestinationFragment() {
        super();
    }

    public static PickDestinationFragment newInstance() {
        PickDestinationFragment fragment = new PickDestinationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SupportPlaceAutocompleteFragment placeAutoComplete = new SupportPlaceAutocompleteFragment();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.place_autocomplete_fragment, placeAutoComplete).commit();

        placeAutoComplete.setOnPlaceSelectedListener(placeSelectionListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_destination, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

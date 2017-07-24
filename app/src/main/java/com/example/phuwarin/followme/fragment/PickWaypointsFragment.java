package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.AddMemberActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Colour;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.BicycleRoute;
import com.example.phuwarin.followme.util.detail.Destination;
import com.example.phuwarin.followme.util.detail.Origin;
import com.example.phuwarin.followme.util.detail.TripDetail;
import com.example.phuwarin.followme.util.detail.User;
import com.example.phuwarin.followme.util.detail.Waypoint;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class PickWaypointsFragment extends Fragment
        implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private AppCompatButton buttonNext;

    private List<Marker> waypoints;
    private List<Waypoint> listWaypoints;

    /**
     * Listener
     */
    private PlaceSelectionListener placeSelectionListener = new PlaceSelectionListener() {
        @Override
        public void onPlaceSelected(Place place) {
            if (waypoints == null) {
                waypoints = new ArrayList<>();
            }

            final Marker waypoint = mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng())
                    .title(place.getName().toString())
                    .snippet(place.getId())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_waypoint)));
            waypoints.add(waypoint);

            Snackbar.make(buttonNext, getString(R.string.waypoint_success), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.text_undo), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int lastIndex = waypoints.size() - 1;
                            waypoints.get(lastIndex).remove();
                            waypoints.remove(lastIndex);
                        }
                    })
                    .setActionTextColor(Color.parseColor(Colour.BLUE.getCode()))
                    .show();
        }

        @Override
        public void onError(Status status) {
            showSnackbar(status.getStatusMessage());
        }
    };
    private Callback<NormalDao> createTripCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(Call<NormalDao> call,
                               Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    showSnackbar("Trip Created, please check database.");
                    Intent intent = new Intent(getActivity(), AddMemberActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                } else {
                    int errorCode = response.body().getErrorCode();
                    showSnackbar(Constant.getInstance().getMessage(errorCode));
                }
            } else {
                try {
                    showSnackbar(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<NormalDao> call,
                              Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };
    /**
     * Callback
     */
    private Callback<GenerateDao> callbackGenerateTripId = new Callback<GenerateDao>() {
        @Override
        public void onResponse(Call<GenerateDao> call,
                               Response<GenerateDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    String id = response.body().getData();
                    TripDetail.getInstance().setTripId(id);

                    createTrip();
                } else {
                    int errorCode = response.body().getErrorCode();
                    showSnackbar(Constant.getInstance().getMessage(errorCode));
                }
            } else {
                try {
                    showSnackbar(response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<GenerateDao> call,
                              Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };


    /**
     * Default Constructor
     */
    public PickWaypointsFragment() {
        super();
    }

    public static PickWaypointsFragment newInstance() {
        PickWaypointsFragment fragment = new PickWaypointsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Overridden Method
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pick_waypoints, container, false);
        initInstances(rootView);
        return rootView;
    }

    /**
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng origin = Origin.getInstance().getOriginLocation();
        LatLng destination = Destination.getInstance().getDestinationLocation();
        String path = BicycleRoute.getInstance().getRoutePath();

        mMap.addMarker(new MarkerOptions().position(origin));
        mMap.addMarker(new MarkerOptions().position(destination));

        drawRoutePath(path);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);

        LatLngBounds bounds = builder.build();
        final CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                bounds, 70);

        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(cameraUpdate);
            }
        });
    }

    /**
     * Method
     */

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        buttonNext = rootView.findViewById(R.id.button_next);
        buttonNext.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_waypoints);
        mapFragment.getMapAsync(this);

        SupportPlaceAutocompleteFragment autocomplete = new SupportPlaceAutocompleteFragment();
        getChildFragmentManager().beginTransaction()
                .add(R.id.place_autocomplete_fragment, autocomplete)
                .commit();
        autocomplete.setOnPlaceSelectedListener(placeSelectionListener);
    }

    private void showSnackbar(CharSequence message) {
        Snackbar snackbar = Snackbar.make(buttonNext, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(getResources().getColor(R.color.white));

        snackbar.show();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonNext) {
            if (waypoints == null || waypoints.size() == 0) {
                Snackbar.make(buttonNext, getString(R.string.waypoint_zero), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.text_continue), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                keepWaypointsToLocal();
                                generateTripId();
                            }
                        })
                        .setActionTextColor(Color.parseColor(Colour.BLUE.getCode()))
                        .show();
            } else {
                keepWaypointsToLocal();
                generateTripId();
            }
        }
    }

    private void drawRoutePath(String path) {
        List<LatLng> location = PolyUtil.decode(path);
        if (mMap != null) {
            mMap.addPolyline(DirectionConverter.createPolyline(
                    getContext(), (ArrayList<LatLng>) location,
                    3, Color.parseColor(Colour.RED.getCode())));
        }
    }

    private void generateTripId() {
        Call<GenerateDao> callGenerateTripId = HttpManager.getInstance().getService()
                .generateTripId();
        callGenerateTripId.enqueue(callbackGenerateTripId);
    }

    private void createTrip() {
        try {
            JSONObject main = new JSONObject();
            JSONObject user = new JSONObject();
            JSONObject origin = new JSONObject();
            JSONObject destination = new JSONObject();
            JSONObject route = new JSONObject();

            user.put("id", User.getInstance().getId());
            user.put("name", User.getInstance().getName());
            user.put("position", User.getInstance().getPosition());

            origin.put("id", Origin.getInstance().getOriginId());
            origin.put("name_en", Origin.getInstance().getOriginNameEn());
            origin.put("name_th", Origin.getInstance().getOriginNameTh());
            origin.put("lat", Origin.getInstance().getOriginLocation().latitude);
            origin.put("lng", Origin.getInstance().getOriginLocation().longitude);

            destination.put("id", Destination.getInstance().getDestinationId());
            destination.put("name_en", Destination.getInstance().getDestinationNameEn());
            destination.put("name_th", Destination.getInstance().getDestinationNameTh());
            destination.put("lat", Destination.getInstance().getDestinationLocation().latitude);
            destination.put("lng", Destination.getInstance().getDestinationLocation().longitude);

            route.put("id", BicycleRoute.getInstance().getRouteId());
            route.put("path", BicycleRoute.getInstance().getRoutePath());
            route.put("origin", BicycleRoute.getInstance().getRouteOrigin().getOriginId());
            route.put("destination", BicycleRoute.getInstance().getRouteDestination().getDestinationId());

            JSONArray list = new JSONArray();
            if (waypoints != null && waypoints.size() != 0) {
                for (Marker aMarker : waypoints) {
                    JSONObject object = new JSONObject();
                    object.put("name_en", aMarker.getTitle());
                    object.put("name_th", aMarker.getTitle());
                    object.put("lat", aMarker.getPosition().latitude);
                    object.put("lng", aMarker.getPosition().longitude);
                    list.put(object);
                }
            }

            main.put("id", TripDetail.getInstance().getTripId());
            main.put("created_by", user);
            main.put("origin", origin);
            main.put("destination", destination);
            main.put("route", route);
            main.put("waypoints", list);

            Log.i("XTAG", main.toString());

            Call<NormalDao> call = HttpManager.getInstance().getService()
                    .createTrip(main);
            call.enqueue(createTripCallback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void keepWaypointsToLocal() {
        int i = 1;
        listWaypoints = new ArrayList<>();
        if (waypoints != null && waypoints.size() != 0) {
            for (Marker marker : waypoints) {
                listWaypoints.add(new Waypoint(marker.getTitle(), marker.getTitle(),
                        marker.getPosition().latitude, marker.getPosition().longitude, i++));
            }
            BicycleRoute.getInstance().setWaypoints(listWaypoints);
        }
    }


}

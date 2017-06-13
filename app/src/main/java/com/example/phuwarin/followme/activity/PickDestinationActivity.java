package com.example.phuwarin.followme.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.MemberAreaFragment;
import com.example.phuwarin.followme.fragment.PickDestinationFragment;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickDestinationActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener {

    private static GoogleMap mMap;
    private static Marker aMarker;
    private static LatLng currentDestination;

    private static AppCompatButton buttonNext;
    /***
     * Listener Zone
     * ****/
    GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if (aMarker != null) {
                aMarker.remove();
            }

            aMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            currentDestination = latLng;
            buttonNext.setVisibility(View.VISIBLE);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    };

    public static void setMarker(Place place) {
        if (aMarker != null) {
            aMarker.remove();
        }

        LatLng destination = place.getLatLng();
        String desName = place.getName().toString();
        MarkerOptions marker = new MarkerOptions().position(destination)
                .title(desName);

        aMarker = mMap.addMarker(marker);
        currentDestination = destination;
        buttonNext.setVisibility(View.VISIBLE);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 16));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_destination);

        initUi();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.member_area,
                            MemberAreaFragment.newInstance(),
                            "MemberAreaFragment")
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pick_destination_area,
                            PickDestinationFragment.newInstance(),
                            "PickDestinationFragment")
                    .commit();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
    }

    private void initUi() {
        buttonNext = (AppCompatButton) findViewById(R.id.button_next);
        buttonNext.setOnClickListener(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        showToast("Map Ready");

        mMap = googleMap;
        mMap.setOnMapClickListener(onMapClickListener);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonNext) {
            Intent intent = new Intent(PickDestinationActivity.this, MapsActivity.class);
            if (currentDestination != null) {
                double lat = currentDestination.latitude;
                double lng = currentDestination.longitude;
                intent.putExtra("des_lat", lat);
                intent.putExtra("des_lng", lng);
                showToast(lat + ", " + lng);
                startActivity(intent);
            } else {
                showToast("Don't forget to choose destination");
            }
        }
    }
}

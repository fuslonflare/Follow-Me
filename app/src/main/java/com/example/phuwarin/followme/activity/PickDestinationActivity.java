package com.example.phuwarin.followme.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
import com.example.phuwarin.followme.fragment.PickDestinationFragment;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.Destination;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickDestinationActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener {

    private static GoogleMap mMap;

    private static Marker destinationMarker;
    private static LatLng destinationLocation;
    private static String destinationName;
    private static Place destinationPlace;

    //private List<Address> addresses;

    private static AppCompatButton buttonNext;
    /***
     * Listener Zone
     * ****/
    GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            String TAG = "GeocoderTAG";
            List<Address> addresses = new ArrayList<>();
            Geocoder geocoder;
            geocoder = new Geocoder(PickDestinationActivity.this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (addresses != null && addresses.size() != 0) {
                int trim = 0;
                Address address = addresses.get(0);
                String addressLine;
                do {
                    addressLine = "";
                    if (address.getMaxAddressLineIndex() > 0) {
                        for (int i = 0; i < address.getMaxAddressLineIndex() - trim; i++) {
                            addressLine += address.getAddressLine(i) + " ";
                        }
                        trim++;
                    } else {
                        showSnackbar("This location no address lines");
                    }
                } while (addressLine.length() > 100);

                if (addressLine.length() > 0) {
                    destinationName = addressLine;
                }
            }

            if (destinationMarker != null) {
                destinationMarker.remove();
            }

            destinationMarker = mMap.addMarker(new MarkerOptions().position(latLng));
            destinationLocation = latLng;
            buttonNext.setVisibility(View.VISIBLE);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
        }
    };
    Callback<NormalDao> addDestinationCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    Intent intent = new Intent(PickDestinationActivity.this, PickRouteActivity.class);
                    double lat = destinationLocation.latitude;
                    double lng = destinationLocation.longitude;
                    intent.putExtra("des_lat", lat);
                    intent.putExtra("des_lng", lng);
                    showToast(lat + ", " + lng);
                    startActivity(intent);
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
        public void onFailure(@NonNull Call<NormalDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };
    /**
     * Callback Zone
     **/
    Callback<GenerateDao> generateDestinationCallback = new Callback<GenerateDao>() {
        @Override
        public void onResponse(@NonNull Call<GenerateDao> call,
                               @NonNull Response<GenerateDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    String id = response.body().getData();
                    String nameEn = destinationName, nameTh = destinationName;
                    LatLng location = destinationLocation;

                    if (nameEn != null && nameTh != null && location != null &&
                            nameEn.length() > 0 && nameTh.length() > 0) {
                        saveDestinationLocal(id, nameEn, nameTh, location);
                    } else {
                        showSnackbar("Destination name = null or length = 0");
                    }

                    HttpManager.getInstance().getService()
                            .addDestination(
                                    Destination.getInstance().getDestinationId(),
                                    Destination.getInstance().getDestinationNameEn(),
                                    Destination.getInstance().getDestinationNameTh(),
                                    Destination.getInstance().getDestinationLocation().latitude,
                                    Destination.getInstance().getDestinationLocation().longitude
                            ).enqueue(addDestinationCallback);
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
        public void onFailure(@NonNull Call<GenerateDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };

    public static void setMarker(Place place) {
        destinationPlace = place;

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        destinationLocation = destinationPlace.getLatLng();
        destinationName = destinationPlace.getName().toString();
        MarkerOptions marker = new MarkerOptions().position(destinationLocation)
                .title(destinationName);

        destinationMarker = mMap.addMarker(marker);
        buttonNext.setVisibility(View.VISIBLE);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destinationLocation, 16));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_destination);

        initUi();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pick_destination_area,
                            PickDestinationFragment.newInstance(),
                            "PickDestinationFragment")
                    .commit();
        }

        //addresses = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_destination);
        mapFragment.getMapAsync(this);
    }

    private void initUi() {
        buttonNext = findViewById(R.id.button_next);
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

    private void showToast(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(buttonNext, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonNext) {
            if (destinationLocation != null) {
                HttpManager.getInstance().getService()
                        .generateDestinationId()
                        .enqueue(generateDestinationCallback);
            } else {
                showToast("Don't forget to choose destination");
            }
        }
    }

    private void saveDestinationLocal(String id,
                                      String nameEn,
                                      String nameTh,
                                      LatLng location) {
        Destination.getInstance().setDestinationId(id);
        Destination.getInstance().setDestinationNameEn(nameEn);
        Destination.getInstance().setDestinationNameTh(nameTh);
        Destination.getInstance().setDestinationLocation(location);
    }
}

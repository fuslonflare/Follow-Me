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
import android.widget.TextView;
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

    private static GoogleMap sMap;

    private static Marker sMarker;
    private static LatLng sLocation;
    private static String sName;
    private static Place sPlace;

    private static AppCompatButton sButtonNext;

    /**
     * Listener
     */
    private GoogleMap.OnMapClickListener onMapClickListener = new GoogleMap.OnMapClickListener() {
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
                    sName = addressLine;
                }
            }

            if (sMarker != null) {
                sMarker.remove();
            }

            sMarker = sMap.addMarker(new MarkerOptions().position(latLng));
            sLocation = latLng;
            sButtonNext.setVisibility(View.VISIBLE);
            sMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
        }
    };

    /**
     * Callback
     */
    private Callback<GenerateDao> generateDestinationCallback = new Callback<GenerateDao>() {
        @Override
        public void onResponse(@NonNull Call<GenerateDao> call,
                               @NonNull Response<GenerateDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    String id = response.body().getData();
                    String nameEn = sName, nameTh = sName;
                    LatLng location = sLocation;

                    if (nameEn != null && nameTh != null && location != null &&
                            nameEn.length() > 0 && nameTh.length() > 0) {
                        saveDestinationLocal(id, nameEn, nameTh, location);

                        Intent intent = new Intent(PickDestinationActivity.this, PickRouteActivity.class);
                        startActivity(intent);
                    } else {
                        showSnackbar("Destination name = null or length = 0");
                    }

                    /*HttpManager.getInstance().getService()
                            .addDestination(
                                    Destination.getInstance().getDestinationId(),
                                    Destination.getInstance().getDestinationNameEn(),
                                    Destination.getInstance().getDestinationNameTh(),
                                    Destination.getInstance().getDestinationLocation().latitude,
                                    Destination.getInstance().getDestinationLocation().longitude
                            ).enqueue(addDestinationCallback);*/
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

    private Callback<NormalDao> addDestinationCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    Intent intent = new Intent(PickDestinationActivity.this, PickRouteActivity.class);
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

    public static void setMarker(Place place) {
        sPlace = place;

        if (sMarker != null) {
            sMarker.remove();
        }

        sLocation = sPlace.getLatLng();
        sName = sPlace.getName().toString();
        MarkerOptions marker = new MarkerOptions().position(sLocation)
                .title(sName);

        sMarker = sMap.addMarker(marker);
        sButtonNext.setVisibility(View.VISIBLE);
        sMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sLocation, 20));
    }

    /**
     * Overridden method */
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        showToast("Map Ready");

        sMap = googleMap;
        sMap.setOnMapClickListener(onMapClickListener);
    }

    @Override
    public void onClick(View view) {
        if (view == sButtonNext) {
            if (sLocation != null) {
                HttpManager.getInstance().getService()
                        .generateDestinationId()
                        .enqueue(generateDestinationCallback);
            } else {
                showToast("Don't forget to choose destination");
            }
        }
    }

    /**
     * Method
     */
    private void initUi() {
        sButtonNext = findViewById(R.id.button_next);
        sButtonNext.setOnClickListener(this);
    }

    private void showToast(CharSequence message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(CharSequence message) {
        Snackbar snackbar = Snackbar.make(sButtonNext, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(getResources().getColor(R.color.white));

        snackbar.show();
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

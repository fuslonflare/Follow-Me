package com.example.phuwarin.followme.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView mTextLatitude;
    private TextView mTextLongitude;

    private GoogleApiAvailability availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mTextLatitude = (TextView) findViewById(R.id.tv_latitude);
        mTextLongitude = (TextView) findViewById(R.id.tv_longitude);

        availability = GoogleApiAvailability.getInstance();
        buildGoogleApiClient();
    }

    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();

        availability.makeGooglePlayServicesAvailable(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected() && mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        int hasLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    346);
            return;
        }
        LocationAvailability availability = LocationServices.FusedLocationApi.
                getLocationAvailability(mGoogleApiClient);
        if (availability.isLocationAvailable()) {
            showToast("Location available");

            LocationRequest request = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(3000)
                    .setFastestInterval(1000);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient,
                    request,
                    this);
        } else {
            showToast("Location Provider turn off");
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        showToast("Connection suspended: cause = " + cause);
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showToast("Connection Failed: errorMessage = " + connectionResult.getErrorMessage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 346:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showToast("Grant OK");
                } else {
                    showToast("Grant Fail");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);
        }

    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        showToast("onLocationChanged");

        String latitudeLabel = "Latitude";
        String longitudeLabel = "Longitude";

        mTextLatitude.setText(String.format(
                Locale.US, "%s: %f", latitudeLabel, location.getLatitude()));
        mTextLongitude.setText(String.format(
                Locale.US, "%s: %f", longitudeLabel, location.getLongitude()));
    }
}

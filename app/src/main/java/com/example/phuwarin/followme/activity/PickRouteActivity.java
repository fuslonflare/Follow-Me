package com.example.phuwarin.followme.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
import com.example.phuwarin.followme.fragment.PickRouteFragment;
import com.example.phuwarin.followme.manager.ContextBuilder;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Colour;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.BicycleRoute;
import com.example.phuwarin.followme.util.detail.Destination;
import com.example.phuwarin.followme.util.detail.Origin;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PickRouteActivity extends FragmentActivity
        implements OnMapReadyCallback, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, DirectionCallback {

    private static final String TAG = "DirectionTAG";
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 346;
    //private static final double DISTANCE_THRESHOLD = 50.0;

    private static GoogleMap mMap;
    private static List<Route> route;
    private static Polyline polyline;
    private static int sizeOfRoute;
    private static AppCompatTextView textDistance;
    private String API_KEY;
    private GoogleApiClient mGoogleApiClient;
    private Marker mOriginMarker;
    private AppCompatButton buttonRequestDirection;
    private LatLng origin;
    private LatLng destination;
    /**
     * Callback Zone
     **/
    private Callback<GenerateDao> generateOriginCallback = new Callback<GenerateDao>() {
        @Override
        public void onResponse(@NonNull Call<GenerateDao> call,
                               @NonNull Response<GenerateDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    String id = response.body().getData();
                    Origin.getInstance().setOriginId(id);

                    /*HttpManager.getInstance().getService()
                            .addOrigin(
                                    Origin.getInstance().getOriginId(),
                                    Origin.getInstance().getOriginNameEn(),
                                    Origin.getInstance().getOriginNameTh(),
                                    Origin.getInstance().getOriginLocation().latitude,
                                    Origin.getInstance().getOriginLocation().longitude)
                            .enqueue(addOriginCallback);*/
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
    private Callback<NormalDao> addOriginCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (!response.body().isIsSuccess()) {
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

    public static int getSizeOfRoute() {
        return sizeOfRoute;
    }

    public static void showRoute(int which) {
        if (route != null && !route.isEmpty()) {
            if (polyline != null) {
                polyline.remove();
            }
            polyline = mMap.addPolyline(DirectionConverter.createPolyline(
                    ContextBuilder.getInstance().getContext(),
                    route.get(which).getLegList().get(0).getDirectionPoint(),
                    5, Color.parseColor(Colour.RED.getCode())));
            BicycleRoute.getInstance().setRoutePath(
                    route.get(which).getOverviewPolyline().getRawPointList());

            double length = SphericalUtil.computeLength(route.get(which).getLegList().get(0).getDirectionPoint());
            length = length / 1000.0;
            textDistance.setText(String.format("%.2f", length) + " km.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_route);

        initInstance();
        initUi();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected() && mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_LOCATION_PERMISSION:
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

    private void initInstance() {
        API_KEY = getResources().getString(R.string.google_maps_key);
        buildGoogleApiClient();

        destination = Destination.getInstance().getDestinationLocation();
    }

    protected void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private LatLng getLatLngCamera(LatLng origin, LatLng destination) {
        double originX = origin.latitude;
        double originY = origin.longitude;

        double destinationX = destination.latitude;
        double destinationY = destination.longitude;

        return new LatLng((originX + destinationX) / 2, (originY + destinationY) / 2);
    }

    private void initUi() {
        buttonRequestDirection = findViewById(R.id.button_request_direction);
        buttonRequestDirection.setOnClickListener(this);

        textDistance = findViewById(R.id.text_distance);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_route);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(CharSequence message) {
        Snackbar snackbar = Snackbar.make(buttonRequestDirection, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(getResources().getColor(R.color.white));

        snackbar.show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_request_direction) {
            HttpManager.getInstance().getService()
                    .generateOriginId()
                    .enqueue(generateOriginCallback);

            Origin.getInstance().setOriginLocation(origin);
            Origin.getInstance().setOriginNameEn(getAddressLineFromLocation(origin));
            Origin.getInstance().setOriginNameTh(getAddressLineFromLocation(origin));

            requestDirection();
        }
    }

    private String getAddressLineFromLocation(LatLng location) {
        List<Address> addresses = new ArrayList<>();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
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
                return addressLine;
            }
            return "";
        } else {
            return "";
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int hasLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        LocationAvailability availability = LocationServices.FusedLocationApi.
                getLocationAvailability(mGoogleApiClient);
        if (availability.isLocationAvailable()) {
            showToast("Location available");
            if (buttonRequestDirection.getVisibility() == View.INVISIBLE) {
                buttonRequestDirection.setVisibility(View.VISIBLE);
            }
            LocationRequest request = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(3000)
                    .setFastestInterval(100);
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
    public void onLocationChanged(Location location) {
        //showToast(location.getLatitude() + ", " + location.getLongitude());
        if (!buttonRequestDirection.isEnabled()) {
            buttonRequestDirection.setEnabled(true);
        }

        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        origin = currentLocation;
        if (buttonRequestDirection.getVisibility() == View.VISIBLE) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
        }


        if (mOriginMarker != null) {
            mOriginMarker.remove();
        }

        mOriginMarker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location)));

        /*if (directionPositionList != null) {
            if (directionPositionList.size() > 0) {
                if (isWrongWay(currentLocation, directionPositionList, DISTANCE_THRESHOLD)) {
                    showToast("Wrong way");
                }
            }
        }*/
    }

    private boolean isWrongWay(LatLng currentLocation,
                               ArrayList<LatLng> direction,
                               double threshold) {
        for (LatLng aPoint : direction) {
            double distance = SphericalUtil.computeDistanceBetween(aPoint, currentLocation);
            if (distance <= threshold) {
                return false;
            }
        }
        return true;
    }

    private void stopLocationUpdate() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,
                this
        );
    }

    private void requestDirection() {
        showSnackbar("Requesting Direction...");

        GoogleDirection.withServerKey(API_KEY)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .alternativeRoute(true)
                .unit(Unit.METRIC)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        showSnackbar(direction.getStatus());
        if (direction.isOK()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.pick_route_area, PickRouteFragment.newInstance(), "PickRouteFragment")
                    .commit();

            mMap.addMarker(new MarkerOptions().position(origin));
            mMap.addMarker(new MarkerOptions().position(destination));

            sizeOfRoute = direction.getRouteList().size();
            route = direction.getRouteList();

            polyline = mMap.addPolyline(DirectionConverter.createPolyline(
                    ContextBuilder.getInstance().getContext(),
                    route.get(0).getLegList().get(0).getDirectionPoint(),
                    5, Color.parseColor(Colour.RED.getCode())));
            /*BicycleRoute.getInstance().setPath(
                    PolyUtil.encode(route.get(0).getLegList().get(0).getDirectionPoint()));*/
            BicycleRoute.getInstance().setRoutePath(
                    route.get(0).getOverviewPolyline().getRawPointList());

            double length = SphericalUtil.computeLength(route.get(0).getLegList().get(0).getDirectionPoint());
            length = length / 1000.0;
            textDistance.setText(String.format("%.2f", length) + " km.");

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(origin);
            builder.include(destination);

            LatLngBounds bounds = builder.build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                    bounds, 70);
            mMap.animateCamera(cameraUpdate);

            buttonRequestDirection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDirectionFailure(Throwable throwable) {
        showSnackbar(throwable.getMessage());
    }
}

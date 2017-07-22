package com.example.phuwarin.followme.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.FeedbackActivity;
import com.example.phuwarin.followme.dao.CheckStatusTripDao;
import com.example.phuwarin.followme.dao.CheckStatusTripDataDao;
import com.example.phuwarin.followme.dao.FriendLocationDao;
import com.example.phuwarin.followme.dao.FriendLocationDataDao;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.route.RouteDao;
import com.example.phuwarin.followme.dao.route.RouteDataDao;
import com.example.phuwarin.followme.dao.route.RouteDestinationDao;
import com.example.phuwarin.followme.dao.route.RouteOriginDao;
import com.example.phuwarin.followme.dao.route.RouteWaypointsDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Colour;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.BicycleRoute;
import com.example.phuwarin.followme.util.detail.Destination;
import com.example.phuwarin.followme.util.detail.Origin;
import com.example.phuwarin.followme.util.detail.TripDetail;
import com.example.phuwarin.followme.util.detail.User;
import com.example.phuwarin.followme.util.detail.Waypoint;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class TravelFragment extends Fragment
        implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private static final String TAG = "FriendsLocation";
    private static final double FARTHEST_TO_NOTIFICATION_WAYPOINTS = 300.00; // in meters
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 919;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Marker meMarker, friendsMarker;
    private LatLng currentLocation;
    private List<Waypoint> waypoints;
    private List<Marker> markWaypoints;

    private AppCompatButton buttonFinish;
    private AppCompatButton buttonCheckIn;

    private boolean isLeader;

    private Call<CheckStatusTripDao> checkStatusCall;
    private Call<FriendLocationDao> friendLocationCall;
    private Call<NormalDao> updateLocationCall;

    /**
     * Listener
     */
    private DialogInterface.OnClickListener onCancelClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface,
                            int i) {
            dialogInterface.dismiss();
        }
    };
    /**
     * Callback
     **/
    private Callback<RouteDao> loadRouteDetailCallback = new Callback<RouteDao>() {
        @Override
        public void onResponse(@NonNull Call<RouteDao> call,
                               @NonNull Response<RouteDao> response) {
            if (response.isSuccessful()) {
                if (response.body().getIsSuccess()) {
                    RouteDataDao data = response.body().getRouteDataDao();
                    if (data != null) {
                        RouteOriginDao origin = data.getOrigin();
                        RouteDestinationDao dest = data.getDestination();
                        Origin.getInstance().setOrigin(origin.getId(), origin.getNameEn(),
                                origin.getNameTh(), origin.getLat(), origin.getLng());
                        Destination.getInstance().setDestination(dest.getId(), dest.getNameEn(),
                                dest.getNameTh(), dest.getLat(), dest.getLng());
                        BicycleRoute.getInstance().setBicycleRoute(data.getId(), data.getPath(),
                                Origin.getInstance(), Destination.getInstance());

                        List<RouteWaypointsDao> listWaypoints = data.getWaypoints();
                        for (RouteWaypointsDao aWaypoint : listWaypoints) {
                            Waypoint w = new Waypoint(aWaypoint.getNameEn(), aWaypoint.getNameTh(),
                                    aWaypoint.getLat(), aWaypoint.getLng(), aWaypoint.getOrder());
                            waypoints.add(w);
                        }
                        markWaypoint(waypoints);


                        drawPolyline(BicycleRoute.getInstance().getRoutePath());
                        addStartEndMarker(
                                new LatLng(origin.getLat(), origin.getLng()),
                                new LatLng(dest.getLat(), dest.getLng()));
                    }
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
        public void onFailure(@NonNull Call<RouteDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };
    private Callback<NormalDao> updateLocationCallback = new Callback<NormalDao>() {
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
    private Callback<FriendLocationDao> friendLocationCallback = new Callback<FriendLocationDao>() {
        @Override
        public void onResponse(@NonNull Call<FriendLocationDao> call,
                               @NonNull Response<FriendLocationDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    List<FriendLocationDataDao> data = response.body().getData();
                    Log.d(TAG,
                            data.get(0).getName() + " (" +
                                    data.get(0).getLat() + "," +
                                    data.get(0).getLng() + ")");
                    showFriendsLocation(data);
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
        public void onFailure(@NonNull Call<FriendLocationDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };
    private Callback<NormalDao> loadRouteTripCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    if (response.body().getData() != null) {
                        String routeId = response.body().getData();
                        BicycleRoute.getInstance().setRouteId(routeId);

                        Call<RouteDao> loadRouteDetailCall = HttpManager.getInstance().getService()
                                .loadRouteDetail(routeId);
                        loadRouteDetailCall.enqueue(loadRouteDetailCallback);
                    }

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
    private Callback<NormalDao> insertCheckInPlaceCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    showSnackbar("Store check in place successfully.");
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
    private Callback<NormalDao> finishTripCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    showSnackbar(getResources().getString(R.string.text_finish_trip_successful));
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
    private DialogInterface.OnClickListener onOkClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface,
                            int i) {
            Call<NormalDao> finishTripCall = HttpManager.getInstance().getService()
                    .finishTrip(TripDetail.getInstance().getTripId());
            finishTripCall.enqueue(finishTripCallback);
        }
    };
    private Callback<CheckStatusTripDao> checkStatusTripCallback = new Callback<CheckStatusTripDao>() {
        @Override
        public void onResponse(@NonNull Call<CheckStatusTripDao> call,
                               @NonNull Response<CheckStatusTripDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    CheckStatusTripDataDao data = response.body().getData();
                    if (data.getTripId().equalsIgnoreCase(TripDetail.getInstance().getTripId())) {
                        if (data.getIsFinish()) {
                            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
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
        public void onFailure(@NonNull Call<CheckStatusTripDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };

    /**
     * Default Constructor
     */
    public TravelFragment() {
        super();
    }

    public static TravelFragment newInstance(boolean isLeader) {
        TravelFragment fragment = new TravelFragment();
        Bundle args = new Bundle();
        args.putBoolean("isLeader", isLeader);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Overridden Method
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildGoogleApiClient();

        isLeader = getArguments().getBoolean("isLeader");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_travel, container, false);
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
        markWaypoints = new ArrayList<>();

        showSnackbar(getString(R.string.text_start_trip));

        if (!isLeader) {
            buttonFinish.setVisibility(View.GONE);
        } else {
            buttonFinish.setVisibility(View.VISIBLE);
        }

        friendLocationCall = HttpManager.getInstance().getService()
                .loadFriendsLocation(TripDetail.getInstance().getTripId());
        checkStatusCall = HttpManager.getInstance().getService()
                .checkFinishTrip(TripDetail.getInstance().getTripId());
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
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
        if (mGoogleApiClient.isConnected() && mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.button_finish) {
            showToast("button_finish");
            showDialogWhenFinish();
        }

        if (id == R.id.button_check_in) {
            Call<NormalDao> insertCheckInPlaceCall = HttpManager.getInstance().getService().insertCheckInPlace(
                    User.getInstance().getId(),
                    currentLocation.latitude,
                    currentLocation.longitude);
            insertCheckInPlaceCall.enqueue(insertCheckInPlaceCallback);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (isLeader) {
            String path = BicycleRoute.getInstance().getRoutePath();
            if (path != null && path.length() != 0) {
                drawPolyline(path);
                addStartEndMarker(Origin.getInstance().getOriginLocation(),
                        Destination.getInstance().getDestinationLocation());
            }
            waypoints = BicycleRoute.getInstance().getWaypoints();
            markWaypoint(waypoints);
        } else {
            String routeId = BicycleRoute.getInstance().getRouteId();
            if (routeId == null || routeId.length() == 0) {
                Call<RouteDao> call = HttpManager.getInstance().getService()
                        .loadRouteDetail(TripDetail.getInstance().getTripId());
                call.enqueue(loadRouteDetailCallback);
            }
            waypoints = new ArrayList<>();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        int hasLocationPermission = ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
            return;
        }
        LocationAvailability availability = LocationServices.FusedLocationApi.
                getLocationAvailability(mGoogleApiClient);
        if (availability.isLocationAvailable()) {
            showToast("Location available");

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
        showSnackbar("Google API: Connection suspended: cause = " + cause);
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        showSnackbar("Google API: Connection Failed: errorMessage = " +
                connectionResult.getErrorMessage());
    }


    @Override
    public void onLocationChanged(Location location) {
        buttonCheckIn.setVisibility(View.VISIBLE);

        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (meMarker != null) {
            meMarker.remove();
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 20));
        meMarker = mMap.addMarker(new MarkerOptions()
                .position(currentLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location)));

        updateLocationCall = HttpManager.getInstance().getService()
                .updateLocation(User.getInstance().getId(),
                        location.getLatitude(), location.getLongitude());

        callServer();
        checkNearWaypoints(currentLocation, waypoints);
    }

    /**
     * Method
     */

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        buttonFinish = rootView.findViewById(R.id.button_finish);
        buttonFinish.setOnClickListener(this);

        buttonCheckIn = rootView.findViewById(R.id.button_check_in);
        buttonCheckIn.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_master);
        mapFragment.getMapAsync(this);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    private void showToast(CharSequence message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(buttonFinish, message, Snackbar.LENGTH_LONG).show();
    }

    private void drawPolyline(String path) {
        List<LatLng> location = PolyUtil.decode(path);
        if (mMap != null) {
            mMap.addPolyline(DirectionConverter.createPolyline(
                    getContext(), (ArrayList<LatLng>) location,
                    3, Color.parseColor(Colour.BLUE.getCode())));
        }
    }

    private void showFriendsLocation(List<FriendLocationDataDao> data) {
        if (data != null && data.size() != 0) {
            String myName = User.getInstance().getName();
            for (FriendLocationDataDao person : data) {
                if (!person.getName().equals(myName)) {
                    LatLng location = new LatLng(person.getLat(), person.getLng());
                    if (friendsMarker != null) {
                        friendsMarker.remove();
                    }
                    friendsMarker = mMap.addMarker(new MarkerOptions()
                            .position(location)
                            .icon(BitmapDescriptorFactory.fromResource
                                    (R.drawable.ic_friend_location)));
                }
            }
        }
    }

    private void addStartEndMarker(LatLng origin, LatLng destination) {
        mMap.addMarker(new MarkerOptions().position(origin));
        mMap.addMarker(new MarkerOptions().position(destination));
    }

    private void showDialogWhenFinish() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setMessage("ต้องการจะสิ้นสุดการเดินทางจริงเหรอ");
        builder.setPositiveButton("จริง", onOkClickListener);
        builder.setNegativeButton("ไม่อ่ะ กดผิด", onCancelClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void callServer() {
        updateLocationCall.clone().enqueue(updateLocationCallback);
        friendLocationCall.clone().enqueue(friendLocationCallback);
        checkStatusCall.clone().enqueue(checkStatusTripCallback);
    }

    private void markWaypoint(List<Waypoint> waypoints) {
        for (Waypoint aWaypoint : waypoints) {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(aWaypoint.getLat(), aWaypoint.getLng()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_waypoint)));
            markWaypoints.add(m);
        }
    }

    private void checkNearWaypoints(LatLng currentLocation,
                                    List<Waypoint> waypoints) {
        double distanceNearestWaypoint = 999999.99;
        int indexNearestWaypoint = -1;
        for (Waypoint aWaypoint : waypoints) {
            double distance = SphericalUtil.computeDistanceBetween(
                    currentLocation, new LatLng(aWaypoint.getLat(), aWaypoint.getLng()));
            if (distance < FARTHEST_TO_NOTIFICATION_WAYPOINTS) {
                if (distance < distanceNearestWaypoint) {
                    distanceNearestWaypoint = distance;
                    indexNearestWaypoint = waypoints.indexOf(aWaypoint);
                }
                showSnackbar("จุดแวะที่ " + (indexNearestWaypoint + 1) + " : " +
                        waypoints.get(indexNearestWaypoint).getNameTh());
            }
        }
    }
}

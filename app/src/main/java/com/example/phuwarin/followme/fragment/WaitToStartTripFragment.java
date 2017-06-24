package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.MapsActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.TripDetail;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class WaitToStartTripFragment extends Fragment {

    private final String TAG = WaitToStartTripFragment.this.getClass().getSimpleName() + "TAG";
    private final int DURATION = 60;

    private ProgressBar progressBar;
    private CountDownTimer timer;

    private Call<NormalDao> getRouteFromTripCall;
    /**
     * Callback Zone
     **/
    Callback<NormalDao> loadRouteTripCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    Log.i(TAG, response.body().getData() == null ? "null" :
                            response.body().getData());

                    if (response.body().getData() != null) {
                        if (!getRouteFromTripCall.clone().isCanceled()) {
                            getRouteFromTripCall.clone().cancel();
                        }

                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), MapsActivity.class);
                        startActivity(intent);
                    } else {
                        if (!getRouteFromTripCall.clone().isCanceled()) {
                            getRouteFromTripCall.clone().enqueue(loadRouteTripCallback);
                        }
                    }
                } else {
                    showSnackbar(Constant.getInstance().getMessage(response.body().getErrorCode()));
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
            showSnackbar(throwable.toString());
        }
    };

    public WaitToStartTripFragment() {
        super();
    }

    public static WaitToStartTripFragment newInstance() {
        WaitToStartTripFragment fragment = new WaitToStartTripFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getRouteFromTripCall = HttpManager.getInstance().getService()
                .loadRouteTrip(TripDetail.getInstance().getTripId());
        timer = new CountDownTimer(DURATION * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                getActivity().finish();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wait_to_start_trip, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        progressBar = rootView.findViewById(R.id.progress_bar);
    }

    @Override
    public void onStart() {
        super.onStart();

        timer.start();
        getRouteFromTripCall.clone().enqueue(loadRouteTripCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        getRouteFromTripCall.clone().cancel();

        timer.cancel();
    }

    /**
     * Save Instance State Here
     **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /**
     * Restore Instance State Here
     **/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(progressBar, message, Snackbar.LENGTH_LONG)
                .show();
    }
}

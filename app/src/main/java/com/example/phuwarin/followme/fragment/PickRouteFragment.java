package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.MapsActivity;
import com.example.phuwarin.followme.activity.PickRouteActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.BicycleRoute;
import com.example.phuwarin.followme.util.detail.Destination;
import com.example.phuwarin.followme.util.detail.Origin;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class PickRouteFragment extends Fragment
        implements View.OnClickListener {

    /*** Listener Zone ***/
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            int x = group.indexOfChild(group.findViewById(checkedId));
            PickRouteActivity.showRoute(x);
        }
    };
    private RadioGroup radioGroupRoute;
    private AppCompatButton buttonStart;
    Callback<NormalDao> addRouteCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    Intent intent = new Intent(getActivity(), MapsActivity.class);
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
        public void onFailure(@NonNull Call<NormalDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };
    /*** Callback Zone ***/
    Callback<GenerateDao> generateTripCallback = new Callback<GenerateDao>() {
        @Override
        public void onResponse(@NonNull Call<GenerateDao> call,
                               @NonNull Response<GenerateDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    String id = response.body().getData();
                    BicycleRoute.getInstance().setRouteId(id);
                    BicycleRoute.getInstance().setRouteOrigin(Origin.getInstance());
                    BicycleRoute.getInstance().setRouteDestination(Destination.getInstance());

                    HttpManager.getInstance().getService()
                            .addBicycleRoute(
                                    BicycleRoute.getInstance().getRouteId(),
                                    BicycleRoute.getInstance().getRoutePath(),
                                    BicycleRoute.getInstance().getRouteOrigin().getOriginId(),
                                    BicycleRoute.getInstance().getRouteDestination().getDestinationId()
                            ).enqueue(addRouteCallback);


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
        radioGroupRoute = rootView.findViewById(R.id.rg_route);
        buttonStart = rootView.findViewById(R.id.button_start);
        buttonStart.setOnClickListener(this);
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
        int size = PickRouteActivity.getSizeOfRoute();
        for (int i = radioGroupRoute.getChildCount() - 1; i >= size; i--) {
            radioGroupRoute.getChildAt(i).setVisibility(View.GONE);
        }
        radioGroupRoute.check(radioGroupRoute.getChildAt(0).getId());
        radioGroupRoute.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonStart) {
            HttpManager.getInstance().getService()
                    .generateRouteId()
                    .enqueue(generateTripCallback);
        }
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(buttonStart, message, Snackbar.LENGTH_LONG).show();
    }
}

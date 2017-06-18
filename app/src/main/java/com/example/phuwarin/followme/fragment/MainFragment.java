package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.AddMemberActivity;
import com.example.phuwarin.followme.activity.WaitingActivity;
import com.example.phuwarin.followme.dao.position.PositionDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.detail.PositionCollection;
import com.example.phuwarin.followme.util.detail.User;
import com.facebook.AccessToken;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    private static AppCompatButton buttonLeader;
    private static AppCompatButton buttonFollower;
    /**
     * Callback
     **/
    Callback<PositionDao> positionDaoCallback = new Callback<PositionDao>() {
        @Override
        public void onResponse(@NonNull Call<PositionDao> call,
                               @NonNull Response<PositionDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    PositionCollection.getInstance().setPositionList(response.body().getData());
                } else {
                    showSnackbar("errorCode = " + response.body().getErrorCode());
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
        public void onFailure(@NonNull Call<PositionDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
        }
    };
    private Call<PositionDao> call;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static void turnOnButton() {
        buttonLeader.setEnabled(true);
        buttonFollower.setEnabled(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        initInstances(rootView);

        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        buttonLeader = rootView.findViewById(R.id.button_leader);
        buttonFollower = rootView.findViewById(R.id.button_follower);
        buttonLeader.setOnClickListener(this);
        buttonFollower.setOnClickListener(this);

        if (AccessToken.getCurrentAccessToken() != null) {
            turnOnButton();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        call = HttpManager.getInstance().getService().loadPosition();
        call.enqueue(positionDaoCallback);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        if (view == buttonLeader) {
            Intent intent = new Intent(getActivity(), AddMemberActivity.class);
            startActivity(intent);
        }
        if (view == buttonFollower) {
            User.getInstance().setPosition(
                    PositionCollection.getInstance().getPositionList().get(0).getId()
            );

            Intent intent = new Intent(getActivity(), WaitingActivity.class);
            startActivity(intent);
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(buttonFollower, message, Snackbar.LENGTH_LONG)
                .show();
    }
}


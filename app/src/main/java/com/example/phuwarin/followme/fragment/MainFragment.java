package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.AddMemberActivity;
import com.example.phuwarin.followme.activity.WaitingActivity;
import com.example.phuwarin.followme.manager.SharedPreferenceHandler;
import com.facebook.AccessToken;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class MainFragment extends Fragment implements View.OnClickListener {

    private static AppCompatButton buttonLeader;
    private static AppCompatButton buttonFollower;

    public MainFragment() {
        super();
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        buttonLeader = (AppCompatButton) rootView.findViewById(R.id.button_leader);
        buttonFollower = (AppCompatButton) rootView.findViewById(R.id.button_follower);
        buttonLeader.setOnClickListener(this);
        buttonFollower.setOnClickListener(this);

        if (AccessToken.getCurrentAccessToken() != null) {
            turnOnButton();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
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
            Intent intent = new Intent(getActivity(), WaitingActivity.class);
            startActivity(intent);
        }
    }

    public static void turnOnButton() {
        buttonLeader.setEnabled(true);
        buttonFollower.setEnabled(true);
    }
}


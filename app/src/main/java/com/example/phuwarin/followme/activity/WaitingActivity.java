package com.example.phuwarin.followme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.MemberAreaFragment;
import com.example.phuwarin.followme.fragment.WaitingFragment;

public class WaitingActivity extends AppCompatActivity {

    private static final String TAG = "WaitingActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.member_area, MemberAreaFragment.newInstance())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.waiting_area, WaitingFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d(TAG, "onBackPressed");
    }
}

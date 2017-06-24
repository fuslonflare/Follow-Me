package com.example.phuwarin.followme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.WaitingFragment;

public class WaitingActivity extends AppCompatActivity {

    private static final String TAG = "WaitingActivityTAG";
    private static final String TAG2 = "LifeCycleTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.waiting_area, WaitingFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Log.d(TAG2, "Activity onBackPressed");
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG2, "Activity onStop");
    }
}

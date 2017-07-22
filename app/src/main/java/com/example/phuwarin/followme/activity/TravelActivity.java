package com.example.phuwarin.followme.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.TravelFragment;

public class TravelActivity extends FragmentActivity {

    @Override
    public void onBackPressed() {
        // TODO: Abort trip
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        boolean isLeader = getIntent().getBooleanExtra("isLeader", false);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.travel_area, TravelFragment.newInstance(isLeader), "TravelFragment")
                    .commit();
        }
    }


}

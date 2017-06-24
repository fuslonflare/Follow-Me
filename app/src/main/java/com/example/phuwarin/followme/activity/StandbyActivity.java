package com.example.phuwarin.followme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.StandbyFragment;

public class StandbyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standby);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.standby_area, StandbyFragment.newInstance())
                    .commit();

        }
    }
}

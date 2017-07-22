package com.example.phuwarin.followme.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.FeedbackFragment;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.feedback_area, FeedbackFragment.newInstance())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {

    }
}

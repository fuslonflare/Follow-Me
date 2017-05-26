package com.example.phuwarin.followme.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.fragment.AddMemberFragment;
import com.example.phuwarin.followme.fragment.MemberAreaFragment;

public class AddMemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.add_member_area, AddMemberFragment.newInstance())
                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.member_area, MemberAreaFragment.newInstance())
                    .commit();
        }
    }
}

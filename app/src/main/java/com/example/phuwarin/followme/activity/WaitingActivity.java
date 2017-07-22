package com.example.phuwarin.followme.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.fragment.WaitingFragment;
import com.example.phuwarin.followme.util.Constant;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitingActivity extends AppCompatActivity {

    private static final String TAG = "WaitingActivityTAG";
    private static final String TAG2 = "LifeCycleTAG";

    /**
     * Callback Zone **/
    Callback<NormalDao> deleteUserCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (!response.body().isIsSuccess()) {
                    showSnackbar(Constant.getInstance().getMessage(
                            response.body().getErrorCode()));
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
    protected void onStop() {
        super.onStop();

        /*HttpManager.getInstance().getService()
                .deleteUserJoinTrip(User.getInstance().getId())
                .enqueue(deleteUserCallback);*/
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(findViewById(R.id.waiting_area), message, Snackbar.LENGTH_LONG).show();
    }
}

package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.TravelActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.JoinTripDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.manager.UserSharedPreferenceHandler;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.TripDetail;
import com.example.phuwarin.followme.util.detail.User;

import net.glxn.qrgen.android.QRCode;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class WaitingFragment extends Fragment {

    /**
     * Constant variable
     **/

    private static final String TAG = "RetrofitTAG";
    private static final String TAG2 = "LifeCycleTAG";
    private static final String TAG3 = "TimerTAG";
    private static final int DURATION = 60;

    /**
     * Member variable **/
    private ImageView imageQrCode;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private AppCompatTextView textMemberId;
    private AppCompatTextView tvCountdown;

    private String memberId;
    private String tripId;
    private boolean wantStop;
    private long timeRemaining;
    private Call<JoinTripDao> joinTripCall;
    Callback<JoinTripDao> joinTripCallback = new Callback<JoinTripDao>() {
        @Override
        public void onResponse(@NonNull Call<JoinTripDao> call,
                               @NonNull Response<JoinTripDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    if (response.body().getJoinTripDataDao() != null) {
                        tripId = response.body().getJoinTripDataDao().getTripId();
                        Log.i(TAG, tripId == null ? "null" : tripId);
                        if (tripId != null) {
                            joinTripCall.clone().cancel();
                            wantStop = true;

                            TripDetail.getInstance().setTripId(tripId);

                            Intent intent = new Intent(getActivity(), TravelActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                        if (!wantStop) {
                            joinTripCall.clone().enqueue(joinTripCallback);
                        } else {
                            joinTripCall.clone().cancel();
                        }
                    }
                } else {
                    showSnackbar(Constant.getInstance().getMessage(response.body().getErrorCode()));
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
        public void onFailure(@NonNull Call<JoinTripDao> call,
                              @NonNull Throwable t) {
            showSnackbar(t.toString());
        }
    };
    /**
     * Callback Zone **/

    Callback<NormalDao> insertUserCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    joinTripCall = HttpManager.getInstance().getService()
                            .loadStatusJoinTrip(memberId);
                    joinTripCall.clone().enqueue(joinTripCallback);
                } else {
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

    /**
     * Default Constructor */
    public WaitingFragment() {
        super();
    }

    public static WaitingFragment newInstance() {
        WaitingFragment fragment = new WaitingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Overridden method */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tripId = null;
        wantStop = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiting, container, false);
        initInstances(rootView);
        return rootView;
    }

    /**
     * Restore Instance State Here
     **/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            timeRemaining = DURATION * 1000;
        } else {
            timeRemaining = savedInstanceState.getLong("timeRemaining", DURATION * 1000);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        String id = User.getInstance().getId();
        String name = User.getInstance().getName();
        String position = User.getInstance().getPosition();
        HttpManager.getInstance().getService()
                .addMember(id, name, position)
                .enqueue(insertUserCallback);

        progressBar.setProgress(100);
        timer = new CountDownTimer(timeRemaining, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG3, millisUntilFinished / 1000 + "");
                tvCountdown.setText(String.valueOf(millisUntilFinished / 1000));

                timeRemaining = millisUntilFinished;

                int progress = (int) (100 * millisUntilFinished / (DURATION * 1000));
                if (progressBar != null) {
                    progressBar.setProgress(progress);
                }
            }

            @Override
            public void onFinish() {
                getActivity().finish();
            }
        }.start();
    }

    /**
     * Save Instance State Here
     **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putLong("timeRemaining", timeRemaining);
    }

    @Override
    public void onStop() {
        super.onStop();

        wantStop = true;
        timer.cancel();
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        imageQrCode = rootView.findViewById(R.id.image_qr);
        textMemberId = rootView.findViewById(R.id.text_member_id);
        tvCountdown = rootView.findViewById(R.id.text_countdown);
        progressBar = rootView.findViewById(R.id.progress_bar);

        memberId = UserSharedPreferenceHandler
                .getInstance()
                .getMemberId(getContext());
        textMemberId.setText(memberId);


        Bitmap myQr = QRCode.from(memberId)
                .bitmap();
        imageQrCode.setImageBitmap(myQr);
    }

    private void showSnackbar(CharSequence message) {
        Snackbar snackbar = Snackbar.make(imageQrCode, message, Snackbar.LENGTH_LONG);
        View snackbarView = snackbar.getView();

        int snackbarTextId = android.support.design.R.id.snackbar_text;
        TextView textView = snackbarView.findViewById(snackbarTextId);
        textView.setTextColor(getResources().getColor(R.color.white));

        snackbar.show();
    }

    private void showToast(CharSequence message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

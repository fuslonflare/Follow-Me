package com.example.phuwarin.followme.fragment;

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

import com.example.phuwarin.followme.R;
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

public class WaitToAddMemberFragment extends Fragment {

    private static final String TAG = "RetrofitTAG";
    private static final String TAG2 = "LifeCycleTAG";
    private static final int DURATION = 60;

    private ImageView imageQrCode;
    private ProgressBar progressBar;
    private CountDownTimer timer;
    private AppCompatTextView textMemberId;

    private String memberId;
    private String tripId;
    private boolean wantStop;

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

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.waiting_area, WaitToStartTripFragment.newInstance())
                                    .commit();
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
     * Callback Zone
     **/

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

    public WaitToAddMemberFragment() {
        super();
    }

    public static WaitToAddMemberFragment newInstance() {
        WaitToAddMemberFragment fragment = new WaitToAddMemberFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tripId = null;
        wantStop = false;

        timer = new CountDownTimer(DURATION * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int progress = (int) (100 * (millisUntilFinished / 1000) / DURATION);
                if (progressBar != null) {
                    progressBar.setProgress(progress);
                }
            }

            @Override
            public void onFinish() {
                getActivity().finish();
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wait_to_add_member, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        imageQrCode = rootView.findViewById(R.id.image_qr);
        textMemberId = rootView.findViewById(R.id.text_member_id);
        progressBar = rootView.findViewById(R.id.progress_bar);

        memberId = UserSharedPreferenceHandler
                .getInstance()
                .getMemberId(getContext());
        textMemberId.setText(memberId);


        Bitmap myQr = QRCode.from(memberId)
                .bitmap();
        imageQrCode.setImageBitmap(myQr);
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
        timer.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG2, "Fragment onStop");
        Log.d(TAG, "onStop");
        wantStop = true;

        timer.cancel();
    }

    /**
     * Save Instance State Here
     **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /**
     * Restore Instance State Here
     **/
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(imageQrCode, message, Snackbar.LENGTH_LONG).show();
    }
}

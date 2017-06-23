package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.StandbyActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.JoinTripDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.manager.UserSharedPreferenceHandler;
import com.example.phuwarin.followme.util.Constant;
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

    private static final String TAG = "RetrofitTAG";
    private static final String TAG2 = "LifeCycleTAG";

    private ImageView imageQrCode;
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

                            getActivity().finish();
                            Intent intent = new Intent(getActivity(), StandbyActivity.class);
                            getActivity().startActivity(intent);
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
                    joinTripCall = HttpManager.getInstance()
                            .getService().loadStatusJoinTrip(memberId);
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

    public WaitingFragment() {
        super();
    }

    public static WaitingFragment newInstance() {
        WaitingFragment fragment = new WaitingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

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

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        imageQrCode = rootView.findViewById(R.id.image_qr);
        AppCompatTextView textMemberId = rootView.findViewById(R.id.text_member_id);

        memberId = UserSharedPreferenceHandler
                .getInstance()
                .getMemberId(getContext());

        Bitmap myQr = QRCode.from(memberId)
                .bitmap();
        imageQrCode.setImageBitmap(myQr);

        textMemberId.setText(memberId);
    }

    @Override
    public void onStart() {
        super.onStart();

        String id = User.getInstance().getId();
        String name = User.getInstance().getName();
        String position = User.getInstance().getPosition();
        HttpManager.getInstance().getService().addMember(
                id, name, position).enqueue(insertUserCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG2, "onStop");
        Log.d(TAG, "onStop");
        wantStop = true;

        HttpManager.getInstance().getService().deleteUserJoinTrip(
                User.getInstance().getId()).enqueue(new Callback<NormalDao>() {
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
        });
    }

    /** Save Instance State Here **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /** Restore Instance State Here **/
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

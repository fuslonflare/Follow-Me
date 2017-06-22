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
import com.example.phuwarin.followme.dao.trip.JoinTripDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.manager.UserSharedPreferenceHandler;

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

    private ImageView imageQrCode;

    private AppCompatTextView textMemberId;
    private String memberId;
    private String tripId;
    private boolean wantStop;
    private Call<JoinTripDao> joinTripCall;
    /**
     * Callback Zone
     **/

    Callback<JoinTripDao> joinTripCallback = new Callback<JoinTripDao>() {
        @Override
        public void onResponse(@NonNull Call<JoinTripDao> call,
                               @NonNull Response<JoinTripDao> response) {
            if (response.isSuccessful()) {
                if (response.body().getJoinTripDataDao() != null) {
                    tripId = response.body().getJoinTripDataDao().getTripId();
                    Log.i(TAG, tripId == null ? "null" : tripId);
                /*Toast.makeText(getActivity(),
                        tripId == null ? "null" : tripId,
                        Toast.LENGTH_SHORT)
                        .show();*/
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
                try {
                    /*Toast.makeText(getActivity(), response.errorBody().string(),
                            Toast.LENGTH_SHORT)
                            .show();*/
                    Log.i(TAG, response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(@NonNull Call<JoinTripDao> call,
                              @NonNull Throwable t) {
            /*Toast.makeText(getActivity(), t.toString(),
                    Toast.LENGTH_SHORT)
                    .show();*/
            Log.i(TAG, t.toString());
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
        textMemberId = rootView.findViewById(R.id.text_member_id);

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

        joinTripCall = HttpManager.getInstance().getService().loadStatusJoinTrip(memberId);
        joinTripCall.clone().enqueue(joinTripCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        wantStop = true;
    }

    /*
     * Save Instance State Here
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    /*
     * Restore Instance State Here
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore Instance State here
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(imageQrCode, message, Snackbar.LENGTH_LONG).show();
    }
}

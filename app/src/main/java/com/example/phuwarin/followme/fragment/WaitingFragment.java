package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.StandbyActivity;
import com.example.phuwarin.followme.dao.JoinTripMasterDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.manager.SharedPreferenceHandler;

import net.glxn.qrgen.android.QRCode;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class WaitingFragment extends Fragment {

    ImageView imageQrCode;
    AppCompatTextView textMemberId;

    String memberId;
    String tripId;
    boolean isEnqueue;
    boolean wantStop;
    Call<JoinTripMasterDao> mainCall;

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
        isEnqueue = false;
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
        imageQrCode = (ImageView) rootView.findViewById(R.id.image_qr);
        textMemberId = (AppCompatTextView) rootView.findViewById(R.id.text_member_id);

        memberId = SharedPreferenceHandler
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

        mainCall = HttpManager.getInstance().getService().loadStatusJoinTrip(memberId);
        mainCall.clone().enqueue(joinTripCallback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("RetrofitTAG", "onStop");
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

    /** Callback Zone **/

    Callback<JoinTripMasterDao> joinTripCallback = new Callback<JoinTripMasterDao>() {
        @Override
        public void onResponse(Call<JoinTripMasterDao> call,
                               Response<JoinTripMasterDao> response) {
            if (response.isSuccessful()) {
                tripId = response.body().getData().get(0).getTripId();
                Log.i("RetrofitTAG", tripId == null ? "null" : tripId);
                /*Toast.makeText(getActivity(),
                        tripId == null ? "null" : tripId,
                        Toast.LENGTH_SHORT)
                        .show();*/
                if (tripId != null) {
                    mainCall.clone().cancel();
                    wantStop = true;
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), StandbyActivity.class);
                    getActivity().startActivity(intent);
                }
                if (!wantStop) {
                    mainCall.clone().enqueue(joinTripCallback);
                } else {
                    mainCall.clone().cancel();
                }
            } else {
                try {
                    /*Toast.makeText(getActivity(), response.errorBody().string(),
                            Toast.LENGTH_SHORT)
                            .show();*/
                    Log.i("RetrofitTAG", response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call<JoinTripMasterDao> call,
                              Throwable t) {
            /*Toast.makeText(getActivity(), t.toString(),
                    Toast.LENGTH_SHORT)
                    .show();*/
            Log.i("RetrofitTAG", t.toString());
        }
    };
}

package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.PickDestinationActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.GenerateTripDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.detail.TripDetail;
import com.example.phuwarin.followme.util.detail.User;
import com.example.phuwarin.followme.view.AddMemberField;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class AddMemberFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_SCAN_QR_CODE = 9;
    private static final String TAG = "AddMemberFragmentTAG";

    private Map<String, String> memberJoinTrip;
    private int countComplete;

    private AppCompatButton buttonNext;
    Callback<NormalDao> addTripCallback2 = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    getActivity().finish();

                    Intent intent = new Intent(getActivity(), PickDestinationActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    showSnackbar("Error code: " + response.body().getErrorCode());
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
            showSnackbar(throwable.toString());
        }
    };
    Callback<GenerateTripDao> generateTripCallback = new Callback<GenerateTripDao>() {
        @Override
        public void onResponse(@NonNull Call<GenerateTripDao> call,
                               @NonNull Response<GenerateTripDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    TripDetail.getInstance().setTripId(
                            response.body().getData());
                    Call<NormalDao> addTripCall = HttpManager.getInstance().getService().addTrip(
                            User.getInstance().getId(),
                            TripDetail.getInstance().getTripId());
                    addTripCall.enqueue(addTripCallback2);
                } else {
                    showSnackbar("Error code: " + response.body().getErrorCode());
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
        public void onFailure(@NonNull Call<GenerateTripDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.toString());
        }
    };
    private AppCompatButton buttonCancel;
    private View rootView;
    private LinearLayout parent;
    private String idFromUser;
    /**
     * Callback Zone
     **/
    GraphRequest.Callback graphRequestCallback = new GraphRequest.Callback() {
        @Override
        public void onCompleted(GraphResponse response) {
            if (response.getError() == null) {
                JSONObject aResponse = response.getJSONObject();
                String name = null;
                String id = null;
                try {
                    name = aResponse.getString("name");
                    id = aResponse.getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (name == null || id == null) {
                    showToast("ID Invalid, Please recheck.");
                    return;
                }

                memberJoinTrip.put(id, name);
                countComplete = countComplete + 1;

                Log.i(TAG, memberJoinTrip.toString());

                if (countComplete == parent.getChildCount()) {
                    Log.d(TAG, "Go to PickDestinationActivity");

                    TripDetail.getInstance().setListMember(memberJoinTrip);
                    Call<GenerateTripDao> generateTripCall = HttpManager.getInstance()
                            .getService().generateTripId();
                    generateTripCall.enqueue(generateTripCallback);
                }
            } else {
                countComplete = 0;
                showToast("ID " + idFromUser + " invalid.");
            }
        }
    };

    public AddMemberFragment() {
        super();
    }

    public static AddMemberFragment newInstance() {
        AddMemberFragment fragment = new AddMemberFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        memberJoinTrip = new HashMap<>();
        countComplete = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_member, container, false);
        initInstances(rootView);
        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        buttonNext = rootView.findViewById(R.id.button_next);
        buttonCancel = rootView.findViewById(R.id.button_cancel);

        buttonNext.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
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

    @Override
    public void onClick(View view) {
        /*if (view == buttonQrCode) {
            Intent intent = new Intent(getContext(), CaptureActivity.class);
            intent.setAction("com.google.zxing.client.android.SCAN");
            intent.putExtra("SAVE_HISTORY", false);
            startActivityForResult(intent, REQUEST_SCAN_QR_CODE);
        }*/

        if (view == buttonCancel) {
            getActivity().finish();
        }

        if (view == buttonNext) {
            // TODO: DB-User


            storedMemberById();

            // TODO: DB-CreateTrip
            // TODO: DB-JoinTrip
            // TODO: link to ChoosePlaceActivity
        }

        /*if (view == buttonAddMember) {
            showToast("add member");
            LayoutInflater inflater = (LayoutInflater) getActivity().
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout parent = (LinearLayout) rootView.findViewById(R.id.parent_add_member);
            View custom = inflater.inflate(R.layout.field_add_member, null);
            parent.addView(custom);
        }*/
    }

    private void storedMemberById() {
        parent = rootView.findViewById(R.id.parent_add_member);

        for (int i = 0; i < parent.getChildCount(); i++) {
            idFromUser = ((AddMemberField) parent.getChildAt(i)).getMemberId();
            if (!idFromUser.isEmpty()) {
                GraphRequest graphRequest = new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + idFromUser,
                        null, HttpMethod.GET, graphRequestCallback
                );
                graphRequest.executeAsync();
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showSnackbar(String message) {
        Snackbar.make(buttonNext, message, Snackbar.LENGTH_LONG).show();
    }
}

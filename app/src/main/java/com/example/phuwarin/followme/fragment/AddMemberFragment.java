package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.StandbyActivity;
import com.example.phuwarin.followme.view.AddMemberField;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class AddMemberFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_SCAN_QR_CODE = 9;
    private static final String TAG = "AddMemberFragmentTAG";

    private Map<String, String> memberJoinTrip;
    private int countComplete;

    private AppCompatButton buttonNext;
    private AppCompatButton buttonCancel;
    private View rootView;

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
        buttonNext = (AppCompatButton) rootView.findViewById(R.id.button_next);
        buttonCancel = (AppCompatButton) rootView.findViewById(R.id.button_cancel);

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
        final LinearLayout parent = (LinearLayout) rootView.findViewById(R.id.parent_add_member);

        for (int i = 0; i < parent.getChildCount(); i++) {
            final String idFromUser = ((AddMemberField) parent.getChildAt(i)).getMemberId();
            if (!idFromUser.isEmpty()) {
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "/" + idFromUser,
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
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
                                        Log.d(TAG, "Go to StandbyActivity");
                                        getActivity().finish();
                                        Intent intent = new Intent(getActivity(), StandbyActivity.class);
                                        getActivity().startActivity(intent);
                                    }
                                } else {
                                    countComplete = 0;
                                    showToast("ID " + idFromUser + " invalid.");
                                }
                            }
                        }
                ).executeAsync();
            }
        }
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}

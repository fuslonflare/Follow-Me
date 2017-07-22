package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.TravelActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.dao.trip.GenerateDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.Constant;
import com.example.phuwarin.followme.util.detail.TripDetail;
import com.example.phuwarin.followme.util.detail.User;
import com.example.phuwarin.followme.view.AddMemberField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class AddMemberFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_SCAN_QR_CODE = 9;
    private static final String TAG = "AddMemberFragmentTAG";

    private AppCompatButton buttonCancel;
    private AppCompatButton buttonStart;

    /**
     * Callback Zone
     **/

    private Callback<NormalDao> insertUserCallback = new Callback<NormalDao>() {
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

    private Callback<NormalDao> addMemberToJoinTripCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    getActivity().finish();

                    Intent intent = new Intent(getActivity(), TravelActivity.class);
                    intent.putExtra("isLeader", true);
                    getActivity().startActivity(intent);
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
    private View rootView;
    private List<String> listIdMember;
    private Callback<NormalDao> checkUserExistCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    HttpManager.getInstance().getService()
                            .addMemberToJoinTrip(extractMemberIdFromList(listIdMember),
                                    TripDetail.getInstance().getTripId())
                            .enqueue(addMemberToJoinTripCallback);
                } else {
                    int errorCode = response.body().getErrorCode();
                    if (errorCode == 352) {
                        Constant.getInstance().setMessageErrorCode352(
                                addSpaceAfterComma(response.body().getData()));
                    }
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
    private Callback<NormalDao> addTripCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    HttpManager.getInstance().getService()
                            .addMemberToJoinTrip(extractMemberIdFromList(listIdMember),
                                    TripDetail.getInstance().getTripId())
                            .enqueue(addMemberToJoinTripCallback);
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
    private Callback<GenerateDao> generateTripCallback = new Callback<GenerateDao>() {
        @Override
        public void onResponse(@NonNull Call<GenerateDao> call,
                               @NonNull Response<GenerateDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    TripDetail.getInstance().setTripId(
                            response.body().getData());
                    HttpManager.getInstance().getService()
                            .addTrip(User.getInstance().getId(),
                                    TripDetail.getInstance().getTripId())
                            .enqueue(addTripCallback);
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
        public void onFailure(@NonNull Call<GenerateDao> call,
                              @NonNull Throwable throwable) {
            showSnackbar(throwable.getMessage());
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_member, container, false);
        initInstances(rootView);
        return rootView;
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

    /** Save Instance State Here **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save Instance State here
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        buttonStart = rootView.findViewById(R.id.button_start);
        buttonCancel = rootView.findViewById(R.id.button_cancel);

        buttonStart.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
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

        if (view == buttonStart) {
            storedMemberById();
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
        LinearLayout parent = rootView.findViewById(R.id.parent_add_member);
        listIdMember = new ArrayList<>();

        for (int i = 0; i < parent.getChildCount(); i++) {
            String id = ((AddMemberField) parent.getChildAt(i)).getMemberId();
            if (!id.isEmpty()) {
                listIdMember.add(id);
            }
        }
        if (!listIdMember.isEmpty()) {
            TripDetail.getInstance().setListMember(listIdMember);
            HttpManager.getInstance().getService()
                    .checkUserExist(extractMemberIdFromList(listIdMember))
                    .enqueue(checkUserExistCallback);
        } else {
            showSnackbar(getString(R.string.error_message_351));
        }
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(buttonStart, message, Snackbar.LENGTH_LONG).show();
    }

    private String extractMemberIdFromList(List<String> listMember) {
        String output = "";
        for (String id : listMember) {
            output = output + id + ",";
        }
        return output.substring(0, output.length() - 1);
    }

    private String addSpaceAfterComma(String body) {
        String output = body;
        output = output.substring(0, body.length() - 1);
        String[] listMemberProblem = output.split(",");
        output = "";
        for (String aMemberHasProblem : listMemberProblem) {
            output = output + aMemberHasProblem + ", ";
        }
        output = output.substring(0, output.length() - 2);
        return output;
    }
}

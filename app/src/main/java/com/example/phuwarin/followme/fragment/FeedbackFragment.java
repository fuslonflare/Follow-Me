package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.activity.MainActivity;
import com.example.phuwarin.followme.dao.NormalDao;
import com.example.phuwarin.followme.manager.HttpManager;
import com.example.phuwarin.followme.util.detail.TripDetail;
import com.example.phuwarin.followme.util.detail.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class FeedbackFragment extends Fragment implements View.OnClickListener {

    private AppCompatEditText etFeedback;
    private AppCompatButton buttonSubmit;
    private AppCompatButton buttonSkip;

    private Intent intent;

    /**
     * Callback
     */
    private Callback<NormalDao> sendFeedbackCallback = new Callback<NormalDao>() {
        @Override
        public void onResponse(@NonNull Call<NormalDao> call,
                               @NonNull Response<NormalDao> response) {
            if (response.isSuccessful()) {
                if (response.body().isIsSuccess()) {
                    Snackbar.make(buttonSkip, getString(R.string.text_send_feedback_success),
                            Snackbar.LENGTH_LONG)
                            .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                @Override
                                public void onDismissed(Snackbar transientBottomBar, int event) {
                                    startActivity(intent);
                                    super.onDismissed(transientBottomBar, event);
                                }
                            }).show();
                    showSnackbar(getString(R.string.text_send_feedback_success));
                } else {
                    showSnackbar(getString(R.string.text_send_feedback_fail));
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

    public FeedbackFragment() {
        super();
    }

    public static FeedbackFragment newInstance() {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        initInstances(rootView);
        return rootView;
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
        intent = new Intent(getActivity(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /*
     * Save Instance State Here
     */
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
        etFeedback = rootView.findViewById(R.id.edit_feedback);
        etFeedback.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        buttonSkip = rootView.findViewById(R.id.button_skip);
        buttonSubmit = rootView.findViewById(R.id.button_submit_feedback);

        buttonSkip.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSkip) {
            startActivity(intent);
        }
        if (view == buttonSubmit) {
            buttonSkip.setEnabled(false);
            sendFeedback(User.getInstance().getId(),
                    TripDetail.getInstance().getTripId(),
                    getFeedback());
        }
    }

    private void showSnackbar(CharSequence message) {
        Snackbar.make(buttonSkip, message, Snackbar.LENGTH_LONG).show();
    }

    private String getFeedback() {
        return etFeedback.getText().toString();
    }

    private void sendFeedback(String userId,
                              String tripId,
                              String message) {
        Call<NormalDao> call = HttpManager.getInstance().getService()
                .sendFeedback(userId, tripId, message);
        call.enqueue(sendFeedbackCallback);
    }
}

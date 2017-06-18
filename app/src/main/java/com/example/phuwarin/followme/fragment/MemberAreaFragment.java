package com.example.phuwarin.followme.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phuwarin.followme.R;
import com.example.phuwarin.followme.manager.SharedPreferenceHandler;
import com.example.phuwarin.followme.util.detail.User;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class MemberAreaFragment extends Fragment {

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;

    private LinearLayout loginArea;
    private LinearLayout memberArea;
    /**
     * Callback Zone
     **/
    FacebookCallback<LoginResult> facebookLoginCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            showToast("Login onSuccess");
            unlockMemberArea();
            MainFragment.turnOnButton();
        }

        @Override
        public void onCancel() {
            showToast("Login onCancel");
        }

        @Override
        public void onError(FacebookException error) {
            showToast("Login onError: " + error.getMessage());
        }
    };
    private AppCompatTextView userName;
    private AppCompatTextView userId;
    private ImageView userPhoto;

    public MemberAreaFragment() {
        super();
    }

    public static MemberAreaFragment newInstance() {
        MemberAreaFragment fragment = new MemberAreaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,
                                                   Profile currentProfile) {
                if (currentProfile != null) {
                    SharedPreferenceHandler
                            .getInstance()
                            .setMemberName(getContext(), currentProfile.getName());
                    SharedPreferenceHandler
                            .getInstance()
                            .setMemberPhoto(getContext(), currentProfile.getProfilePictureUri(200, 200).toString());
                    SharedPreferenceHandler
                            .getInstance()
                            .setMemberId(getContext(), currentProfile.getId());
                    setMemberData();
                }
            }
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_member, container, false);
        initInstances(rootView);

        if (AccessToken.getCurrentAccessToken() != null) {
            unlockMemberArea();
            setMemberData();
        }

        return rootView;
    }

    private void initInstances(View rootView) {
        // Init 'View' instance(s) with rootView.findViewById here
        LoginButton loginButton = rootView.findViewById(R.id.facebook_login_button);
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, facebookLoginCallback);

        loginArea = rootView.findViewById(R.id.login_area);
        memberArea = rootView.findViewById(R.id.profile_area);
        userName = rootView.findViewById(R.id.user_name);
        userId = rootView.findViewById(R.id.user_id);
        userPhoto = rootView.findViewById(R.id.user_photo);
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        profileTracker.startTracking();
    }

    @Override
    public void onStop() {
        super.onStop();
        profileTracker.stopTracking();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setMemberData() {
        String name = SharedPreferenceHandler.getInstance().getMemberName(getContext());
        String photo = SharedPreferenceHandler.getInstance().getMemberPhoto(getContext());
        String id = SharedPreferenceHandler.getInstance().getMemberId(getContext());
        userName.setText(name);
        userId.setText(id);
        Glide.with(getContext())
                .load(photo)
                .bitmapTransform(new CropCircleTransformation(getContext()))
                .into(userPhoto);

        User.getInstance().setId(id);
        User.getInstance().setName(name);
    }

    private void unlockMemberArea() {
        loginArea.setVisibility(View.GONE);
        memberArea.setVisibility(View.VISIBLE);
    }
}

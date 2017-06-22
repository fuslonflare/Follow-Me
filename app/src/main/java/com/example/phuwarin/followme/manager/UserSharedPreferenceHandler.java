package com.example.phuwarin.followme.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class UserSharedPreferenceHandler {

    private static UserSharedPreferenceHandler instance;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context mContext;

    // Lock default constructor.
    private UserSharedPreferenceHandler() {
        mContext = ContextBuilder.getInstance().getContext();
    }

    public static UserSharedPreferenceHandler getInstance() {
        if (instance == null) {
            instance = new UserSharedPreferenceHandler();
        }
        return instance;
    }

    public String getMemberName(Context context) {
        sharedPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }

    public void setMemberName(Context context, String name) {
        sharedPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.apply();
    }

    public String getMemberPhoto(Context context) {
        sharedPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE);
        return sharedPreferences.getString("photo", "");
    }

    public void setMemberPhoto(Context context, String photo) {
        sharedPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("photo", photo);
        editor.apply();
    }

    public String getMemberId(Context context) {
        sharedPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE);
        return sharedPreferences.getString("id", "");
    }

    public void setMemberId(Context context, String id) {
        sharedPreferences = context.getSharedPreferences("member", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("id", id);
        editor.apply();
    }
}

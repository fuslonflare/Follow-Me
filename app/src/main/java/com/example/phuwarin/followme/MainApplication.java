package com.example.phuwarin.followme;

import android.app.Application;

import com.example.phuwarin.followme.manager.ContextBuilder;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class MainApplication extends Application {

    private static final String TAG = "LifeCycleTAG";

    @Override
    public void onCreate() {
        super.onCreate();

        ContextBuilder.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

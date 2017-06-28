package com.example.phuwarin.followme;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.example.phuwarin.followme.manager.ContextBuilder;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class MainApplication extends Application {

    private static final String TAG = "LifeCycleTAG";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        ContextBuilder.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
}

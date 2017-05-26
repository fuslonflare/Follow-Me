package com.example.phuwarin.followme.manager;

import android.content.Context;

/**
 * Created by Phuwarin on 4/5/2017.
 */

public class ContextBuilder {
    private static ContextBuilder instance;

    public static ContextBuilder getInstance() {
        if (instance == null)
            instance = new ContextBuilder();
        return instance;
    }

    private Context mContext;

    private ContextBuilder() {
        // default constructor
    }

    public void init(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

}

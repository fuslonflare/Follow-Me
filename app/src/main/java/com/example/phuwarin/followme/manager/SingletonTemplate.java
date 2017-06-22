package com.example.phuwarin.followme.manager;

import android.content.Context;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class SingletonTemplate {

    private static SingletonTemplate instance;
    private Context mContext;

    private SingletonTemplate() {
        mContext = ContextBuilder.getInstance().getContext();
    }

    /**
     * Traditional of Singleton
     **/

    public static SingletonTemplate getInstance() {
        if (instance == null)
            instance = new SingletonTemplate();
        return instance;
    }

}

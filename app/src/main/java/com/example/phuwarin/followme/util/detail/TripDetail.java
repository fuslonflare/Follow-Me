package com.example.phuwarin.followme.util.detail;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class TripDetail {

    private static TripDetail instance;
    private Context mContext;


    /**
     * Traditional of Singleton ;)
     **/
    private TripDetail() {
        mContext = Contextor.getInstance().getContext();
    }

    public static TripDetail getInstance() {
        if (instance == null) {
            instance = new TripDetail();
        }
        return instance;
    }
}

package com.example.phuwarin.followme.util.detail;

import android.content.Context;

import com.inthecheesefactory.thecheeselibrary.manager.Contextor;

import java.util.Map;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class TripDetail {

    private static TripDetail instance;
    private Context mContext;

    private String tripId;
    private Map<String, String> listMember;

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

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public Map<String, String> getListMember() {
        return listMember;
    }

    public void setListMember(Map<String, String> listMember) {
        this.listMember = listMember;
    }
}

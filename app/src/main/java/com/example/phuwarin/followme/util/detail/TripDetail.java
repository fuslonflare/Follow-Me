package com.example.phuwarin.followme.util.detail;

import android.content.Context;

import com.example.phuwarin.followme.manager.ContextBuilder;

import java.util.List;

/**
 * Created by nuuneoi on 11/16/2014.
 */
public class TripDetail {

    private static TripDetail instance;
    private Context mContext;

    private String tripId;
    private BicycleRoute tripRoute;
    private List<String> listMember;

    /**
     * Traditional of Singleton ;)
     **/
    private TripDetail() {
        mContext = ContextBuilder.getInstance().getContext();
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

    public BicycleRoute getTripRoute() {
        return tripRoute;
    }

    public void setTripRoute(BicycleRoute tripRoute) {
        this.tripRoute = tripRoute;
    }

    public List<String> getListMember() {
        return listMember;
    }

    public void setListMember(List<String> listMember) {
        this.listMember = listMember;
    }
}

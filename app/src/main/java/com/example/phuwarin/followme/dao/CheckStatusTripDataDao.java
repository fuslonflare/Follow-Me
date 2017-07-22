
package com.example.phuwarin.followme.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckStatusTripDataDao {

    @SerializedName("trip_id")
    @Expose
    private String tripId;
    @SerializedName("is_finish")
    @Expose
    private int isFinish;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public boolean getIsFinish() {
        return isFinish != 0;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

}

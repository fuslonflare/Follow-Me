
package com.example.phuwarin.followme.dao.trip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinTripDataDao {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("trip_id")
    @Expose
    private String tripId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

}

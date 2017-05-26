package com.example.phuwarin.followme.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuwarin on 4/14/2017.
 */

public class JoinTripDao implements Parcelable {
    @SerializedName("user_id") private String userId;
    @SerializedName("trip_id") private String tripId;

    public JoinTripDao() {
    }

    protected JoinTripDao(Parcel in) {
        userId = in.readString();
        tripId = in.readString();
    }

    public static final Creator<JoinTripDao> CREATOR = new Creator<JoinTripDao>() {
        @Override
        public JoinTripDao createFromParcel(Parcel in) {
            return new JoinTripDao(in);
        }

        @Override
        public JoinTripDao[] newArray(int size) {
            return new JoinTripDao[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(tripId);
    }
}

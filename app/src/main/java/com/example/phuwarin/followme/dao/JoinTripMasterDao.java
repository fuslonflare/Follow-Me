package com.example.phuwarin.followme.dao;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Phuwarin on 4/14/2017.
 */

public class JoinTripMasterDao implements Parcelable {
    @SerializedName("result") private String result;
    @SerializedName("data")   private List<JoinTripDao> data;

    public JoinTripMasterDao() {
    }

    protected JoinTripMasterDao(Parcel in) {
        result = in.readString();
        data = in.createTypedArrayList(JoinTripDao.CREATOR);
    }

    public static final Creator<JoinTripMasterDao> CREATOR = new Creator<JoinTripMasterDao>() {
        @Override
        public JoinTripMasterDao createFromParcel(Parcel in) {
            return new JoinTripMasterDao(in);
        }

        @Override
        public JoinTripMasterDao[] newArray(int size) {
            return new JoinTripMasterDao[size];
        }
    };

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<JoinTripDao> getData() {
        return data;
    }

    public void setData(List<JoinTripDao> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(result);
        parcel.writeTypedList(data);
    }
}

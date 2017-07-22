
package com.example.phuwarin.followme.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckStatusTripDao {

    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("data")
    @Expose
    private CheckStatusTripDataDao data;

    public boolean isIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public CheckStatusTripDataDao getData() {
        return data;
    }

    public void setData(CheckStatusTripDataDao data) {
        this.data = data;
    }

}

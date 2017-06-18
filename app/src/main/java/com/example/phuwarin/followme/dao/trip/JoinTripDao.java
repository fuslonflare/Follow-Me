
package com.example.phuwarin.followme.dao.trip;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JoinTripDao {

    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("data")
    @Expose
    private JoinTripDataDao joinTripDataDao;

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

    public JoinTripDataDao getJoinTripDataDao() {
        return joinTripDataDao;
    }

    public void setJoinTripDataDao(JoinTripDataDao joinTripDataDao) {
        this.joinTripDataDao = joinTripDataDao;
    }

}

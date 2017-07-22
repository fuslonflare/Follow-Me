
package com.example.phuwarin.followme.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendLocationDao {

    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("data")
    @Expose
    private List<FriendLocationDataDao> data = null;

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

    public List<FriendLocationDataDao> getData() {
        return data;
    }

    public void setData(List<FriendLocationDataDao> data) {
        this.data = data;
    }

}

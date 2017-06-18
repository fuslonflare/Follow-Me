
package com.example.phuwarin.followme.dao.position;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PositionDao {

    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("data")
    @Expose
    private List<PositionDataDao> data;

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

    public List<PositionDataDao> getData() {
        return data;
    }

    public void setData(List<PositionDataDao> data) {
        this.data = data;
    }

}

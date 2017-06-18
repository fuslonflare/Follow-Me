
package com.example.phuwarin.followme.dao.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteDao {

    @SerializedName("is_success")
    @Expose
    private boolean isSuccess;
    @SerializedName("error_code")
    @Expose
    private int errorCode;
    @SerializedName("data")
    @Expose
    private RouteDataDao routeDataDao;

    public boolean getIsSuccess() {
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

    public RouteDataDao getRouteDataDao() {
        return routeDataDao;
    }

    public void setRouteDataDao(RouteDataDao routeDataDao) {
        this.routeDataDao = routeDataDao;
    }

}

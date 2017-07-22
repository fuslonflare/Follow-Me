package com.example.phuwarin.followme.dao.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Phuwarin on 7/22/2017.
 */

public class RouteWaypointsDao {
    @SerializedName("name_en")
    @Expose
    private String nameEn;
    @SerializedName("name_th")
    @Expose
    private String nameTh;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;
    @SerializedName("order")
    @Expose
    private int order;

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameTh() {
        return nameTh;
    }

    public void setNameTh(String nameTh) {
        this.nameTh = nameTh;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}

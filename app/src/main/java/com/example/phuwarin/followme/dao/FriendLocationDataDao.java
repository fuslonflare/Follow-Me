
package com.example.phuwarin.followme.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FriendLocationDataDao {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("position")
    @Expose
    private String position;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
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

}

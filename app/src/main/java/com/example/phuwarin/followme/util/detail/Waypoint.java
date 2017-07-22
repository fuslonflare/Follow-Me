package com.example.phuwarin.followme.util.detail;

/**
 * Created by Phuwarin on 7/22/2017.
 */

public class Waypoint {
    private String nameEn;
    private String nameTh;
    private double lat;
    private double lng;
    private int order;

    public Waypoint() {
    }

    public Waypoint(String nameEn,
                    String nameTh,
                    double lat,
                    double lng,
                    int order) {
        this.nameEn = nameEn;
        this.nameTh = nameTh;
        this.lat = lat;
        this.lng = lng;
        this.order = order;
    }

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

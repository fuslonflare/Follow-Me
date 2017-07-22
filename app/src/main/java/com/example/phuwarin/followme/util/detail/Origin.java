package com.example.phuwarin.followme.util.detail;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Phuwarin on 6/25/2017.
 */

public class Origin {
    private static Origin instance;

    private String originId;
    private String originNameEn;
    private String originNameTh;
    private LatLng originLocation;

    private Origin() {

    }

    public static Origin getInstance() {
        if (instance == null) {
            instance = new Origin();
        }
        return instance;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getOriginNameEn() {
        return originNameEn;
    }

    public void setOriginNameEn(String originNameEn) {
        this.originNameEn = originNameEn;
    }

    public String getOriginNameTh() {
        return originNameTh;
    }

    public void setOriginNameTh(String originNameTh) {
        this.originNameTh = originNameTh;
    }

    public LatLng getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(LatLng originLocation) {
        this.originLocation = originLocation;
    }

    public void setOrigin(String id, String nameEn, String nameTh,
                          double lat, double lng) {
        this.originId = id;
        this.originNameEn = nameEn;
        this.originNameTh = nameTh;
        this.originLocation = new LatLng(lat, lng);
    }
}

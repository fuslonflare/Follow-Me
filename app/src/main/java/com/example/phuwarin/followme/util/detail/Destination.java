package com.example.phuwarin.followme.util.detail;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Phuwarin on 6/25/2017.
 */

public class Destination {
    private static Destination instance;

    private String destinationId;
    private String destinationNameEn;
    private String destinationNameTh;
    private LatLng destinationLocation;

    private Destination() {
    }

    public static Destination getInstance() {
        if (instance == null) {
            instance = new Destination();
        }
        return instance;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationNameEn() {
        return destinationNameEn;
    }

    public void setDestinationNameEn(String destinationNameEn) {
        this.destinationNameEn = destinationNameEn;
    }

    public String getDestinationNameTh() {
        return destinationNameTh;
    }

    public void setDestinationNameTh(String destinationNameTh) {
        this.destinationNameTh = destinationNameTh;
    }

    public LatLng getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(LatLng destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public void setDestination(String id, String nameEn, String nameTh,
                               double lat, double lng) {
        this.destinationId = id;
        this.destinationNameEn = nameEn;
        this.destinationNameTh = nameTh;
        this.destinationLocation = new LatLng(lat, lng);
    }
}

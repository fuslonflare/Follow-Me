
package com.example.phuwarin.followme.dao.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RouteDataDao {

    @SerializedName("route_id")
    @Expose
    private String routeId;
    @SerializedName("route_path")
    @Expose
    private String routePath;
    @SerializedName("trip_origin")
    @Expose
    private TripOriginDao tripOriginDao;
    @SerializedName("trip_destination")
    @Expose
    private TripDestinationDao tripDestinationDao;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRoutePath() {
        return routePath;
    }

    public void setRoutePath(String routePath) {
        this.routePath = routePath;
    }

    public TripOriginDao getTripOriginDao() {
        return tripOriginDao;
    }

    public void setTripOriginDao(TripOriginDao tripOriginDao) {
        this.tripOriginDao = tripOriginDao;
    }

    public TripDestinationDao getTripDestinationDao() {
        return tripDestinationDao;
    }

    public void setTripDestinationDao(TripDestinationDao tripDestinationDao) {
        this.tripDestinationDao = tripDestinationDao;
    }

}

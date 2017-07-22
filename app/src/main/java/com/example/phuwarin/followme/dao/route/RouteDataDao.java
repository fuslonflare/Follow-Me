
package com.example.phuwarin.followme.dao.route;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RouteDataDao {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("origin")
    @Expose
    private RouteOriginDao origin;
    @SerializedName("destination")
    @Expose
    private RouteDestinationDao destination;
    @SerializedName("waypoints")
    @Expose
    private List<RouteWaypointsDao> waypoints;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RouteOriginDao getOrigin() {
        return origin;
    }

    public void setOrigin(RouteOriginDao origin) {
        this.origin = origin;
    }

    public RouteDestinationDao getDestination() {
        return destination;
    }

    public void setDestination(RouteDestinationDao destination) {
        this.destination = destination;
    }

    public List<RouteWaypointsDao> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<RouteWaypointsDao> waypoints) {
        this.waypoints = waypoints;
    }
}

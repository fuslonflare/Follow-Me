package com.example.phuwarin.followme.util.detail;

import java.util.List;

/**
 * Created by Phuwarin on 6/25/2017.
 */

public class BicycleRoute {
    private static BicycleRoute instance;

    private String routeId;
    private String routePath;
    private Origin routeOrigin;
    private Destination routeDestination;
    private List<Waypoint> waypoints;

    private BicycleRoute() {
    }

    public static BicycleRoute getInstance() {
        if (instance == null) {
            instance = new BicycleRoute();
        }
        return instance;
    }

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

    public Origin getRouteOrigin() {
        return routeOrigin;
    }

    public void setRouteOrigin(Origin routeOrigin) {
        this.routeOrigin = routeOrigin;
    }

    public Destination getRouteDestination() {
        return routeDestination;
    }

    public void setRouteDestination(Destination routeDestination) {
        this.routeDestination = routeDestination;
    }

    public List<Waypoint> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(List<Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    public void setBicycleRoute(String id, String path,
                                Origin origin, Destination dest) {
        this.routeId = id;
        this.routePath = path;
        this.routeOrigin = origin;
        this.routeDestination = dest;
    }
}

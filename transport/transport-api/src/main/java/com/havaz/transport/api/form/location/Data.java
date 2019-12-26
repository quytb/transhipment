package com.havaz.transport.api.form.location;

import java.util.List;

public class Data{
    private List<PointLocation> points;
    private Waypoints waypoints;
    private Total total;

    public List<PointLocation> getPoints() {
        return points;
    }

    public void setPoints(List<PointLocation> points) {
        this.points = points;
    }

    public Waypoints getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(Waypoints waypoints) {
        this.waypoints = waypoints;
    }

    public Total getTotal() {
        return total;
    }

    public void setTotal(Total total) {
        this.total = total;
    }
}

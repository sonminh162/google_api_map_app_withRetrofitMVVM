package com.lifetime.google_map_api_mvvm_retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Route {

    @SerializedName("waypoint")
    @Expose
    private List<Waypoint> waypoint = null;
    @SerializedName("mode")
    @Expose
    private Mode mode;
    @SerializedName("leg")
    @Expose
    private List<Leg> leg = null;
    @SerializedName("summary")
    @Expose
    private Summary summary;

    public List<Waypoint> getWaypoint() {
        return waypoint;
    }

    public void setWaypoint(List<Waypoint> waypoint) {
        this.waypoint = waypoint;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public List<Leg> getLeg() {
        return leg;
    }

    public void setLeg(List<Leg> leg) {
        this.leg = leg;
    }

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

}
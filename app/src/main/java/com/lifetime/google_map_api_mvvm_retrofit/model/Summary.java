package com.lifetime.google_map_api_mvvm_retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Summary {

    @SerializedName("distance")
    @Expose
    private Integer distance;
    @SerializedName("trafficTime")
    @Expose
    private Integer trafficTime;
    @SerializedName("baseTime")
    @Expose
    private Integer baseTime;
    @SerializedName("flags")
    @Expose
    private List<String> flags = null;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("travelTime")
    @Expose
    private Integer travelTime;
    @SerializedName("_type")
    @Expose
    private String type;

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getTrafficTime() {
        return trafficTime;
    }

    public void setTrafficTime(Integer trafficTime) {
        this.trafficTime = trafficTime;
    }

    public Integer getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(Integer baseTime) {
        this.baseTime = baseTime;
    }

    public List<String> getFlags() {
        return flags;
    }

    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(Integer travelTime) {
        this.travelTime = travelTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
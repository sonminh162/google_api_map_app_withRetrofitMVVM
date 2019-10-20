package com.lifetime.google_map_api_mvvm_retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Mode {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("transportModes")
    @Expose
    private List<String> transportModes = null;
    @SerializedName("trafficMode")
    @Expose
    private String trafficMode;
    @SerializedName("feature")
    @Expose
    private List<Object> feature = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getTransportModes() {
        return transportModes;
    }

    public void setTransportModes(List<String> transportModes) {
        this.transportModes = transportModes;
    }

    public String getTrafficMode() {
        return trafficMode;
    }

    public void setTrafficMode(String trafficMode) {
        this.trafficMode = trafficMode;
    }

    public List<Object> getFeature() {
        return feature;
    }

    public void setFeature(List<Object> feature) {
        this.feature = feature;
    }

}
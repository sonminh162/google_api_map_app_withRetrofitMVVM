package com.lifetime.google_map_api_mvvm_retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MetaInfo {

    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("mapVersion")
    @Expose
    private String mapVersion;
    @SerializedName("moduleVersion")
    @Expose
    private String moduleVersion;
    @SerializedName("interfaceVersion")
    @Expose
    private String interfaceVersion;
    @SerializedName("availableMapVersion")
    @Expose
    private List<String> availableMapVersion = null;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMapVersion() {
        return mapVersion;
    }

    public void setMapVersion(String mapVersion) {
        this.mapVersion = mapVersion;
    }

    public String getModuleVersion() {
        return moduleVersion;
    }

    public void setModuleVersion(String moduleVersion) {
        this.moduleVersion = moduleVersion;
    }

    public String getInterfaceVersion() {
        return interfaceVersion;
    }

    public void setInterfaceVersion(String interfaceVersion) {
        this.interfaceVersion = interfaceVersion;
    }

    public List<String> getAvailableMapVersion() {
        return availableMapVersion;
    }

    public void setAvailableMapVersion(List<String> availableMapVersion) {
        this.availableMapVersion = availableMapVersion;
    }

}

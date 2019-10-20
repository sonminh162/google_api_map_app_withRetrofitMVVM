package com.lifetime.google_map_api_mvvm_retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResultForAll {

    @SerializedName("response")
    @Expose
    private Response response;

    public ResultForAll(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}

package com.lifetime.google_map_api_mvvm_retrofit.retrofit;

import com.lifetime.google_map_api_mvvm_retrofit.model.ResultForAll;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    String test = "hello";

    @GET("calculateroute.json")
    Call<ResultForAll> getResultDataFromStartPointEndPoint(
            @Query("app_id") String app_id,
            @Query("app_code") String app_code,
            @Query("mode") String mode,
            @Query("departure") String departure,
            @Query("waypoint0") String waypoint0,
            @Query("waypoint1") String waypoint1
    );
}

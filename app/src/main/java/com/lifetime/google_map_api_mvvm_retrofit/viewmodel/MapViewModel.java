package com.lifetime.google_map_api_mvvm_retrofit.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.lifetime.google_map_api_mvvm_retrofit.model.Leg;
import com.lifetime.google_map_api_mvvm_retrofit.model.Maneuver;
import com.lifetime.google_map_api_mvvm_retrofit.model.ResultForAll;
import com.lifetime.google_map_api_mvvm_retrofit.model.Route;
import com.lifetime.google_map_api_mvvm_retrofit.retrofit.GetDataService;
import com.lifetime.google_map_api_mvvm_retrofit.retrofit.RetrofitClientInstance;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapViewModel extends ViewModel {

    private GetDataService getDataService;

    public MutableLiveData<ArrayList> directionRequest = new MutableLiveData<>();

    public void init(){
        getDataService = RetrofitClientInstance.createService(GetDataService.class);
    }

    public void createDirectionResult(double latStart, double lonStart, double latEnd, double lonEnd){
        Call<ResultForAll> call = getDataService.getResultDataFromStartPointEndPoint("IHvLTkkCTu4oUixgJ4gR","E_CvlpVwmJtn3uVlJvqPlg","fastest;car;traffic:enabled","now",latStart + "," + lonStart,latEnd + "," + lonEnd);
        call.enqueue(new Callback<ResultForAll>() {
            @Override
            public void onResponse(Call<ResultForAll> call, Response<ResultForAll> response) {
                List<Route> routes = response.body().getResponse().getRoute();
                ArrayList points = null;

                for(int i = 0; i < routes.size(); i++){
                    List<Leg> legs = routes.get(i).getLeg();

                    for(int j = 0; j < legs.size(); j++){
                        List<Maneuver> maneuvers = legs.get(j).getManeuver();
                        points = new ArrayList();

                        for(int k = 0; k< maneuvers.size(); k++){
                            double lat = maneuvers.get(k).getPosition().getLatitude();
                            double lon = maneuvers.get(k).getPosition().getLongitude();

                            points.add(new LatLng(lat,lon));
                        }
                    }
                }

                directionRequest.setValue(points);
            }

            @Override
            public void onFailure(Call<ResultForAll> call, Throwable t) {

            }
        });
    }

    public void createReverseDirectionResult(double latStart, double lonStart, double latEnd, double lonEnd){
        Call<ResultForAll> call = getDataService.getResultDataFromStartPointEndPoint("IHvLTkkCTu4oUixgJ4gR","E_CvlpVwmJtn3uVlJvqPlg","fastest;car;traffic:enabled","now",latEnd + "," + lonEnd,latStart + "," + lonStart);
        call.enqueue(new Callback<ResultForAll>() {
            @Override
            public void onResponse(Call<ResultForAll> call, Response<ResultForAll> response) {
                List<Route> routes = response.body().getResponse().getRoute();
                ArrayList points = null;
                PolylineOptions polylineOptions = null;

                for(int i = 0; i < routes.size(); i++){
                    List<Leg> legs = routes.get(i).getLeg();
                    polylineOptions = new PolylineOptions();

                    for(int j = 0; j < legs.size(); j++){
                        List<Maneuver> maneuvers = legs.get(j).getManeuver();
                        points = new ArrayList();

                        for(int k = 0; k< maneuvers.size(); k++){
                            double lat = maneuvers.get(k).getPosition().getLatitude();
                            double lon = maneuvers.get(k).getPosition().getLongitude();

                            points.add(new LatLng(lat,lon));
                        }
                    }
                }

                directionRequest.setValue(points);
            }

            @Override
            public void onFailure(Call<ResultForAll> call, Throwable t) {

            }
        });
    }
}

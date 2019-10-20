package com.lifetime.google_map_api_mvvm_retrofit.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.lifetime.google_map_api_mvvm_retrofit.R;
import com.lifetime.google_map_api_mvvm_retrofit.utils.Utils;
import com.lifetime.google_map_api_mvvm_retrofit.viewmodel.MapViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapViewModel mapViewModel;

    private static final int LOCATION_REQUEST = 500;

    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1;

    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;

    private LocationManager locationManager;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Location mLastKnownLocation;

    private GoogleMap mMap;

    private EditText mSearchText;

    List<LatLng> locations = new ArrayList<>();

    Geocoder geocoder;

    private EditText startPoint;

    List<LatLng> listPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.init();

        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mSearchText = findViewById(R.id.input_search);

        startPoint = findViewById(R.id.input_start_point);

        listPoints = new ArrayList<>();

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MINIMUM_TIME_BETWEEN_UPDATES,
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        geocoder = new Geocoder(MainActivity.this);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }

        mapViewModel.directionRequest.observe(this, new Observer<ArrayList>() {
            @Override
            public void onChanged(ArrayList arrayList) {
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.addAll(arrayList);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
                mMap.addPolyline(polylineOptions);
            }
        });

        getDeviceLocationAndMoveCamera();

        SetUpUIAddSearchFeature();

    }

    private void SetUpUIAddSearchFeature() {
        findViewById(R.id.border_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locations.clear();
                mMap.clear();
            }
        });

        findViewById(R.id.border).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocationAndMoveCamera();
            }
        });

        findViewById(R.id.direction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDirectionResult(false);
            }
        });

        findViewById(R.id.reverse).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDirectionResult(true);
            }
        });

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                    showSearchLocationResult();
                }
                return false;
            }
        });

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.clear();
                updateCurrentLocation();
                List<Address> resultAddresses = null;
                for (LatLng latLngLeuLeu : locations) {
                    try {
                        resultAddresses = geocoder.getFromLocation(latLngLeuLeu.latitude, latLngLeuLeu.longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMap.addMarker(new MarkerOptions()
                            .position(latLngLeuLeu)
                            .title(resultAddresses.get(0).getAddressLine(0))
                    );
                }
                showCurrentPlaceInformation(latLng);
            }
        });
    }

    private void showCurrentPlaceInformation(LatLng latLng) {
        List<Address> resultAddresses = null;
        try {
            resultAddresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.title(resultAddresses.get(0).getAddressLine(0));
        mMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 13);
        mMap.animateCamera(cameraUpdate);
    }

    private void getDeviceLocationAndMoveCamera() {
        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastKnownLocation = task.getResult();
                    LatLng target = new LatLng(mLastKnownLocation.getLatitude()
                            , mLastKnownLocation.getLongitude());
                    MarkerOptions mark = new MarkerOptions()
                            .title("Current Position.")
                            .position(target)
                            .icon(BitmapDescriptorFactory.fromBitmap(Utils.createMarker(MainActivity.this, R.drawable.watashi, "Watashi")));
                    mMap.addMarker(mark);
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 13);
                    mMap.animateCamera(cameraUpdate);
                } else {
                    Toast.makeText(MainActivity.this, "problem here", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateCurrentLocation() {
        Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
        locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    mLastKnownLocation = task.getResult();
                    LatLng target = new LatLng(mLastKnownLocation.getLatitude()
                            , mLastKnownLocation.getLongitude());
                    MarkerOptions mark = new MarkerOptions()
                            .title("Current Position.")
                            .position(target)
                            .icon(BitmapDescriptorFactory.fromBitmap(Utils.createMarker(MainActivity.this, R.drawable.watashi, "Watashi")));
                    mMap.addMarker(mark);
                } else {
                    Toast.makeText(MainActivity.this, "problem here", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDirectionResult(boolean reverse) {
        mMap.clear();

        EditText edtStart = findViewById(R.id.input_start_point);
        EditText edtEnd = findViewById(R.id.input_search);

        final String sStartPoint = edtStart.getText().toString();
        final String sEndPoint = edtEnd.getText().toString();

        if (sStartPoint.isEmpty()) {
            edtStart.setError("required");
            edtStart.requestFocus();
            return;
        }

        if (sEndPoint.isEmpty()) {
            edtEnd.setError("required");
            edtEnd.requestFocus();
            return;
        }

        List<Address> addressesStart = null;
        List<Address> addressesEnd = null;
        try {
            addressesStart = geocoder.getFromLocationName(edtStart.getText().toString(), 1);
            addressesEnd = geocoder.getFromLocationName(edtEnd.getText().toString(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addressesStart.size() > 0 && addressesEnd.size() > 0) {
            double latStart = addressesStart.get(0).getLatitude();
            double lonStart = addressesStart.get(0).getLongitude();
            LatLng startPoint = new LatLng(latStart, lonStart);

            double latEnd = addressesEnd.get(0).getLatitude();
            double lonEnd = addressesEnd.get(0).getLongitude();
            LatLng endPoint = new LatLng(latEnd, lonEnd);

            if (!reverse) {
                mapViewModel.createDirectionResult(latStart, lonStart, latEnd, lonEnd);
            } else {
                mapViewModel.createReverseDirectionResult(latStart, lonStart, latEnd, lonEnd);
            }

            mMap.addMarker(new MarkerOptions()
                    .position(startPoint)
                    .title(addressesStart.get(0).getAddressLine(0)));

            mMap.addMarker(new MarkerOptions()
                    .position(endPoint)
                    .title(addressesEnd.get(0).getAddressLine(0)));

            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(startPoint)
                    .include(endPoint)
                    .build();
            int padding = 200;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);

        }
    }

    private void showSearchLocationResult() {
        EditText etEndereco = findViewById(R.id.input_search);

        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocationName(etEndereco.getText().toString(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses.size() > 0) {
            double latitude = addresses.get(0).getLatitude();
            double longitude = addresses.get(0).getLongitude();
            locations.add(new LatLng(latitude, longitude));
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng latLng : locations) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(addresses.get(0).getAddressLine(0)));
                builder.include(latLng);
            }

            LatLngBounds bounds = builder.build();
            int padding = 200;
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            mMap.animateCamera(cu);
        }
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            LatLng target = new LatLng(location.getLatitude()
                    , location.getLongitude());
            MarkerOptions mark = new MarkerOptions()
                    .title("Current Position.")
                    .position(target)
                    .icon(BitmapDescriptorFactory.fromBitmap(Utils.createMarker(MainActivity.this, R.drawable.watashi, "Watashi")));
            mMap.addMarker(mark);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(target, 13);
            mMap.animateCamera(cameraUpdate);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}

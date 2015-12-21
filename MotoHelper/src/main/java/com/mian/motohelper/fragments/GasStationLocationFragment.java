package com.mian.motohelper.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mian.motohelper.R;

/**
 * "加油站位置"頁面的Fragment
 */
public class GasStationLocationFragment extends Fragment {
    private Context context;
    private MapView mapView;
    private GoogleMap googleMap;

    private final String LM_GPS = LocationManager.GPS_PROVIDER;
    private final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude;//經度
    private double longitude;//緯度

    public GasStationLocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();


        View rootView = inflater.inflate(R.layout.fragment_gas_station_location, container, false);

        mapView = (MapView) rootView.findViewById(R.id.google_maps);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(context);
        googleMap = mapView.getMap();

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onResume() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new MyLocationListener();
        }
        locationManager.requestLocationUpdates(LM_GPS, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LM_NETWORK, 0, 0, locationListener);
        openGPS(context);
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }

        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
            Toast.makeText(context, "位置資訊已改變", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void openGPS(Context context) {
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        String gpsStatue = gps ? "啟用" : "未啟用";
        String networkStatue = network ? "啟用" : "未啟用";
        Toast.makeText(context, "GPS:" + gpsStatue + ",NETWORK:" + networkStatue, Toast.LENGTH_SHORT).show();

        if (gps || network) {
            return;
        } else {
            Intent gpsOptionIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionIntent);

        }
    }
}

package com.mian.motohelper.fragments;


import android.app.Fragment;
import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
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
        super.onResume();


        mapView.onResume();

    }

    @Override
    public void onPause() {
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


}

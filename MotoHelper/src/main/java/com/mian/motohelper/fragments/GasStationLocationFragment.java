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
    private Context context;//使用此Fragment的Activity
    private MapView mapView;//顯示地圖的View
    private GoogleMap googleMap;

    private final String LM_GPS = LocationManager.GPS_PROVIDER;//GPS定位提供者
    private final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;//NETWORK定位提供者

    private LocationManager locationManager;//定位管理器
    private LocationListener locationListener;//定位監聽器

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
        //載入此Fragment的畫面Layout
        View rootView = inflater.inflate(R.layout.fragment_gas_station_location, container, false);
        mapView = (MapView) rootView.findViewById(R.id.google_maps);//取得元件物件

        return rootView;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();//取得Activity

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(context);//初始化GoogleAPI
        googleMap = mapView.getMap();//取得GoogleMap物件
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

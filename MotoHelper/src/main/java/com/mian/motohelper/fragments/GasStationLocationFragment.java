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

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);//取得系統的定位服務
        locationListener = new MyLocationListener();//建立監聽物件

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        MapsInitializer.initialize(context);//初始化GoogleAPI
        googleMap = mapView.getMap();//取得GoogleMap物件
    }


    @Override
    public void onResume() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            locationListener = new MyLocationListener();
        }
        locationManager.requestLocationUpdates(LM_GPS, 0, 0, locationListener);//註冊GPS定位更新監聽事件
        locationManager.requestLocationUpdates(LM_NETWORK, 0, 0, locationListener);//註冊NETWORK定位監聽事件
        openGPS(context);
        super.onResume();

        mapView.onResume();

    }

    @Override
    public void onPause() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);//移除定位監聽
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

    /**
     * 實作定位監聽事件的類別
     */
    private class MyLocationListener implements LocationListener {
        /**
         * 位置資訊改變時呼叫
         *
         * @param location 位置改變後的資訊
         */
        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)));
            Toast.makeText(context, "位置資訊已改變", Toast.LENGTH_SHORT).show();
        }

        /**
         * 網路狀態改變時呼叫
         *
         * @param provider
         * @param status
         * @param extras
         */
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

    /**
     * 判斷GPS或網路基地台定位是否啟用，若無啟用則自動轉跳至GPS設定畫面
     *
     * @param context
     */
    private void openGPS(Context context) {
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);//GPS是否啟用
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//網路基地台定位是否啟用

        String gpsStatue = gps ? "啟用" : "未啟用";
        String networkStatue = network ? "啟用" : "未啟用";
        Toast.makeText(context, "GPS:" + gpsStatue + ",NETWORK:" + networkStatue, Toast.LENGTH_SHORT).show();

        if (gps || network) {
            return;
        } else {
            //轉跳至GPS設定畫面
            Intent gpsOptionIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionIntent);

        }
    }
}

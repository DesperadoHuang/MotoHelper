package com.mian.motohelper.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
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
    private UiSettings uiSettings;//地圖使用者介面設定物件
    private final String LM_GPS = LocationManager.GPS_PROVIDER;//GPS定位提供者
    private final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;//NETWORK定位提供者
    private LocationManager locationManager;
    private Location lastPosition;//最後已知位置

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
        mapView.onCreate(savedInstanceState);
        context = getActivity();//取得Activity

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        initMap();

        initMapUiSettings();


        processControllers();
    }


    private void initMap() {
        MapsInitializer.initialize(context);//初始化GoogleAPI，確保 Map!=null
        googleMap = mapView.getMap();//取得GoogleMap物件
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);//設定地圖顯示類型為經典類型
        googleMap.setTrafficEnabled(true);//顯示交通流量資訊圖層
        googleMap.setMyLocationEnabled(true);//顯示目前位置圖層

    }

    private void initMapUiSettings() {
        uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);//顯示目前位置按鈕(左上方)，要開圖層一起開
        uiSettings.setZoomControlsEnabled(true);//開啟縮放控制鈕
    }

    private void processControllers() {
        //設定
      googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (lastPosition == null) {
                    Toast.makeText(context, "尚未取得位置資訊", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        moveToCurrentLocation();
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

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * 移動至目前位置(最後已知位置)
     */
    private void moveToCurrentLocation() {
        lastPosition = locationManager.getLastKnownLocation(LM_NETWORK);
        if (lastPosition != null) {
            LatLng currentLatLng = new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));
        } else {
            Toast.makeText(context, "尚未取得位置", Toast.LENGTH_SHORT).show();
        }
    }
}

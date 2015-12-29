package com.mian.motohelper.fragments;


import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mian.motohelper.R;
import com.tools.MyTools;

import java.util.Date;

/**
 * "加油站位置"頁面的Fragment
 */
public class GasStationLocationFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private Context context;//使用此Fragment的Activity
    private MapView mapView;//顯示地圖的View
    private GoogleMap googleMap;
    private UiSettings uiSettings;//地圖使用者介面設定物件
    private final String LM_GPS = LocationManager.GPS_PROVIDER;//GPS定位提供者
    private final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;//NETWORK定位提供者
    private LocationManager locationManager;
    private Location lastPosition;//最後已知位置
    private LocationListener locationListener;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;

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
        buildGoogleApiClient();
        initMap();
        initMapUiSettings();
        processControllers();
    }

    /**
     * 建立GoogleApiClient物件
     * 註冊GoogleApiClient連結/中斷的Callback
     * 註冊要連結的API
     */
    protected void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();//連結GoogleApiClient
    }

    @Override
    public void onResume() {
        if (locationManager == null) {
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }
        moveToCurrentLocation();
        super.onResume();
        mapView.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();//中斷GoogleApiClient連結
        super.onStop();
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

    /*************************************************************************
     * 這只是分隔線，以下實作ConnectionCallbacks, OnConnectionFailedListener介面 *
     *************************************************************************/

    @Override
    public void onConnected(Bundle bundle) {
        lastPosition = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);//取得最後位置
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        createLocationReuest();
        locationListener = new MyLoctionListener();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, locationListener);
    }

    /**
     * 建立請求位置的參數之物件
     */
    protected void createLocationReuest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);//最慢更新時間
        locationRequest.setFastestInterval(1000);//最快更新時間
        locationRequest.setSmallestDisplacement(1);//最短更新距離
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);//精準度
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    /*************************************************************
     * 這只是分隔線
     **************************************************************/
    private class MyLoctionListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            lastPosition = location;
            MyTools.myLog("緯度-latitude:" + location.getLatitude() + "\n" +
                    "經度-Longitude:" + location.getLongitude() + "\n" +
                    "精準度- Accuracy:" + location.getAccuracy() + "\n" +
                    "標高-Altitude:" + location.getAltitude() + "\n" +
                    "時間-Time:" + new Date(location.getTime()) + "\n" +
                    "速度-Speed:" + location.getSpeed() + "\n" +
                    "方位-Bearing:" + location.getBearing() + "\n");
            Toast.makeText(context, "緯度-latitude:" + location.getLatitude() + "\n" +
                    "經度-Longitude:" + location.getLongitude() + "\n" +
                    "精準度- Accuracy:" + location.getAccuracy() + "\n" +
                    "標高-Altitude:" + location.getAltitude() + "\n" +
                    "時間-Time:" + new Date(location.getTime()) + "\n" +
                    "速度-Speed:" + location.getSpeed() + "\n" +
                    "方位-Bearing:" + location.getBearing() + "\n", Toast.LENGTH_SHORT).show();
        }
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
        //設定點擊
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                openGPS();
                moveToCurrentLocation();
                return true;
            }
        });
    }

    /**
     * 移動至目前位置(最後已知位置)，或預設位置(台灣中心)
     */
    private void moveToCurrentLocation() {
        if (lastPosition != null) {
            LatLng currentLatLng = new LatLng(lastPosition.getLatitude(), lastPosition.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLatLng).zoom(14).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);
        } else {
            Toast.makeText(context, "正在取得位置", Toast.LENGTH_SHORT).show();
            LatLng defultLatLng = new LatLng(23.689563, 120.953900);//台灣中心
            CameraPosition cameraPosition = new CameraPosition.Builder().target(defultLatLng).zoom(7).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.animateCamera(cameraUpdate);
        }
    }

    /**
     * 判斷GPS是否開啟，若無開啟則轉跳至GPS設定畫面
     */
    private void openGPS() {
        boolean gps = locationManager.isProviderEnabled(LM_GPS);
        if (!(gps)) {
            Toast.makeText(context, "建議GPS定位功能\n取得更精確的位置", Toast.LENGTH_LONG).show();
            Intent gpsOptionSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionSetting);
        }
    }

}

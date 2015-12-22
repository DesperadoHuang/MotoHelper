package com.mian.googlemaps_demo_camera;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    private GoogleMap myMap;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        setUpMapIfNeeded();//取得GoogleMap物件

        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);//設定地圖類型
        myMap.setBuildingsEnabled(true);//設定地圖上是否顯示建築物，zoom >= 17才會顯示建築物
        myMap.setMyLocationEnabled(true);//顯示目前所在位置按鈕

        UiSettings uiSettings = myMap.getUiSettings();//取得GoogleMap使用者介面的功能設定物件
        uiSettings.setZoomControlsEnabled(true);//縮放按鈕
        uiSettings.setScrollGesturesEnabled(true);//捲動手勢
        uiSettings.setZoomGesturesEnabled(true);//縮放手勢
        uiSettings.setTiltGesturesEnabled(true);//傾斜手勢
        uiSettings.setRotateGesturesEnabled(true);//旋轉手勢


        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                String address = Helper.getAddressByLatLng(latLng);
                if (address == null) {
                    Toast.makeText(context, "Not found !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
                    playAnimateCamera(latLng, 3000);
                }
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem info1 = menu.add(0, 1, 0, "台北車站");
        info1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                LatLng latLng = new LatLng(25.047924, 121.517081);
                playAnimateCamera(latLng, 10000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playAnimateCamera(LatLng latLng, int i) {

    }


    /**
     * 取得GoogleMap物件
     */
    private void setUpMapIfNeeded() {
        if (myMap == null) {
            myMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            if (myMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
        myMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }
}

package com.mian.googlemaps_demo_camera;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
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

        //設定點擊地圖的某一點時的監聽介面
        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //根據latlng取得該經緯度所對應的地址或地標
                String address = Helper.getAddressByLatLng(latLng);
                if (address == null) {
                    Toast.makeText(context, "Not found !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, address, Toast.LENGTH_SHORT).show();
                    playAnimateCamera(latLng, 1000);
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
                playAnimateCamera(latLng, 1000);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 在地圖上顯示地圖相機鏡頭動畫效果
     *
     * @param latLng     包裝著座標的LatLng物件
     * @param durationMs 動畫持續的時間(毫秒)
     */
    private void playAnimateCamera(LatLng latLng, int durationMs) {
        //設定地圖相機鏡頭的位置參數
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)//座標的位置
                .zoom(17.0f)//縮放的大小，要 >= 17 才會顯示建築物
                .bearing(300)//旋轉的角度
                .tilt(67)//傾斜的角度
                .build();

        //取得CameraUpdate物件，定義地圖相機鏡頭的移動
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);

        //地圖相關鏡頭動畫行程設定
        myMap.animateCamera(cameraUpdate, durationMs, null);
    }


    /**
     * 取得GoogleMap物件，確保GoogleMap物件不為null
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

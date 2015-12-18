package com.example.locationgps_demo;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/**
 * 取得GPS定位的範例
 */
public class MainActivity extends AppCompatActivity {
    public final String LM_GPS = LocationManager.GPS_PROVIDER;//GPS定位提供者
    public final String LM_NETWORK = LocationManager.NETWORK_PROVIDER;//NETWORK定位提供者

    private Context context;
    private TextView textView1;

    private LocationManager myLocationManager;//定位管理器
    private LocationListener myLocationListener;//定位監聽器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        //取得LocationManager物件
        myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //建立定位監聽器物件
        myLocationListener = new MyLocationListener();
        textView1 = (TextView) findViewById(R.id.textview1);
        openGPS(context);

    }

    public void onClick(View v) {
        openGPS(context);
    }

    public void openGPS(Context context) {
        boolean gps = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Toast.makeText(context, "GPS:" + gps + ",Network:" + network, Toast.LENGTH_SHORT).show();
        if (gps || network) {
            return;
        } else {
            Intent gpsOptionIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionIntent);
        }
    }


    @Override
    protected void onResume() {
        if (myLocationManager == null) {
            myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            myLocationListener = new MyLocationListener();
        }

        myLocationManager.requestLocationUpdates(LM_GPS, 0, 0, myLocationListener);
        myLocationManager.requestLocationUpdates(LM_NETWORK, 0, 0, myLocationListener);
        setTitle("onResume...");

        super.onResume();
    }

    @Override
    protected void onPause() {
        if (myLocationManager != null) {
            myLocationManager.removeUpdates(myLocationListener);
            myLocationManager = null;
        }
        setTitle("onPause...");
        super.onPause();
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            textView1.setText("Location-GPS:" + "\n" +
                    "緯度-latitude:" + location.getLatitude() + "\n" +
                    "經度-Longitude:" + location.getLongitude() + "\n" +
                    "精準度- Accuracy:" + location.getAccuracy() + "\n" +
                    "標高-Altitude:" + location.getAltitude() + "\n" +
                    "時間-Time:" + new Date(location.getTime()) + "\n" +
                    "速度-Speed:" + location.getSpeed() + "\n" +
                    "方位-Bearing:" + location.getBearing() + "\n");
            setTitle("GPS位置已更新");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    setTitle("服務中");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    setTitle("沒有服務");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    setTitle("暫時不可使用");
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
}

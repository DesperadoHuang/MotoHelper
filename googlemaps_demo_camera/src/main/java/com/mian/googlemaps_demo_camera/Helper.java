package com.mian.googlemaps_demo_camera;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by WilsonHuang on 2015/12/22.
 */
public class Helper {
    public static String DOMAIN = "http://maps.googleapis.com/maps/api/geocode/json";


    public static String getAddressByLatLng(LatLng latLng) {
        String address = "";
        Helper.LatLngToAddress ga = new Helper.LatLngToAddress(latLng);
        FutureTask<String> futureTask = new FutureTask<String>(ga);
        new Thread(futureTask).start();
        try {
            address = futureTask.get();
        } catch (Exception ex) {

        }
        return address;
    }


    public static LatLng getLatLngByAddress(String address) {
        LatLng latLng = null;
        Helper.AddressToLatLng ga = new Helper.AddressToLatLng(address);
        FutureTask<LatLng> futureTask = new FutureTask<LatLng>(ga);
        new Thread(futureTask).start();
        try {
            latLng = futureTask.get();
        } catch (Exception ex) {

        }
        return latLng;
    }

    private static class HttpUtil {
        public static String get(String urlString) throws Exception {
            InputStream inputStream = null;
            Reader reader = null;
            StringBuilder str = new StringBuilder();
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("User-agent", "IE/6.0");
            inputStream = urlConnection.getInputStream();
            reader = new InputStreamReader(inputStream, "UTF-8");
            char[] buffer = new char[1];
            while (reader.read(buffer) != -1) {
                str.append(new String(buffer));
            }
            return str.toString();
        }
    }

    private static class LatLngToAddress implements Callable<String> {
        private String queryURLString = DOMAIN + "?latlng=%s,%s&sensor=true&language=zh_tw";
        private String address = null;

        private LatLng latLng;

        LatLngToAddress(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public String call() throws Exception {
            address = getAddressByLocation();
            return address;
        }

        private String getAddressByLocation() {
            String urlString = String.format(queryURLString, latLng.latitude, latLng.longitude);
            try {
                String jsonStr = HttpUtil.get(urlString);
                JSONArray results = new JSONObject(jsonStr).getJSONArray("results");
                if (results.length() >= 1) {
                    JSONObject jsonObject = results.getJSONObject(0);
                    address = jsonObject.optString("formatted_address");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return address;
        }
    }


    private static class AddressToLatLng implements Callable<LatLng> {
        private String queryURLString = DOMAIN + "?address=%s&sensor=false&language=zh_tw";
        private String address;

        AddressToLatLng(String address) {
            this.address = address;
        }

        @Override
        public LatLng call() {
            LatLng latLng = null;
            try {
                latLng = getLocationByAddress(URLEncoder.encode(address, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {

            }
            return latLng;
        }

        private LatLng getLocationByAddress(String address) {
            String urlString = String.format(queryURLString, address);
            LatLng latLng = null;
            try {
                String jsonStr = HttpUtil.get(urlString);
                JSONArray results = new JSONObject(jsonStr).getJSONArray("results");
                System.out.println("results.length():" + results.length());
                if (results.length() >= 1) {
                    JSONObject jsonObject = results.getJSONObject(0);
                    JSONObject laln = jsonObject.getJSONObject("geometry").getJSONObject("location");
                    latLng = new LatLng(Double.parseDouble(laln.getString("lat")), Double.parseDouble(laln.getString("lng")));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return latLng;
        }
    }
}

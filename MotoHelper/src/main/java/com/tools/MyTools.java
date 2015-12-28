package com.tools;

import android.location.Location;
import android.util.Log;

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
 * 工具類別
 * <p/>
 * Created by WilsonHuang on 2015/12/16.
 */
public class MyTools {

    /******************************************************************************************
     * 這只是分隔島 *                   以下為測試工具
     ******************************************************************************************/

    private static final String TAG = "debug";

    public static void myLog(String message) {
        Log.i(TAG, message);
    }


    /******************************************************************************************
     * 這只是分隔島 *                以下為地圖相關工具
     ******************************************************************************************/

    //傳回JSON資料格式的URL
    public static String DOMAIN = "http://maps.googleapis.com/maps/api/geocode/json";

    /**
     * 依照LatLng同步地取得Address
     *
     * @param latLng 所點選的座標
     * @return 該座標的地址
     */
    public static String getAddressByLatLng(LatLng latLng) {
        String address = "";
        MyTools.LatLngToAddress ga = new MyTools.LatLngToAddress(latLng);
        FutureTask<String> futureTask = new FutureTask<String>(ga);
        new Thread(futureTask).start();
        try {
            address = futureTask.get();
        } catch (Exception ex) {

        }
        return address;
    }


    /**
     * 依照Address同步地取得LatLng
     *
     * @param address 要查詢的地址
     * @return 該地址的座標
     */
    public static LatLng getLatLngByAddress(String address) {
        LatLng latLng = null;
        MyTools.AddressToLatLng ga = new MyTools.AddressToLatLng(address);
        FutureTask<LatLng> futureTask = new FutureTask<LatLng>(ga);
        new Thread(futureTask).start();
        try {
            latLng = futureTask.get();
        } catch (Exception ex) {

        }
        return latLng;
    }

    /**
     * 將URL所傳回的內容轉成String的類別
     */
    private static class HttpUtil {

        /**
         * 將URL所傳回的內容轉成String
         *
         * @param urlString
         * @return
         * @throws Exception
         */
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

    /**
     * LatLng 轉成 Address 的類別
     */
    private static class LatLngToAddress implements Callable<String> {
        private String queryURLString = DOMAIN + "?latlng=%s,%s&sensor=true&language=zh_tw";
        private String address = null;

        private LatLng latLng;

        LatLngToAddress(LatLng latLng) {
            this.latLng = latLng;
        }

        @Override
        public String call() throws Exception {
            //輸入經緯度得到地址
            address = getAddressByLocation();
            return address;
        }

        /**
         * 分析json取得Address
         *
         * @return Address
         */
        private String getAddressByLocation() {
            String urlString = String.format(queryURLString, latLng.latitude, latLng.longitude);
            try {
                //取得json string
                String jsonStr = HttpUtil.get(urlString);
                //取得 json 根陣列節點 results
                JSONArray results = new JSONObject(jsonStr).getJSONArray("results");
                if (results.length() >= 1) {
                    //取得 results[0] 元素
                    JSONObject jsonObject = results.getJSONObject(0);
                    //取得 formatted_address 屬性內容
                    address = jsonObject.optString("formatted_address");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return address;
        }
    }


    /**
     * Address 轉成 LatLng 的類別
     */
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
                //輸入地址得到經緯度(中文地址必須透過URLEncoder編碼)
                latLng = getLocationByAddress(URLEncoder.encode(address, "UTF-8"));
            } catch (UnsupportedEncodingException ex) {

            }
            return latLng;
        }

        /**
         * 分析json取得LatLng
         *
         * @param address
         * @return LatLng
         */
        private LatLng getLocationByAddress(String address) {
            String urlString = String.format(queryURLString, address);
            LatLng latLng = null;
            try {
                //取得 json String
                String jsonStr = HttpUtil.get(urlString);
                //取得 json 根陣列節點 results
                JSONArray results = new JSONObject(jsonStr).getJSONArray("results");
                System.out.println("results.length():" + results.length());
                if (results.length() >= 1) {
                    //取得 results[0] 元素
                    JSONObject jsonObject = results.getJSONObject(0);
                    //取得 geometry 節點的物件(Location)
                    JSONObject laln = jsonObject.getJSONObject("geometry").getJSONObject("location");

                    latLng = new LatLng(
                            Double.parseDouble(laln.getString("lat")),//取得lat屬性內容
                            Double.parseDouble(laln.getString("lng")) //取得lng屬性內容
                    );
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return latLng;
        }
    }

    /**
     * 使用Location.distanceBetween API 取得大圓距離(地球上兩個座標之間的距離)
     *
     * @param startLatLng 起點座標
     * @param endLatLng   終點座標
     * @return 地球上兩點之間的距離(公尺)
     */
    private static float getDistanceBetween(LatLng startLatLng, LatLng endLatLng) {
        float[] results = new float[1];
        double startLat = startLatLng.latitude;
        double startLng = startLatLng.longitude;
        double endLat = endLatLng.latitude;
        double endLng = endLatLng.longitude;
        Location.distanceBetween(startLat, startLng, endLat, endLng, results);
        return results[0];
    }


}

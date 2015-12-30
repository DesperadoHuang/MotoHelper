package com.mian.motohelper.Datebase;

/**
 * Created by WilsonHuang on 2015/12/28.
 */
public class GasStationInfo {
    private String type;
    private double latitude;
    private double longitude;
    private String address;
    private String phoneNumber;

    public GasStationInfo() {
    }

    public GasStationInfo(String type, double latitude, double longitude, String address, String phoneNumber) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

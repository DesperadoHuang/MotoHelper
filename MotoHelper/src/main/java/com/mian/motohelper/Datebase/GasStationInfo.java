package com.mian.motohelper.Datebase;

/**
 * Created by WilsonHuang on 2015/12/28.
 */
public class GasStationInfo {
    private String type;
    private String name;
    private String address;
    private String phoneNumber;
    private double latitude;
    private double longitude;

    public GasStationInfo(String type, String name, String address, String phoneNumber, double latitude, double longitude) {
        this.type = type;
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}

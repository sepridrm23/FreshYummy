package com.freshyummy.android;

/**
 * Created by afi on 30/03/18.
 */

public class Tracking {
    String latitude_user, longitude_user, latitude_driver, longitude_driver;
    Tracking(String latitude_user, String longitude_user, String latitude_driver, String longitude_driver){
        this.latitude_user = latitude_user;
        this.longitude_user = longitude_user;
        this.latitude_driver = latitude_driver;
        this.longitude_driver = longitude_driver;
    }

    public String getLatitude_user() {
        return latitude_user;
    }

    public void setLatitude_user(String latitude_user) {
        this.latitude_user = latitude_user;
    }

    public String getLongitude_user() {
        return longitude_user;
    }

    public void setLongitude_user(String longitude_user) {
        this.longitude_user = longitude_user;
    }

    public String getLatitude_driver() {
        return latitude_driver;
    }

    public void setLatitude_driver(String latitude_driver) {
        this.latitude_driver = latitude_driver;
    }

    public String getLongitude_driver() {
        return longitude_driver;
    }

    public void setLongitude_driver(String longitude_driver) {
        this.longitude_driver = longitude_driver;
    }
}

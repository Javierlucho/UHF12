package com.pda.uhf_g.entity;

public class GPSInfo {
    private final double latitude;
    private final double longitude;

    public GPSInfo(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

}

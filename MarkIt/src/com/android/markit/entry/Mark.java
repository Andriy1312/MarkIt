package com.android.markit.entry;

public class Mark {
    
    private double latitude;
    private double longitude;
    private long markTime;
    private int id = -1;;

    public Mark(double latitude, double longitude, long time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.markTime = time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    
    public long getTime() {
        return markTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
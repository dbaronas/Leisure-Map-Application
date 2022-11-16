package com.example.leisuremap;

public class Object {
    private String name;
    private double distance;
    private double lat;
    private double lon;

    public Object(String name, double distance, double lat, double lon)
    {
        this.name = name;
        this.distance = distance;
        this.lat = lat;
        this.lon = lon;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}

package com.ivim.ivim;
public class UTMPoint {
    private double x;
    private double y;
    private int zone;

    public UTMPoint(double x, double y, int zone) {
        this.x = x;
        this.y = y;
        this.zone = zone;
    }

    public UTMPoint() {

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String toString() {
        return "(" + x + ", " + y + " - "+ zone + ")";
    }

}
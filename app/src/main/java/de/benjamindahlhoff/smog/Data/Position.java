package de.benjamindahlhoff.smog.Data;

import de.benjamindahlhoff.smog.UI.MainActivity;


public class Position {
    private double mLatitude;
    private double mLongitude;

    public Position() {
        mLatitude = 50;
        mLatitude = 8;
    }

    public Position(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }
}

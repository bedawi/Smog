package de.benjamindahlhoff.smog.Data;

public class Position {
    private String mLatitude;
    private String mLongitude;

    public Position() {
        mLatitude = "50";
        mLatitude = "8";
    }

    public Position(String latitude, String longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }
}

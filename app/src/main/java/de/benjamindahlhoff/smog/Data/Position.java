package de.benjamindahlhoff.smog.Data;

/**
 * Store a position and calculate distances
 */

public class Position {
    private double mLatitude;
    private double mLongitude;

    /**
     * Constructor, start at position 0,0
     */
    public Position() {
        mLatitude = 0;
        mLatitude = 0;
    }

    /**
     * Set position
     * @param latitude      double value containing the latitude
     * @param longitude     double value containing the longitude
     */
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

    /**
     * Calculate distance from lat, long
     * @param latitude
     * @param longitude
     * @return in KM
     */
    public int distanceFromPosition(double latitude, double longitude) {
        // Throw a latitude and a longitude into this an get the distance to the current position
        // in Kilometers in return:
        return (int) Math.round(distance(mLatitude, mLongitude, latitude, longitude, "K"));
    }

    /**
     *  This Code is licensed under the LGPLv3-License from http://www.geodatasource.com/developers/java
     *  Units: M: Miles, K: Kilometers, N: Nautical Miles
     */
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /**
     * Helper method to convert from deg to rad
     * @param deg
     * @return
     */
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Helper method to convert from rad to deg
     * @param rad
     * @return
     */
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}

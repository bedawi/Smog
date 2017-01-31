package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 31.01.17.
 * This class holds data for each individual Measurement Station of the Stuttgart Air Pollution
 * Project Luftdaten.info
 *
 * The name of the class comes from the German word for Particulates in the air "Feinstaub".
 */

public class FeinstaubStation {
    private int mLocationId;
    private String mTimestamp;
    private double mHumidity;
    private double mTemperature;
    private Position mPosition;
    private double mP1Value;
    private double mP2Value;
    private int mMaxMicro;
    private int mMinMicro;
    private int mSamples;
    private int mDistance;

    public int getLocationId() {
        return mLocationId;
    }

    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    public double getHumidity() {
        return mHumidity;
    }

    public void setHumidity(double humidity) {
        mHumidity = humidity;
    }

    public double getTemperature() {
        return mTemperature;
    }

    public void setTemperature(double temperature) {
        mTemperature = temperature;
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public double getP1Value() {
        return mP1Value;
    }

    public void setP1Value(double p1Value) {
        mP1Value = p1Value;
    }

    public double getP2Value() {
        return mP2Value;
    }

    public void setP2Value(double p2Value) {
        mP2Value = p2Value;
    }

    public int getMaxMicro() {
        return mMaxMicro;
    }

    public void setMaxMicro(int maxMicro) {
        mMaxMicro = maxMicro;
    }

    public int getMinMicro() {
        return mMinMicro;
    }

    public void setMinMicro(int minMicro) {
        mMinMicro = minMicro;
    }

    public int getSamples() {
        return mSamples;
    }

    public void setSamples(int samples) {
        mSamples = samples;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }
}

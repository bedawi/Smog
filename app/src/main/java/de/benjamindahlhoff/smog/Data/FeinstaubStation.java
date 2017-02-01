package de.benjamindahlhoff.smog.Data;

import java.util.ArrayList;

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
    private ArrayList<Double> mP1Values = new ArrayList<>();
    private ArrayList<Double> mP2Values = new ArrayList<>();
    private double mP1Mean;
    private double mP2Mean;
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

    public ArrayList<Double> getP1Values() {
        return mP1Values;
    }

    public void addP1Value(double p1Value) {
        mP1Values.add(p1Value);
    }

    public ArrayList<Double> getP2Values() {
        return mP2Values;
    }

    public void addP2Value(double p2Value) {
        mP2Values.add(p2Value);
    }

    public double getP1Mean() {
        return Math.round(mP1Mean*100.0)/100.0;
    }

    public void setP1Mean(double p1Mean) {
        mP1Mean = p1Mean;
    }

    public double getP2Mean() {
        return Math.round(mP2Mean*100.0)/100.0;
    }

    public void setP2Mean(double p2Mean) {
        mP2Mean = p2Mean;
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

    public void calculateMeanValues() {
        double p1Sum = 0;
        for (int i = 0; i < mP1Values.size(); i++) {
            p1Sum = p1Sum + mP1Values.get(i);
        }
        mP1Mean = p1Sum / mP1Values.size();

        double p2Sum = 0;
        for (int i = 0; i < mP1Values.size(); i++) {
            p2Sum = p2Sum + mP2Values.get(i);
        }
        mP2Mean = p2Sum / mP2Values.size();
    }
}

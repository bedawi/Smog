package de.benjamindahlhoff.smog.Data;

import java.util.ArrayList;

/**
 * Created by Benjamin Dahlhoff on 17.01.17.
 */

public class COStation {
    private String mTimestamp;
    private ArrayList<Double> mCarbonMonoxideVolumeMixingRatio = new ArrayList<>();
    private ArrayList<Double> mAtmosphericPressure = new ArrayList<>();
    private ArrayList<Double> mMeasurementPrecision = new ArrayList<>();
    private Position mPosition;
    private int mDistance;
    private double mCarbonMonoxideMeanValue;
    private ArrayList<Double> mCODensity = new ArrayList<>();

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    public ArrayList<Double> getCarbonMonoxideVolumeMixingRatio() {
        return mCarbonMonoxideVolumeMixingRatio;
    }

    public void addCarbonMonoxideVolumeMixingRatio(double carbonMonoxideVolumeMixingRatio) {
        mCarbonMonoxideVolumeMixingRatio.add(carbonMonoxideVolumeMixingRatio);
    }

    public ArrayList<Double> getAtmosphericPressure() {
        return mAtmosphericPressure;
    }

    public void addAtmosphericPressure(double atmosphericPressure) {
        mAtmosphericPressure.add(atmosphericPressure);
    }

    public ArrayList<Double> getMeasurementPrecision() {
        return mMeasurementPrecision;
    }

    public void addMeasurementPrecision(double measurementPrecision) {
        mMeasurementPrecision.add(measurementPrecision);
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public int getDistance() {
        return mDistance;
    }

    public void setDistance(int distance) {
        mDistance = distance;
    }

    public void calculateMeanValues() {
        double sum = 0;
        for (int i = 0; i < mCODensity.size(); i++) {
            sum = sum + mCODensity.get(i);
        }
        mCarbonMonoxideMeanValue = sum / mCODensity.size();

    }

    public double getCarbonMonoxideMeanValue() {
        return mCarbonMonoxideMeanValue;
    }

    public ArrayList<Double> getCODensity() {
        return mCODensity;
    }

    public void addCODensity(double CODensity) {
        mCODensity.add(CODensity);
    }
}

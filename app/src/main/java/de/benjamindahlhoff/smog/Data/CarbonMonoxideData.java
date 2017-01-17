package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 17.01.17.
 */

public class CarbonMonoxideData {
    private double mCarbonMonoxideVolumeMixingRatio;
    private double mAtmosphericPressure;
    private double mMeasurementPrecision;

    public CarbonMonoxideData(double carbonMonoxideVolumeMixingRatio, double atmosphericPressure, double measurementPrecision) {
        mCarbonMonoxideVolumeMixingRatio = carbonMonoxideVolumeMixingRatio;
        mAtmosphericPressure = atmosphericPressure;
        mMeasurementPrecision = measurementPrecision;
    }

    public double getCarbonMonoxideVolumeMixingRatio() {
        return mCarbonMonoxideVolumeMixingRatio;
    }

    public void setCarbonMonoxideVolumeMixingRatio(double carbonMonoxideVolumeMixingRatio) {
        mCarbonMonoxideVolumeMixingRatio = carbonMonoxideVolumeMixingRatio;
    }

    public double getAtmosphericPressure() {
        return mAtmosphericPressure;
    }

    public void setAtmosphericPressure(double atmosphericPressure) {
        mAtmosphericPressure = atmosphericPressure;
    }

    public double getMeasurementPrecision() {
        return mMeasurementPrecision;
    }

    public void setMeasurementPrecision(double measurementPrecision) {
        mMeasurementPrecision = measurementPrecision;
    }
}

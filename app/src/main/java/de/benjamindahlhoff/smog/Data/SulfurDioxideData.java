package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 17.01.17.
 */

public class SulfurDioxideData {
    private double mSulfurDioxideVolumeMixingRatio;
    private double mAtmosphericPressure;
    private double mMeasurementPrecision;

    public SulfurDioxideData(double sulfurDioxideVolumeMixingRatio, double atmosphericPressure, double measurementPrecision) {
        mSulfurDioxideVolumeMixingRatio = sulfurDioxideVolumeMixingRatio;
        mAtmosphericPressure = atmosphericPressure;
        mMeasurementPrecision = measurementPrecision;
    }

    public double getSulfurDioxideVolumeMixingRatio() {
        return mSulfurDioxideVolumeMixingRatio;
    }

    public void setSulfurDioxideVolumeMixingRatio(double sulfurDioxideVolumeMixingRatio) {
        mSulfurDioxideVolumeMixingRatio = sulfurDioxideVolumeMixingRatio;
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

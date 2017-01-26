package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 26.01.17.
 * This class holds the SensorValueData from the Stuttgart Feinstaub-Project
 * Data is acquired from here: http://api.luftdaten.info/static/v1/data.json
 */


public class ParticulatesData {
    private double mValue;
    private String mValueType;

    public double getValue() {
        return mValue;
    }

    public void setValue(double value) {
        mValue = value;
    }

    public String getValueType() {
        return mValueType;
    }

    public void setValueType(String valueType) {
        mValueType = valueType;
    }
}

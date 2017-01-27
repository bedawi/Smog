package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 26.01.17.
 * This class holds the complete Data from the Stuttgart Feinstaub-Project API
 * Data is acquired from here: http://api.luftdaten.info/static/v1/data.json
 */

public class Particulates {
    private String mTimeStamp; // when was it measured?
    private Position mPosition; // where was it measured?
    private ParticulatesValues[] mParticulatesValues; // what data was measured?

    public Particulates(String timeStamp, Position position, ParticulatesValues[] particulatesValues) {
        mTimeStamp = timeStamp;
        mPosition = position;
        mParticulatesValues = particulatesValues;
    }

    public String getTimeStamp() {
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        mTimeStamp = timeStamp;
    }

    public Position getPosition() {
        return mPosition;
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public ParticulatesValues[] getParticulatesValues() {
        return mParticulatesValues;
    }

    public void setParticulatesValues(ParticulatesValues[] particulatesValues) {
        mParticulatesValues = particulatesValues;
    }
}

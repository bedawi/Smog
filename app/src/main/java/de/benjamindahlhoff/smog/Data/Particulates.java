package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 26.01.17.
 * This class holds the complete Data from the Stuttgart Feinstaub-Project API
 * Data is acquired from here: http://api.luftdaten.info/static/v1/data.json
 */

public class Particulates {
    private String mTimeStamp; // when was it measured?
    private Position mPosition; // where was it measured?
    private ParticulatesData[] mParticulatesData; // what data was measured?

    public Particulates(String timeStamp, Position position, ParticulatesData[] particulatesData) {
        mTimeStamp = timeStamp;
        mPosition = position;
        mParticulatesData = particulatesData;
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

    public ParticulatesData[] getParticulatesData() {
        return mParticulatesData;
    }

    public void setParticulatesData(ParticulatesData[] particulatesData) {
        mParticulatesData = particulatesData;
    }
}

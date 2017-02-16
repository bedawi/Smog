package de.benjamindahlhoff.smog.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Benjamin Dahlhoff on 31.01.17.
 * This class holds data for each individual Measurement Station of the Stuttgart Air Pollution
 * Project Luftdaten.info
 *
 * @author Benjamin Dahlhoff
 */

public class Station implements Parcelable {
    private int mLocationId;
    private String mTimestamp;
    private final List<Double> mHumidity = new ArrayList<>();
    private final List<Double> mTemperature = new ArrayList<>();
    private Position mPosition;
    private final List<Double> mPM10Values = new ArrayList<>();
    private final List<Double> mPM25Values = new ArrayList<>();
    private int mDistance;
    private final List<Measurement> mMeasurements = new ArrayList<>();

    /**
     * Default constructor
     * Needed to make class parcelable
     */
    public Station() {
    }

    /**
     * Return location id of weather station. This is an int value identifying the station
     * @return location identifier of weather station
     */
    public int getLocationId() {
        return mLocationId;
    }

    /**
     * Set a loction id
     * @param locationId
     */
    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }

    /**
     * Add value to humidity set
     * @param humidity
     */
    public void addHumidity(double humidity) {
        mHumidity.add(humidity);
    }

    /**
     * Add value to temperature set
     * @param temperature
     */
    public void addTemperature(double temperature) {
        mTemperature.add(temperature);
    }

    /**
     * Set position of station
     * @param position
     */
    public void setPosition(Position position) {
        mPosition = position;
    }

    /**
     * Add value to PM 10 set
     * @param p1Value
     */
    public void addPM10Value(double p1Value) {
        mPM10Values.add(p1Value);
    }

    /**
     * Add value to PM 2.5 set
     * @param p2Value
     */
    public void addPM25Value(double p2Value) {
        mPM25Values.add(p2Value);
    }

    /**
     * Get distance
     * @return  Distance
     */
    public int getDistance() {
        return mDistance;
    }

    /**
     * Get list of all temperature values
     * @return
     */
    public List<Double> getTemperature() {
        return mTemperature;
    }

    /**
     * Get list of all PM 10 values
     * @return
     */
    public List<Double> getPM10Values() {
        return mPM10Values;
    }

    /**
     * Get list of all PM 2.5 values
     * @return
     */
    public List<Double> getPM25Values() {
        return mPM25Values;
    }

    /**
     * Get list of all humidity values
     * @return
     */
    public List<Double> getHumidity() {
        return mHumidity;
    }

    /**
     * The "measurements" are the mean values of each set. Every measurement represents one
     * field in visualisation of the station.
     * @param measurement
     */
    public void addMeasurement (Measurement measurement) {
        mMeasurements.add(measurement);
    }

    /**
     * We store the distance of the station to the current position of the user, let's
     * hope he does not beam over while loading data.
     * @param distance  in KM
     */
    public void setDistance(int distance) {
        mDistance = distance;
    }


    /**
     * Get value of a measurement (name)
     * @param name      Name of measurement
     * @return          value as integer
     * We return integer because the long numbers do not fit into our GUI
     */
    public int getValueFor (String name) {
        for (int i=0; i< mMeasurements.size(); i++) {
            if (mMeasurements.get(i).getName().equals(name)) {
                return mMeasurements.get(i).getValue();
            }
        }
        return 0;
    }

    /**
     * All the measurements
     * @return  Measurement.class
     */
    public List<Measurement> getMeasurements() {
        return mMeasurements;
    }



    /**
     * The following methods are part of the parcelable implementation:
     * https://developer.android.com/reference/android/os/Parcelable.html
     */

     /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     *
     * @return  a bitmask indicating the set of special object types marshaled by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest      The Parcel in which the object should be written.
     * @param flags     Additional flags about how the object should be written.
     *                  May be 0 or PARCELABLE_WRITE_RETURN_VALUE.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLocationId);
        dest.writeString(mTimestamp);
        dest.writeDouble(mPosition.getLatitude());
        dest.writeDouble(mPosition.getLongitude());
        dest.writeList(mPM10Values);
        dest.writeList(mPM25Values);
        dest.writeList(mHumidity);
        dest.writeList(mTemperature);
        dest.writeInt(mDistance);
        dest.writeList(mMeasurements);
    }

    /**
     * Read from a parcel and write into field variables
     * @param in    Content of parcel
     */
    private Station(Parcel in) {
        mLocationId = in.readInt();
        mTimestamp = in.readString();
        mPosition = new Position();
        mPosition.setLatitude(in.readDouble());
        mPosition.setLongitude(in.readDouble());
        in.readList(mPM10Values, Double.class.getClassLoader());
        in.readList(mPM25Values, Double.class.getClassLoader());
        in.readList(mHumidity, Double.class.getClassLoader());
        in.readList(mTemperature, Double.class.getClassLoader());
        mDistance = in.readInt();
        in.readList(mMeasurements, Measurement.class.getClassLoader());
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    /**
     * Calculate the mean value from a list of double values
     * (Yes this can be done easier with lambdas, but backwards compatibility of this app
     * prevents us from using them.
     * @param list      List of doubles
     * @return          result as double
     */
    private double means (List<Double> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum = sum + list.get(i);
        }
        if (list.size() != 0) {
            return sum / list.size();
        }
        return sum;
    }
}

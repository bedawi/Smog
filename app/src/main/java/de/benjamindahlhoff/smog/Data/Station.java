package de.benjamindahlhoff.smog.Data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     */
    public Station() {
        // Need this to make class parcelable
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

    public void addHumidity(double humidity) {
        mHumidity.add(humidity);
    }

    public void addTemperature(double temperature) {
        mTemperature.add(temperature);
    }

    public void setPosition(Position position) {
        mPosition = position;
    }

    public void addPM10Value(double p1Value) {
        mPM10Values.add(p1Value);
    }

    public void addPM25Value(double p2Value) {
        mPM25Values.add(p2Value);
    }

    public int getDistance() {
        return mDistance;
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
     * If the weather station provides us many sets of data from from some timeframe, we
     * calculate the mean values here
     */
    public void calculateMeanValues() {

        // PM10
        if (mPM10Values.size() > 0) {
            double means_pm10 = means(mPM10Values);
            Measurement mPM10 = new Measurement("PM10", means_pm10, "µg/m3",
                    getAirQualityColor(means_pm10));
            mMeasurements.add(mPM10);
        }

        // PM2.5
        if (mPM25Values.size() > 0) {
            double means_pm25 = means(mPM25Values);
            Measurement mPM25 = new Measurement("PM25", means_pm25, "µg/m3",
                    getAirQualityColor(means_pm25));
            mMeasurements.add(mPM25);
        }

        // Temperature:
        if (mTemperature.size() > 0) {
            double means_temperature = means(mTemperature);
            Measurement mTemp = new Measurement("TEMP", means_temperature, "°C",
                    Color.parseColor("#007bd2"));
            mMeasurements.add(mTemp);
        }


        // Humidity:
        if (mHumidity.size() > 0) {
            double means_humidity = means(mHumidity);
            Measurement mHUM = new Measurement("HUMIDITY", means_humidity, "%",
                    Color.parseColor("#BFB600"));
            mMeasurements.add(mHUM);
        }
    }

    /**
     * Return the color representation for air quality.
     * This needs to be fixed to work for other data (humidity, temperature, SO2...)
     * Also the data should come from colors.xml instead of being hard coded.
     * @param value     a measurement value
     * @return          a color as integer
     */
    public int getAirQualityColor(double value) {
        if (value <= 25) { return Color.parseColor("#00796B"); }
        if (value <= 50) { return Color.parseColor("#F9A825"); }
        if (value <= 75) { return Color.parseColor("#E65100"); }
        if (value <= 100) { return Color.parseColor("#DD2C00"); }
        if (value <= 200) { return Color.parseColor("#960084"); }
        if (value > 200) { return Color.parseColor("#001996"); }
        return Color.parseColor("#001996");
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

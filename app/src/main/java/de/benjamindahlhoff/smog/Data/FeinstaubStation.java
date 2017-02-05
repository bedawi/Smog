package de.benjamindahlhoff.smog.Data;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.value;

/**
 * Created by Benjamin Dahlhoff on 31.01.17.
 * This class holds data for each individual Measurement Station of the Stuttgart Air Pollution
 * Project Luftdaten.info
 *
 * The name of the class comes from the German word for Particulates in the air "Feinstaub".
 */

public class FeinstaubStation implements Parcelable {
    private int mLocationId;
    private String mTimestamp;
    private double mHumidity;
    private double mTemperature;
    private Position mPosition;
    private final List<Double> mPM10Values = new ArrayList<>();
    private final List<Double> mPM25Values = new ArrayList<>();
    private double mP1Mean;
    private double mP2Mean;
    private int mMaxMicro;
    private int mMinMicro;
    private int mSamples;
    private int mDistance;

    public FeinstaubStation() {
    }

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

    public List<Double> getPM10Values() {
        return mPM10Values;
    }

    public void addPM10Value(double p1Value) {
        mPM10Values.add(p1Value);
    }

    public List<Double> getPM25Values() {
        return mPM25Values;
    }

    public void addPM25Value(double p2Value) {
        mPM25Values.add(p2Value);
    }

    public double getPM10Mean() {
        return Math.round(mP1Mean*100.0)/100.0;
    }

    public void setPM10Mean(double p1Mean) {
        mP1Mean = p1Mean;
    }

    public double getPM25Mean() {
        return Math.round(mP2Mean*100.0)/100.0;
    }

    public void setPM25Mean(double p2Mean) {
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
        for (int i = 0; i < mPM10Values.size(); i++) {
            p1Sum = p1Sum + mPM10Values.get(i);
        }
        mP1Mean = p1Sum / mPM10Values.size();
        // In the Future we do:
        // mP1Mean = mPM10Values.stream().mapToDouble(d -> d.doubleValue()).average();

        double p2Sum = 0;
        for (int i = 0; i < mPM10Values.size(); i++) {
            p2Sum = p2Sum + mPM25Values.get(i);
        }
        mP2Mean = p2Sum / mPM25Values.size();
    }

    public int getColorforValue(double value) {
        String[] colors = {
                "#00796B",
                "#F9A825",
                "#E65100",
                "#DD2C00",
                "#960084",
                "#001996"
        };
        if (value <= 25) { return Color.parseColor(colors[0]); }
        if (value <= 50) { return Color.parseColor(colors[1]); }
        if (value <= 75) { return Color.parseColor(colors[2]); }
        if (value <= 100) { return Color.parseColor(colors[3]); }
        if (value <= 200) { return Color.parseColor(colors[4]); }
        if (value > 200) { return Color.parseColor(colors[5]); }
        return Color.parseColor(colors[5]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mLocationId);
        dest.writeString(mTimestamp);
        dest.writeDouble(mHumidity);
        dest.writeDouble(mTemperature);
        dest.writeDouble(mPosition.getLatitude());
        dest.writeDouble(mPosition.getLongitude());
        dest.writeList(mPM10Values);
        dest.writeList(mPM25Values);
        dest.writeDouble(mP1Mean);
        dest.writeDouble(mP2Mean);
        dest.writeInt(mMaxMicro);
        dest.writeInt(mMinMicro);
        dest.writeInt(mSamples);
        dest.writeInt(mDistance);
    }

    private FeinstaubStation(Parcel in) {
        mLocationId = in.readInt();
        mTimestamp = in.readString();
        mHumidity = in.readDouble();
        mTemperature = in.readDouble();
        mPosition = new Position();
        mPosition.setLatitude(in.readDouble());
        mPosition.setLongitude(in.readDouble());
        in.readList(mPM10Values, Double.class.getClassLoader());
        in.readList(mPM25Values, Double.class.getClassLoader());
        mP1Mean = in.readDouble();
        mP2Mean = in.readDouble();
        mMaxMicro = in.readInt();
        mMaxMicro = in.readInt();
        mSamples = in.readInt();
        mDistance = in.readInt();
    }

    public static final Creator<FeinstaubStation> CREATOR = new Creator<FeinstaubStation>() {
        @Override
        public FeinstaubStation createFromParcel(Parcel in) {
            return new FeinstaubStation(in);
        }

        @Override
        public FeinstaubStation[] newArray(int size) {
            return new FeinstaubStation[size];
        }
    };
}

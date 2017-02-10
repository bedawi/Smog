package de.benjamindahlhoff.smog.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Benjamin Dahlhoff on 07.02.17.
 * This class is a data model to store data from weather data
 * @author Benjamin Dahlhoff
 */

public class Measurement implements Parcelable {
    private String mName;
    private double mValue;
    private String mUnits;
    private int mColorInterpretation;

    /**
     * Default constructor containing all the fields
     * @param name      e.g. "PM10"
     * @param value     e.g. 1245.12345
     * @param units     e.g. "µg/m3"
     * @param colorInterpretation   Color converted to int
     */
    public Measurement(String name, double value, String units, int colorInterpretation) {
        this.mName = name;
        this.mValue = value;
        this.mUnits = units;
        this.mColorInterpretation = colorInterpretation;
    }

    public String getName() {
        return mName;
    }

    public int getValue() {
        return (int) Math.round(mValue);
    }

    public String getUnits() {
        return mUnits;
    }

    public int getColorInterpretation() {
        return mColorInterpretation;
    }

    /**
     * Making the data of this class parcelable
     * https://developer.android.com/reference/android/os/Parcelable.html
     * @param in
     */
    protected Measurement(Parcel in) {
        mName = in.readString();
        mValue = in.readDouble();
        mUnits = in.readString();
        mColorInterpretation = in.readInt();
    }

    public static final Creator<Measurement> CREATOR = new Creator<Measurement>() {
        @Override
        public Measurement createFromParcel(Parcel in) {
            return new Measurement(in);
        }

        @Override
        public Measurement[] newArray(int size) {
            return new Measurement[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mValue);
        dest.writeString(mUnits);
        dest.writeInt(mColorInterpretation);
    }
}

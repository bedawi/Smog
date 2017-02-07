package de.benjamindahlhoff.smog.Data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Benjamin Dahlhoff on 07.02.17.
 *
 *
 */

public class Measurement implements Parcelable {
    private String mName;
    private double mValue;
    private String mUnits;
    private int mColorInterpretation;

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

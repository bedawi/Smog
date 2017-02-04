package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 17.01.17.
 */

public class Pollution {
    private COStation mCarbonMonoxide;
    private double mOzoneLayerThickness;

    public Pollution() {
    }

    public COStation getCarbonMonoxide() {
        return mCarbonMonoxide;
    }

    public void setCarbonMonoxide(COStation carbonMonoxide) {
        mCarbonMonoxide = carbonMonoxide;
    }

    public double getOzoneLayerThickness() {
        return mOzoneLayerThickness;
    }

    public void setOzoneLayerThickness(double ozoneLayerThickness) {
        mOzoneLayerThickness = ozoneLayerThickness;
    }
}

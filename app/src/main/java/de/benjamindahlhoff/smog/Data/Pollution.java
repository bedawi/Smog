package de.benjamindahlhoff.smog.Data;

/**
 * Created by Benjamin Dahlhoff on 17.01.17.
 */

public class Pollution {
    private CarbonMonoxideData mCarbonMonoxide;
    private double mOzoneLayerThickness;

    public Pollution() {
    }

    public CarbonMonoxideData getCarbonMonoxide() {
        return mCarbonMonoxide;
    }

    public void setCarbonMonoxide(CarbonMonoxideData carbonMonoxide) {
        mCarbonMonoxide = carbonMonoxide;
    }

    public double getOzoneLayerThickness() {
        return mOzoneLayerThickness;
    }

    public void setOzoneLayerThickness(double ozoneLayerThickness) {
        mOzoneLayerThickness = ozoneLayerThickness;
    }
}

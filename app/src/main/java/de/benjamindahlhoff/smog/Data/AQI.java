package de.benjamindahlhoff.smog.Data;

import android.content.Context;

import de.benjamindahlhoff.smog.R;

/**
 * Created by benjamin on 12.02.17.
 *
 * This class calculates the Air Quality Index as defined here:
 * https://en.wikipedia.org/wiki/Air_quality_index#Computing_the_AQI
 *
 * @author Benjamin Dahlhoff
 */

public class AQI {

    Context mContext;

    public AQI(Context context) {
        mContext = context;
    }

    /**
     * Calculates Air Quality Index from concentration.
     * @param concentration double: concentration of pollutant
     * @param pollutant     String: Name of pollutant
     *                      [ O3, PM25, PM10, CO, SO2, NO2 ]
     * @return              int: AQI
     */
    public int calculateIndex (double concentration, String pollutant) {
        double  cLow    = getCLow   (pollutant, concentration);
        double  cHigh   = getCHigh  (pollutant, concentration);
        int     iLow    = getILow   (pollutant, concentration);
        int     iHigh   = getIHigh  (pollutant, concentration);
        double  index   = ( (iHigh - iLow) / (cHigh - cLow) ) * (concentration - cLow) + iLow;
        return (int) Math.round(index);
    }

    /**
     * Returns the color corresponding to the Air Quality Index
     * @param aqi   int: AQI
     * @return      int: color as integer
     */
    public int getColor (int aqi) {
        if (aqi >= 0 && aqi <= 50) return mContext.getColor(R.color.aqi_good);
        if (aqi >= 51 && aqi <= 100) return mContext.getColor(R.color.aqi_moderate);
        if (aqi >= 101 && aqi <= 150) return mContext.getColor(R.color.aqi_unhealthy_sensitivy);
        if (aqi >= 151 && aqi <= 200) return mContext.getColor(R.color.aqi_unhealthy);
        if (aqi >= 201 && aqi <= 300) return mContext.getColor(R.color.aqi_very_unhealty);
        if (aqi >= 301) return mContext.getColor(R.color.aqi_hazardous);
        return mContext.getColor(R.color.aqi_good);
    }

    /**
     * Calculates the AQI and returns corresponding color
     * @param concentration double: concentration of pollutant
     * @param pollutant     String: name of pullutant
     *                      [ O3, PM25, PM10, CO, SO2, NO2 ]
     * @return              int: color as integer
     */
    public int getColorFromConcentration(double concentration, String pollutant) {
        return getColor(calculateIndex(concentration, pollutant));
    }


    /**
     * getCHigh determines the concentration breakpoint that is ≥C.
     * if concentration exceeds higher breakpoint, C is returned.
     * @param pollutant     String: which pollutant? O3, PM25, PM10, CO, SO2, NO2
     * @param concentration double: concentration of pollutant
     * @return              double: high concentration breakpoint
     */
    private double getCHigh(String pollutant, Double concentration) {
        double cHigh = -1;
        switch (pollutant) {
            case "O3":
                if (concentration < 200)    { cHigh = 200; }
                if (concentration < 105)    { cHigh = 105; }
                if (concentration < 85)     { cHigh = 85; }
                if (concentration < 70)     { cHigh = 70; }
                if (concentration < 54)     { cHigh = 54; }
                break;
            case "PM25":
                if (concentration < 500.4)  { cHigh = 500.4; }
                if (concentration < 350.4)  { cHigh = 350.4; }
                if (concentration < 250.4)  { cHigh = 250.4; }
                if (concentration < 150.4)  { cHigh = 150.4; }
                if (concentration < 55.4)   { cHigh = 55.4; }
                if (concentration < 35.4)   { cHigh = 35.4; }
                if (concentration < 12)     { cHigh = 12; }
                break;
            case "PM10":
                if (concentration < 604)    { cHigh = 604; }
                if (concentration < 504)    { cHigh = 504; }
                if (concentration < 424)    { cHigh = 424; }
                if (concentration < 354)    { cHigh = 354; }
                if (concentration < 254)    { cHigh = 254; }
                if (concentration < 154)    { cHigh = 154; }
                if (concentration < 54)     { cHigh = 54; }
                break;
            case "CO":
                if (concentration < 50.4)   { cHigh = 50.4; }
                if (concentration < 40.4)   { cHigh = 40.4; }
                if (concentration < 30.4)   { cHigh = 30.4; }
                if (concentration < 15.4)   { cHigh = 15.4; }
                if (concentration < 12.4)   { cHigh = 12.4; }
                if (concentration < 9.4)    { cHigh = 9.4; }
                if (concentration < 4.4)    { cHigh = 4.4; }
                break;
            case "SO2":
                if (concentration < 1004)   { cHigh = 1004; }
                if (concentration < 804)    { cHigh = 804; }
                if (concentration < 604)    { cHigh = 604; }
                if (concentration < 304)    { cHigh = 304; }
                if (concentration < 185)    { cHigh = 185; }
                if (concentration < 75)     { cHigh = 75; }
                if (concentration < 35)     { cHigh = 35; }
                break;
            case "NO2":
                if (concentration < 2049)   { cHigh = 2049; }
                if (concentration < 1649)   { cHigh = 1649; }
                if (concentration < 1249)   { cHigh = 1249; }
                if (concentration < 649)    { cHigh = 649; }
                if (concentration < 360)    { cHigh = 360; }
                if (concentration < 100)    { cHigh = 100; }
                if (concentration < 53)     { cHigh = 53; }
                break;
        }
        if (cHigh == -1) { cHigh = concentration; }
        return cHigh;
    }

    /**
     * getCLow determines the breakpoint that is ≤ C.
     * If concentration is below breakpoint, zero is returned.
     * @param pollutant     String: which pollutant? O3, PM25, PM10, CO, SO2, NO2
     * @param concentration double: concentration of pollutant
     * @return              double: low concentration breakpoint
     */
    private double getCLow(String pollutant, Double concentration) {
        double cLow = -1;
        switch (pollutant) {
            case "O3":
                if (concentration > 55)     { cLow = 55; }
                if (concentration > 71)     { cLow = 71; }
                if (concentration > 86)     { cLow = 86; }
                if (concentration > 106)    { cLow = 106; }
                break;
            case "PM25":
                if (concentration > 12.1)   { cLow = 12.1; }
                if (concentration > 35.5)   { cLow = 35.5; }
                if (concentration > 55.5)   { cLow = 55.5; }
                if (concentration > 150.5)  { cLow = 150.5; }
                if (concentration > 250.5)  { cLow = 250.5; }
                if (concentration > 350.5)  { cLow = 350.5; }
                break;
            case "PM10":
                if (concentration > 55)     { cLow = 55; }
                if (concentration > 155)    { cLow = 155; }
                if (concentration > 255)    { cLow = 255; }
                if (concentration > 355)    { cLow = 355; }
                if (concentration > 425)    { cLow = 425; }
                if (concentration > 505)    { cLow = 505; }
                break;
            case "CO":
                if (concentration > 4.5)    { cLow = 55; }
                if (concentration > 9.5)    { cLow = 9.5; }
                if (concentration > 12.5)   { cLow = 12.5; }
                if (concentration > 15.5)   { cLow = 15.5; }
                if (concentration > 30.5)   { cLow = 30.5; }
                if (concentration > 40.5)   { cLow = 40.5; }
                break;
            case "SO2":
                if (concentration > 36)     { cLow = 36; }
                if (concentration > 76)     { cLow = 76; }
                if (concentration > 186)    { cLow = 186; }
                if (concentration > 305)    { cLow = 305; }
                if (concentration > 605)    { cLow = 605; }
                if (concentration > 805)    { cLow = 805; }
                break;
            case "NO2":
                if (concentration > 54)     { cLow = 54; }
                if (concentration > 101)    { cLow = 101; }
                if (concentration > 361)    { cLow = 362; }
                if (concentration > 650)    { cLow = 650; }
                if (concentration > 1250)   { cLow = 1250; }
                if (concentration > 1650)   { cLow = 1650; }
                break;
        }
        if (cLow == -1) { cLow = 0; }
        return cLow;
    }

    /**
     * iHigh returns the index breakpoint corresponding to concentration
     * @param concentration     double: concentration
     * @return                  int: getIHigh
     */
    private int getIHigh(String pollutant, double concentration) {
        int iHigh = -1;
        switch (pollutant) {
            case "O3":
                if (concentration < 200)    { iHigh = 300; }
                if (concentration < 105)    { iHigh = 200; }
                if (concentration < 85)     { iHigh = 150; }
                if (concentration < 70)     { iHigh = 100; }
                if (concentration < 54)     { iHigh = 50; }
                break;
            case "PM25":
                if (concentration < 500.4)  { iHigh = 500; }
                if (concentration < 350.4)  { iHigh = 400; }
                if (concentration < 250.4)  { iHigh = 300; }
                if (concentration < 150.4)  { iHigh = 200; }
                if (concentration < 55.4)   { iHigh = 150; }
                if (concentration < 35.4)   { iHigh = 100; }
                if (concentration < 12)     { iHigh = 50; }
                break;
            case "PM10":
                if (concentration < 604)    { iHigh = 500; }
                if (concentration < 504)    { iHigh = 400; }
                if (concentration < 424)    { iHigh = 300; }
                if (concentration < 354)    { iHigh = 200; }
                if (concentration < 254)    { iHigh = 150; }
                if (concentration < 154)    { iHigh = 100; }
                if (concentration < 54)     { iHigh = 50; }
                break;
            case "CO":
                if (concentration < 50.4)   { iHigh = 500; }
                if (concentration < 40.4)   { iHigh = 400; }
                if (concentration < 30.4)   { iHigh = 300; }
                if (concentration < 15.4)   { iHigh = 200; }
                if (concentration < 12.4)   { iHigh = 150; }
                if (concentration < 9.4)    { iHigh = 100; }
                if (concentration < 4.4)    { iHigh = 50; }
                break;
            case "SO2":
                if (concentration < 1004)   { iHigh = 500; }
                if (concentration < 804)    { iHigh = 400; }
                if (concentration < 604)    { iHigh = 300; }
                if (concentration < 304)    { iHigh = 200; }
                if (concentration < 185)    { iHigh = 150; }
                if (concentration < 75)     { iHigh = 100; }
                if (concentration < 35)     { iHigh = 50; }
                break;
            case "NO2":
                if (concentration < 2049)   { iHigh = 500; }
                if (concentration < 1649)   { iHigh = 400; }
                if (concentration < 1249)   { iHigh = 300; }
                if (concentration < 649)    { iHigh = 200; }
                if (concentration < 360)    { iHigh = 150; }
                if (concentration < 100)    { iHigh = 100; }
                if (concentration < 53)     { iHigh = 50; }
                break;
        }
        if (iHigh == -1) { iHigh = (int) Math.round(concentration); }
        return iHigh;
    }

    /**
     * iLow returns the index breakpoint corresponding to concentration
     * @param concentration     double: concentration
     * @return                  int: getILow
     */
    private int getILow(String pollutant, double concentration) {
        int iLow = 0;
        switch (pollutant) {
            case "O3":
                if (concentration > 55)     { iLow = 51; }
                if (concentration > 71)     { iLow = 101; }
                if (concentration > 86)     { iLow = 151; }
                if (concentration > 106)    { iLow = 201; }
                break;
            case "PM25":
                if (concentration > 12.1)   { iLow = 51; }
                if (concentration > 35.5)   { iLow = 101; }
                if (concentration > 55.5)   { iLow = 151; }
                if (concentration > 150.5)  { iLow = 201; }
                if (concentration > 250.5)  { iLow = 301; }
                if (concentration > 350.5)  { iLow = 401; }
                break;
            case "PM10":
                if (concentration > 55)     { iLow = 51; }
                if (concentration > 155)    { iLow = 101; }
                if (concentration > 255)    { iLow = 151; }
                if (concentration > 355)    { iLow = 201; }
                if (concentration > 425)    { iLow = 301; }
                if (concentration > 505)    { iLow = 401; }
                break;
            case "CO":
                if (concentration > 4.5)    { iLow = 51; }
                if (concentration > 9.5)    { iLow = 101; }
                if (concentration > 12.5)   { iLow = 151; }
                if (concentration > 15.5)   { iLow = 201; }
                if (concentration > 30.5)   { iLow = 301; }
                if (concentration > 40.5)   { iLow = 401; }
                break;
            case "SO2":
                if (concentration > 36)     { iLow = 51; }
                if (concentration > 76)     { iLow = 101; }
                if (concentration > 186)    { iLow = 151; }
                if (concentration > 305)    { iLow = 201; }
                if (concentration > 605)    { iLow = 301; }
                if (concentration > 805)    { iLow = 401; }
                break;
            case "NO2":
                if (concentration > 54)     { iLow = 51; }
                if (concentration > 101)    { iLow = 101; }
                if (concentration > 361)    { iLow = 151; }
                if (concentration > 650)    { iLow = 201; }
                if (concentration > 1250)   { iLow = 301; }
                if (concentration > 1650)   { iLow = 401; }
                break;
        }
        return iLow;
    }
}

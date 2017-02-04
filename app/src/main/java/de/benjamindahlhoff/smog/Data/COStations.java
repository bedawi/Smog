package de.benjamindahlhoff.smog.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Benjamin Dahlhoff on 03.02.17.
 * THIS CLASS IS EXPERIMENTAL. PLEASE DO NOT USE...YET
 */

public class COStations {
    public final static String TAG = COStations.class.getSimpleName();
    ArrayList<COStation> mCOStations = new ArrayList<>();
    private Position mCurrentPosition;

    public void addStation(JSONObject stationData) {
        try {
            COStation coStation = new COStation();
            coStation.setTimestamp(stationData.getString("time"));

            // Extract the position:
            Position tmpPosition = new Position();
            JSONObject LocationObject = stationData.getJSONObject("location");
            tmpPosition.setLatitude(LocationObject.getDouble("latitude"));
            tmpPosition.setLongitude(LocationObject.getDouble("longitude"));
            coStation.setPosition(tmpPosition);
            coStation.setDistance(tmpPosition.distanceFromPosition(mCurrentPosition.getLatitude(),
                    mCurrentPosition.getLongitude()));

            JSONArray MeasurementsArray = stationData.getJSONArray("data");
            for (int i = 0; i < MeasurementsArray.length(); i++) {
                JSONObject tempMeasurementsObject = MeasurementsArray.getJSONObject(i);
                coStation.addMeasurementPrecision(tempMeasurementsObject.getDouble("precision"));
                coStation.addCarbonMonoxideVolumeMixingRatio(tempMeasurementsObject.getDouble("value"));
                coStation.addAtmosphericPressure(tempMeasurementsObject.getDouble("pressure"));
                coStation.addCODensity(calculateCODensity(tempMeasurementsObject.getDouble("value"),
                        tempMeasurementsObject.getDouble("pressure"),
                        10));

            }
            coStation.calculateMeanValues();
            mCOStations.add(coStation);
        }
        catch (JSONException e) {
        Log.e(TAG, "Error! Skipping this entry. Error:" +e);
        //e.printStackTrace();
        }


    }

    public void clear() {
        mCOStations.clear();
    }

    public Position getCurrentPosition() {
        return mCurrentPosition;
    }

    public void setCurrentPosition(Position currentPosition) {
        mCurrentPosition = currentPosition;
    }

    public void sortByDistance() {
        // Sorting Data, nearest first.
        Collections.sort(mCOStations, new Comparator<COStation>() {
            @Override
            public int compare(COStation o1, COStation o2) {
                //int returnValue = 0;
                if (Integer.valueOf(o1.getDistance()) > Integer.valueOf(o2.getDistance())) {
                    return 1;
                }
                if (Integer.valueOf(o1.getDistance()) < Integer.valueOf(o2.getDistance())) {
                    return -1;
                }
                return 0;
            }
        });
        Log.v(TAG, "Done Sorting CO-Stations! Next station is "
                + mCOStations.get(0).getDistance()
                + " KM away");
        Log.v(TAG, "Station farest CO-Station is "
                + mCOStations.get(mCOStations.size()-1).getDistance()
                + " KM away");
    }

    public COStation getStationByIndex (int index) {
        return mCOStations.get(index);
    }

    private double calculateCODensity (double mixingRatio, double pressure, double temperatureInCelsius) {
        // Calculate CO μg/m3
        // = vmrCO * some Value * pressure / temperature in kelvin
        // cf: http://wdc.dlr.de/data_products/SERVICES/PROMOTE_O3/vmr.html
        // Fix me: 577.3 is for Ozone not CO - ask for help
        return (mixingRatio * 577.3 * pressure) / (temperatureInCelsius*273.15);
    }
}

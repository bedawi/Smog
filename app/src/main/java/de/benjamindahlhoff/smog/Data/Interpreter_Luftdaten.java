package de.benjamindahlhoff.smog.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * This class interprets the Data from Luftdaten.info
 * In: JSON Object
 * Out: ArrayList of Stations
 */

public class Interpreter_Luftdaten {

    private final ArrayList<Station> mInjectStations = new ArrayList<>();

    public ArrayList<Station> converter (JSONArray data, Position currentPosition) {

        for (int i=0; i< data.length(); i++) {
            try {
                JSONObject stationData = data.getJSONObject(i);
                // create a temporary station
                Station tempStation;

                // First lets get the station's id and Position from the JSON object
                JSONObject LocationObject = stationData.getJSONObject("location");
                int stationId = LocationObject.getInt("id");

                // now lets see, if a station with this index already exists in our Arraylist
                int tmpStationID = StationExistsAtIndex(stationId);
                if (tmpStationID != -1) {
                    // There is a station with this ID, lets pull her data:
                    tempStation = mInjectStations.get(tmpStationID);
                    //Log.v(TAG, "Station " + mInjectStations.get(tmpStationID) + " exists exists as " + tmpStationID);

                } else {
                    // There is no station with this ID, lets give our temporary station the id we
                    // got from the JSON object before:
                    tempStation = new Station();
                    tempStation.setLocationId(stationId);
                }

                // Extract the position:
                Position tmpPosition = new Position();
                tmpPosition.setLatitude(LocationObject.getDouble("latitude"));
                tmpPosition.setLongitude(LocationObject.getDouble("longitude"));
                tempStation.setPosition(tmpPosition);
                tempStation.setDistance(tmpPosition.distanceFromPosition(currentPosition.getLatitude(),
                        currentPosition.getLongitude()));

                // Get the Sensor values
                JSONArray MeasurementsArray = stationData.getJSONArray("sensordatavalues");

                for (int j = 0; j < MeasurementsArray.length(); j++)
                {
                    JSONObject tempMeasurementsObject = MeasurementsArray.getJSONObject(j);

                    //double value = tempMeasurementsObject.getDouble("value");
                    String valueType = tempMeasurementsObject.getString("value_type");

                    switch (valueType) {
                        case "humidity"     : tempStation.addHumidity
                                (tempMeasurementsObject.getDouble("value"));
                            break;
                        case "temperature"  : tempStation.addTemperature
                                (tempMeasurementsObject.getDouble("value"));
                            break;
                        case "P1"           : tempStation.addPM10Value
                                (tempMeasurementsObject.getDouble("value"));
                            break;
                        case "P2"           : tempStation.addPM25Value
                                (tempMeasurementsObject.getDouble("value"));
                            break;
                    /*
                    default:    Log.v(TAG, "Unknown value: "
                            + tempMeasurementsObject.getString("value_type")
                            + ": "
                            + tempMeasurementsObject.getString("value"));
                            */
                    }
                }

                // Alright, finally, lets insert the ne
                if (tmpStationID != -1) {
                    // update existing station
                    mInjectStations.set(tmpStationID, tempStation);
                } else {
                    // add new station
                    mInjectStations.add(tempStation);
                }

            } catch (JSONException e) {
                Log.e(TAG, "Error! Skipping this entry. Error:" +e);
            }
        }
        return mInjectStations;
    }

    private int StationExistsAtIndex (int mStationID) {
        for (int i = 0; i< mInjectStations.size(); i++) {
            if (mInjectStations.get(i).getLocationId() == mStationID) {
                return i;
            }
        }
        return -1;
    }

}

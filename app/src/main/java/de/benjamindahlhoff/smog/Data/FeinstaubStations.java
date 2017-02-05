package de.benjamindahlhoff.smog.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Benjamin Dahlhoff on 31.01.17.
 *
 * This class holds all the stations' data in one ArrayList.
 * It also provides methods to add data and to sort itself by the distance of the stations to
 * the user's position.
 *
 */

public class FeinstaubStations {
    public final static String TAG = FeinstaubStations.class.getSimpleName();
    ArrayList<FeinstaubStation> mStations = new ArrayList<>();
    private Position mCurrentPosition;

    public FeinstaubStations() {
    }

    public FeinstaubStations(ArrayList<FeinstaubStation> stations) {
        mStations = stations;
    }

    public void clear() {
        mStations.clear();
    }

    /**
     * @param stationData JSON Object where the data is extracted from
     */
    public void addDataToStation(JSONObject stationData) {
        try {
            // create a temporary station
            FeinstaubStation tempStation;

            // First lets get the station's id and Position from the JSON object
            JSONObject LocationObject = stationData.getJSONObject("location");
            int stationId = LocationObject.getInt("id");

            // now lets see, if a station with this index already exists in our Arraylist
            int tmpStationID = StationExistsAtIndex(stationId);
            if (tmpStationID != -1) {
                // There is a station with this ID, lets pull her data:
                tempStation = mStations.get(tmpStationID);
                //Log.v(TAG, "Station exists");

            } else {
                // There is no station with this ID, lets give our temporary station the id we
                // got from the JSON object before:
                tempStation = new FeinstaubStation();
                tempStation.setLocationId(stationId);
            }

            // Extract the position:
            Position tmpPosition = new Position();
            tmpPosition.setLatitude(LocationObject.getDouble("latitude"));
            tmpPosition.setLongitude(LocationObject.getDouble("longitude"));
            tempStation.setPosition(tmpPosition);
            tempStation.setDistance(tmpPosition.distanceFromPosition(mCurrentPosition.getLatitude(),
                    mCurrentPosition.getLongitude()));

            // Get the Sensor values
            JSONArray MeasurementsArray = stationData.getJSONArray("sensordatavalues");

            for (int j = 0; j < MeasurementsArray.length(); j++)
            {
                JSONObject tempMeasurementsObject = MeasurementsArray.getJSONObject(j);

                //double value = tempMeasurementsObject.getDouble("value");
                String valueType = tempMeasurementsObject.getString("value_type");

                switch (valueType) {
                    case "humidity"     : tempStation.setHumidity
                            (tempMeasurementsObject.getDouble("value"));
                        break;
                    case "temperature"  : tempStation.setTemperature
                            (tempMeasurementsObject.getDouble("value"));
                        break;
                    case "P1"           : tempStation.addPM10Value
                            (tempMeasurementsObject.getDouble("value"));
                        break;
                    case "P2"           : tempStation.addPM25Value
                            (tempMeasurementsObject.getDouble("value"));
                        break;
                    case "max_micro"    : tempStation.setMaxMicro
                            (tempMeasurementsObject.getInt("value"));
                        break;
                    case "min_micro"    : tempStation.setMinMicro
                            (tempMeasurementsObject.getInt("value"));
                        break;
                    case "samples"      : tempStation.setSamples
                            (tempMeasurementsObject.getInt("value"));
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
                mStations.set(tmpStationID, tempStation);
            } else {
                // add new station
                mStations.add(tempStation);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error! Skipping this entry. Error:" +e);
        }

    }

    public Position getCurrentPosition() {
        return mCurrentPosition;
    }

    public ArrayList<FeinstaubStation> getStations() {
        return mStations;
    }

    public FeinstaubStation getStationByIndex(int index) {
        return mStations.get(index);
    }

    public void sortByDistance() {
        // Sorting Data, nearest first.

        Collections.sort(mStations, new Comparator<FeinstaubStation>() {
            @Override
            public int compare(FeinstaubStation o1, FeinstaubStation o2) {
                return o1.getDistance() - o2.getDistance();
            }
        });
        Log.v(TAG, "Done Sorting! Next station is "
                + mStations.get(0).getLocationId()
                + ", "
                + mStations.get(0).getDistance()
                + " KM away");
        Log.v(TAG, "Station farest away is #"
                + mStations.get(mStations.size()-1).getLocationId()
                + ", "
                + mStations.get(mStations.size()-1).getDistance()
                + " KM away");
    }

    public void calculateMeans() {
        for (int i = 0; i < mStations.size(); i++) {
            mStations.get(i).calculateMeanValues();
        }
    }

    public void setCurrentPosition(Position currentPosition) {
        mCurrentPosition = currentPosition;
    }

    private int StationExistsAtIndex (int mStationID) {
        for (int i=0; i< mStations.size(); i++) {
            if (mStations.get(i).getLocationId() == mStationID) {
                return i;
            }
        }
        return -1;
    }

}

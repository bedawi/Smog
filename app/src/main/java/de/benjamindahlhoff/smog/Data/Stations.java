package de.benjamindahlhoff.smog.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Benjamin Dahlhoff on 31.01.17.
 *
 * This class holds all the stations' data in one ArrayList.
 * It also provides methods to add data and to sort itself by the distance of the stations to
 * the user's position.
 *
 */

public class Stations {
    public final static String TAG = Stations.class.getSimpleName();
    ArrayList<Station> mStations = new ArrayList<>();
    private Position mCurrentPosition;

    public Stations() {
    }

    public Stations(ArrayList<Station> stations) {
        mStations = stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.clear();
        mStations = stations;
    }

    public void clear() {
        mStations.clear();
    }

    public void addData_fromWAQI(JSONObject stationData) {
        try {
            Station tempStation;
            JSONObject DataObject = stationData.getJSONObject("data");

        } catch (JSONException e) {
            Log.e(TAG, "Error! Skipping this entry. Error:" +e);
        }


    }

    public void injectStations (ArrayList<Station> newStations) {
        for (int i=0; i<newStations.size(); i++) {
            if (StationExistsAtIndex(newStations.get(i).getLocationId()) == -1) {
                mStations.add(newStations.get(i));
            }
        }
    }

    public Position getCurrentPosition() {
        return mCurrentPosition;
    }

    public ArrayList<Station> getStations() {
        return mStations;
    }

    public Station getStationByIndex(int index) {
        return mStations.get(index);
    }

    public void sortByDistance() {
        // Sorting Data, nearest first.

        Collections.sort(mStations, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
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
        for (int i = 0; i< mStations.size(); i++) {
            if (mStations.get(i).getLocationId() == mStationID) {
                return i;
            }
        }
        return -1;
    }


}

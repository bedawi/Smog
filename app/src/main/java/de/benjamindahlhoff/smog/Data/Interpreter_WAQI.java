package de.benjamindahlhoff.smog.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class interprets the data feed from
 * http://aqicn.org/json-api/doc/#api-Geolocalized_Feed-GetGeolocFeed
 *
 * Just inject some JSON and get Station Data out. :-D
 *
 * Data basis: http://aqicn.org/json-api/doc/
 * Data interpretation: https://airnow.gov/index.cfm?action=resources.conc_aqi_calc
 * Data calculation: https://en.wikipedia.org/wiki/Air_quality_index#Computing_the_AQI
 */

public class Interpreter_WAQI {
    public final static String TAG = Interpreter_WAQI.class.getSimpleName();

    /**
     * Interprets data from WAQI-Project and returns them as weather stations as defined
     * in Station.class
     * @param data              Data as a String
     * @param currentPosition   The user's current position
     *                          Necessary to define distance to station
     * @return                  ArrayList of Station
     */
    public ArrayList<Station> converter (String data, Position currentPosition) {
        Log.v(TAG, "Started converting WAQI-Data");
        ArrayList<Station> waqiStationsList = new ArrayList<>();
        return waqiStationsList;
    }
}

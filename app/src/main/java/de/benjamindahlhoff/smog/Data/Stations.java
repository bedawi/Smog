package de.benjamindahlhoff.smog.Data;

import android.content.Context;
import android.graphics.Color;
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

public class Stations {
    public final static String TAG = Stations.class.getSimpleName();
    ArrayList<Station> mStations = new ArrayList<>();
    private Position mCurrentPosition;
    private Context mContext;


    public Stations(Context context) {
        mContext = context;
    }


    public void setStations(ArrayList<Station> stations) {
        this.clear();
        mStations = stations;
    }

    public void clear() {
        mStations.clear();
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

    /**
     * Calculate the mean values from the data the station holds
     * and also calculate the Air Quality Index (0 if not applicable)
     * and store the corresponding color.
     */
    public void calculateMeanValues() {
        AQI mAQI = new AQI(mContext);

        for (int i = 0; i < mStations.size(); i++) {
            // PM10
            if (mStations.get(i).getPM10Values().size() > 0) {
                double means_pm10 = means(mStations.get(i).getPM10Values());
                int pm10AQI = mAQI.calculateIndex(means_pm10, "PM10");
                Measurement mPM10 = new Measurement("PM10", means_pm10, "µg/m3", pm10AQI, mAQI.getColor(pm10AQI));
                mStations.get(i).addMeasurement(mPM10);
            }

            // PM2.5
            if (mStations.get(i).getPM25Values().size() > 0) {
                double means_pm25 = means(mStations.get(i).getPM25Values());
                int pm25AQI = mAQI.calculateIndex(means_pm25, "PM25");
                Measurement mPM25 = new Measurement("PM25", means_pm25, "µg/m3", pm25AQI, mAQI.getColor(pm25AQI));
                mStations.get(i).addMeasurement(mPM25);
            }

            // Temperature:
            if (mStations.get(i).getTemperature().size() > 0) {
                double means_temperature = means(mStations.get(i).getTemperature());
                Measurement mTemp = new Measurement("TEMP", means_temperature, "°C", 0, Color.parseColor("#00796B"));
                mStations.get(i).addMeasurement(mTemp);
            }


            // Humidity:
            if (mStations.get(i).getHumidity().size() > 0) {
                double means_humidity = means(mStations.get(i).getHumidity());
                Measurement mHUM = new Measurement("HUMIDITY", means_humidity, "%", 0, Color.parseColor("#00796B"));
                mStations.get(i).addMeasurement(mHUM);
            }
        }
    }

    /**
     * Calculate the mean value from a list of double values
     * (Yes this can be done easier with lambdas, but backwards compatibility of this app
     * prevents us from using them.
     * @param list      List of doubles
     * @return          result as double
     */
    private double means (List<Double> list) {
        double sum = 0;
        for (int i = 0; i < list.size(); i++) {
            sum = sum + list.get(i);
        }
        if (list.size() != 0) {
            return sum / list.size();
        }
        return sum;
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

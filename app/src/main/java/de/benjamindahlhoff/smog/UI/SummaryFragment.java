package de.benjamindahlhoff.smog.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.R;

import static de.benjamindahlhoff.smog.Data.Interpreter_WAQI.TAG;
import static de.benjamindahlhoff.smog.UI.MainActivity.FEINSTAUB_STATIONS;

/**
 * Created by benjamin on 11.03.17.
 */

public class SummaryFragment extends Fragment {
    ArrayList<Station> mStations = new ArrayList<>();
    TextView mQualityTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, "Fragment Summary OnCreateView");

        mStations = getArguments().getParcelableArrayList(FEINSTAUB_STATIONS);
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "Fragment Summary OnStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "Fragment Summary OnResume");

        int dAQI = getDominantAQI(10);


        mQualityTextView = (TextView) getView().findViewById(R.id.textQuality);
        mQualityTextView.setText(getQuality(10));
    }


    /**
     * Returns the dominant AQI from measurements of all Stations within given distance
     * @param distance in KM
     * @return AQI representation
     */
    private int getDominantAQI (int distance) {
        Log.v(TAG, "Calculating Distance");
        for (int i=0; i<mStations.size(); i++) {
            if (mStations.get(i).getDistance() <= distance) {
                Log.v(TAG, "Station within distance");
            }
        }
        return 12;
    }

    /**
     * Method returns the air quality as a string interpretation
     * @param AQI dominant AQI (calculated from relevant stations)
     * @return String like "Good" or "Average"
     */
    private String getQuality (int AQI) {
        Log.v(TAG, "Inside getQuality");
        String airQualityString = getActivity().getString(R.string.quality_good);

        return airQualityString;
    }
}

package de.benjamindahlhoff.smog.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.benjamindahlhoff.smog.Data.AQI;
import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.Data.Stations;
import de.benjamindahlhoff.smog.R;
import de.benjamindahlhoff.smog.UI.MainActivity;

/**
 * StationsAdapter show a vertical list of all weather stations
 * @author Benjamin Dahlhoff
 * @version 2017-02-10
 * Created by Benjamin Dahlhoff on 04.02.17.
 * Please refer to for a short explanation: https://benjamindahlhoff.de/2017/02/10/recyclerviews-at-work/
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationsViewHolder> {

    public final static String TAG = StationsAdapter.class.getSimpleName();
    private static List<Station> mStations = new ArrayList<>();
    private Context mContext;
    private static RecyclerView measurementsList;


    public class StationsViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView mStationInfoView;
        private MeasurementsAdapter mMeasurementsAdapter;

        /**
         * Constructor of inner class StationsViewHolder
         * @param itemView
         */
        public StationsViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            mStationInfoView = (TextView) itemView.findViewById(R.id.stationInfoText);
            itemView.setOnClickListener(this);
            measurementsList = (RecyclerView) itemView.findViewById(R.id.
                    measurementsListRecyclerView);
            measurementsList.setLayoutManager(new LinearLayoutManager(context,
                    LinearLayoutManager.HORIZONTAL, false));
            mMeasurementsAdapter = new MeasurementsAdapter();
            measurementsList.setAdapter(mMeasurementsAdapter);

        }

        /**
         * Bind weather station to view
         * @param station is a class containing data of one weather station
         */
        public void bindStation (Station station) {
            AQI qualityIndex = new AQI(mContext);
            int aqi = qualityIndex.calculateIndex(station.getValueFor("PM10"), "PM10");
            mStationInfoView.setText(String.format(mContext.getString(R.string.stationinfo)
                            + " %s Km, "
                            + mContext.getString(R.string.station_id)
                            + " %s. "
                            + "AQI for PM10 is: %s",
                    station.getDistance(),
                    station.getLocationId(),
                    aqi));
            /**
             * Never forget this line: It took me three days to find out what was wrong with
             * my program. If adapter.notifyDataSetChanged is not called, the list will show
             * wrong measurements when scrolling up in the primary RecyclerView.
             */
            mMeasurementsAdapter.notifyDataSetChanged();
        }

        /**
         * What shall be done when item in vertical list is clicked
         * @param v
         */
        @Override
        public void onClick(View v) {
            // Nothing happens here .... yet :-P
        }
    }

    /**
     * Constructor of StationsAdapter
     * @param context   Context (from StationsListActivity)
     * @param stations  Custom class holding data of one weather station
     */
    public StationsAdapter(Context context, ArrayList<Station> stations) {
        mStations.addAll(stations);
        mContext = context;
    }

    /**
     * Will be executed when view holder is created. In here the content of the RecyclerView
     * as defined in stations_list_item.xml is created (inflated).
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public StationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stations_list_item, parent, false);
        StationsViewHolder viewHolder = new StationsViewHolder(view);
        return viewHolder;
    }

    /**
     * When binding the view holder define the data show inside of it
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(StationsViewHolder holder, int position) {
        // Bind the station to holder
        holder.bindStation(mStations.get(position));

        // Prepare data to hand over to inner adapter/holder
        String[] name = new String[mStations.get(position).getMeasurements().size()];
        double[] value = new double[mStations.get(position).getMeasurements().size()];
        String[] units = new String[mStations.get(position).getMeasurements().size()];
        int[] color = new int[mStations.get(position).getMeasurements().size()];

        for (int i=0; i<mStations.get(position).getMeasurements().size(); i++) {
            name[i] = mStations.get(position).getMeasurements().get(i).getName();
            value[i] = mStations.get(position).getMeasurements().get(i).getValue();
            units[i] = mStations.get(position).getMeasurements().get(i).getUnits();
            color[i] = mStations.get(position).getMeasurements().get(i).getColorInterpretation();
        }

        // bind the inner measurement adapter to this holder:

        // holder.mMeasurementsAdapter.setMeasurements(mStations.get(position).getMeasurements());
        holder.mMeasurementsAdapter.setMeasurements(name, value, units, color);
    }

    /**
     * determine how many items are in this list
     * @return
     */
    @Override
    public int getItemCount() {
        return mStations.size();
    }


}

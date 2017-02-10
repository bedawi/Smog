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

import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.Data.Stations;
import de.benjamindahlhoff.smog.R;
import de.benjamindahlhoff.smog.UI.MainActivity;

/**
 * Created by Benjamin Dahlhoff on 04.02.17.
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationsViewHolder> {

    public final static String TAG = StationsAdapter.class.getSimpleName();
    private static List<Station> mStations = new ArrayList<>();
    private Context mContext;
    private static RecyclerView measurementsList;

    public class StationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mStationInfoView;
        private MeasurementsAdapter mMeasurementsAdapter;


        public StationsViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            mStationInfoView = (TextView) itemView.findViewById(R.id.stationInfoText);
            itemView.setOnClickListener(this);
            measurementsList = (RecyclerView) itemView.findViewById(R.id.measurementsListRecyclerView);
            measurementsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            mMeasurementsAdapter = new MeasurementsAdapter();
            measurementsList.setAdapter(mMeasurementsAdapter);

        }

        public void bindStation (Station station) {
            mStationInfoView.setText(String.format(mContext.getString(R.string.stationinfo)
                            + " %s Km, "
                            + mContext.getString(R.string.station_id)
                            + " %s",
                    station.getDistance(),
                    station.getLocationId()));
            /**
             * Never forget this line: It took me three days to find out what was wrong with
             * my program. If adapter.notifyDataSetChanged is not called, the list will show
             * wrong measurements when scrolling up in the primary RecyclerView.
             */
            mMeasurementsAdapter.notifyDataSetChanged();
        }

        @Override
        public void onClick(View v) {
            // Nothing happens here .... yet :-P
        }
    }

    public StationsAdapter(Context context, ArrayList<Station> stations) {
        mStations.addAll(stations);
        mContext = context;
    }

    @Override
    public StationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stations_list_item, parent, false);
        StationsViewHolder viewHolder = new StationsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StationsViewHolder holder, int position) {
        holder.bindStation(mStations.get(position));
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
        // holder.mMeasurementsAdapter.setMeasurements(mStations.get(position).getMeasurements());
        holder.mMeasurementsAdapter.setMeasurements(name, value, units, color);
        //Log.v(TAG, "Station:"+ mStations.get(position).getLocationId() +" PM10:"+mStations.get(position).getMeasurements().get(0).getValue());

    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }


}

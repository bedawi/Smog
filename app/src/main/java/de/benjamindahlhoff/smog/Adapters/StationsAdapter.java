package de.benjamindahlhoff.smog.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.R;

/**
 * Created by Benjamin Dahlhoff on 04.02.17.
 */

public class StationsAdapter extends RecyclerView.Adapter<StationsAdapter.StationsViewHolder> {

    List<Station> mStations = new ArrayList<>();
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
        }

        @Override
        public void onClick(View v) {
            // Nothing happens here .... yet :-P
        }
    }

    public StationsAdapter(Context context, ArrayList<Station> stations) {
        mStations = stations;
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
        holder.mMeasurementsAdapter.setMeasurements(mStations.get(position).getMeasurements());
// second adapter here?
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }


}

package de.benjamindahlhoff.smog.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.benjamindahlhoff.smog.Data.FeinstaubStation;
import de.benjamindahlhoff.smog.Data.FeinstaubStations;
import de.benjamindahlhoff.smog.R;

/**
 * Created by Benjamin Dahlhoff on 04.02.17.
 */

public class FeinstaubStationsAdapter extends RecyclerView.Adapter<FeinstaubStationsAdapter.FeinstaubStationsViewHolder> {
    ArrayList<FeinstaubStation> mStations = new ArrayList<>();
    private Context mContext;

    public FeinstaubStationsAdapter(Context context, ArrayList<FeinstaubStation> stations) {
        mStations = stations;
        mContext = context;
    }

    @Override
    public FeinstaubStationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feinstaubstations_list_item, parent, false);
        FeinstaubStationsViewHolder viewHolder = new FeinstaubStationsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FeinstaubStationsAdapter.FeinstaubStationsViewHolder holder, int position) {
        holder.bindFeinstaubStation(mStations.get(position));
    }

    @Override
    public int getItemCount() {
        return mStations.size();
    }

    public class FeinstaubStationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mPM25ValueView;
        public RelativeLayout mPM25box;
        public TextView mPM10ValueView;
        public RelativeLayout mPM10box;
        public TextView mTemperatureValueView;
        public TextView mHumidityValueView;
        public TextView mStationInfoView;


        public FeinstaubStationsViewHolder (View itemView) {
            super(itemView);

            mPM25ValueView = (TextView) itemView.findViewById(R.id.pm25ValueView);
            mPM25box = (RelativeLayout) itemView.findViewById(R.id.pm25Box);
            mPM10ValueView = (TextView) itemView.findViewById(R.id.pm10ValueView);
            mPM10box = (RelativeLayout) itemView.findViewById(R.id.pm10Box);
            mTemperatureValueView = (TextView) itemView.findViewById(R.id.temperatureValueView);
            mHumidityValueView = (TextView) itemView.findViewById(R.id.HumidityValueView);
            mStationInfoView = (TextView) itemView.findViewById(R.id.stationInfoText);


            itemView.setOnClickListener(this);

        }

        public void bindFeinstaubStation (FeinstaubStation feinstaubStation) {
            mPM25ValueView.setText(String.valueOf(feinstaubStation.getPM25Mean()));
            mPM25box.setBackgroundColor(feinstaubStation.getColorforValue(feinstaubStation.getPM25Mean()));
            mPM10ValueView.setText(String.valueOf(feinstaubStation.getPM10Mean()));
            mPM10box.setBackgroundColor(feinstaubStation.getColorforValue(feinstaubStation.getPM10Mean()));

            // Fix me when temperature and humidity mean values are implemented!
            mTemperatureValueView.setText(String.valueOf(feinstaubStation.getTemperature()));
            mHumidityValueView.setText(String.valueOf(feinstaubStation.getHumidity()));
            mStationInfoView.setText(String.format(mContext.getString(R.string.stationinfo)
                    + " %s Km, "
                    + mContext.getString(R.string.station_id)
                    + " %s",
                    feinstaubStation.getDistance(), 
                    feinstaubStation.getLocationId()));
        }

        @Override
        public void onClick(View v) {
            // Nothing happens here .... yet :-P
        }
    }
}

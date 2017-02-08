package de.benjamindahlhoff.smog.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import de.benjamindahlhoff.smog.Data.Measurement;
import de.benjamindahlhoff.smog.R;

/**
 * Created by benjamin on 07.02.17.
 */

public class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.MeasurementsViewHolder> {
    private final List<Measurement> mMeasurements = new ArrayList<>();

    public void setMeasurements(List<Measurement> measurements) {
        mMeasurements.clear();
        mMeasurements.addAll(measurements);
    }

    @Override
    public MeasurementsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        MeasurementsViewHolder viewHolder = new MeasurementsViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MeasurementsViewHolder holder, int position) {
        holder.bindMeasurement(mMeasurements.get(position));
    }

    @Override
    public int getItemCount() {
        return mMeasurements.size();
    }


    public class MeasurementsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mNameView;
        public TextView mValueView;
        public TextView mUnitsView;
        public RelativeLayout mBoxLayout;

        public MeasurementsViewHolder (View itemView) {
            super(itemView);
            mNameView = (TextView) itemView.findViewById(R.id.measurementTypeTextView);
            mValueView = (TextView) itemView.findViewById(R.id.measurementValueView);
            mUnitsView = (TextView) itemView.findViewById(R.id.measurementUnitsView);
            mBoxLayout = (RelativeLayout) itemView.findViewById(R.id.measurementBox);
            //itemView.setOnClickListener(this);
        }

        public void bindMeasurement (Measurement measurement) {
            mNameView.setText(measurement.getName());
            mValueView.setText(String.valueOf(measurement.getValue()));
            mUnitsView.setText(measurement.getUnits());
            mBoxLayout.setBackgroundColor(measurement.getColorInterpretation());
        }

        @Override
        public void onClick(View v) {
            // Nothing happens here .... yet :-P
        }
    }



}

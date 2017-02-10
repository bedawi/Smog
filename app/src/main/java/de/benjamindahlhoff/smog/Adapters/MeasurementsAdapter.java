package de.benjamindahlhoff.smog.Adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.benjamindahlhoff.smog.R;


/**
 * Created by Benjamin Dahlhoff on 07.02.17.
 */

public class MeasurementsAdapter extends RecyclerView.Adapter<MeasurementsAdapter.MeasurementsViewHolder> {
    public final static String TAG = MeasurementsAdapter.class.getSimpleName();
    //private static List<Measurement> mMeasurements;
    private String[] mNames;
    private double[] mValues;
    private String[] mUnits;
    private int[] mColors;

    public void setMeasurements(String[] names, double[] values, String[] units, int[] colors) {
        mNames = names;
        mValues = values;
        mUnits = units;
        mColors = colors;
    }

    @Override
    public MeasurementsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        MeasurementsViewHolder viewHolder = new MeasurementsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MeasurementsViewHolder holder, int position) {
        holder.bindMeasurement(mNames[position], mValues[position], mUnits[position], mColors[position]);
    }

    @Override
    public int getItemCount() {
        return mNames.length;
    }


    public class MeasurementsViewHolder extends RecyclerView.ViewHolder {
        private final TextView mNameView;
        private final TextView mValueView;
        private final TextView mUnitsView;
        private final RelativeLayout mBoxLayout;

        public MeasurementsViewHolder (View itemView) {
            super(itemView);
            mNameView = (TextView) itemView.findViewById(R.id.measurementTypeTextView);
            mValueView = (TextView) itemView.findViewById(R.id.measurementValueView);
            mUnitsView = (TextView) itemView.findViewById(R.id.measurementUnitsView);
            mBoxLayout = (RelativeLayout) itemView.findViewById(R.id.measurementBox);
        }

        public void bindMeasurement (String name, double value, String unit, int color) {
            /*
            * Changed this while searching for bugs:
            * Not sure if I want to revert it to this again.
            * Need to discuss this with other programmers.
            mNameView.setText(measurement.getName());
            mValueView.setText(String.valueOf(measurement.getValue()));
            mUnitsView.setText(measurement.getUnits());
            mBoxLayout.setBackgroundColor(measurement.getColorInterpretation());
            */
            mNameView.setText(name);
            mValueView.setText(String.valueOf(Math.round(value)));
            mUnitsView.setText(unit);
            mBoxLayout.setBackgroundColor(color);
            // Log.v(TAG, name + " " + value);
        }
    }
}

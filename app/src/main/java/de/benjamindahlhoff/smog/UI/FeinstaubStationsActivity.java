package de.benjamindahlhoff.smog.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.benjamindahlhoff.smog.Adapters.FeinstaubStationsAdapter;
import de.benjamindahlhoff.smog.Data.FeinstaubStation;
import de.benjamindahlhoff.smog.Data.FeinstaubStations;
import de.benjamindahlhoff.smog.R;

/**
 * Created by benjamin on 04.02.17.
 */

public class FeinstaubStationsActivity extends AppCompatActivity {
    ArrayList<FeinstaubStation> mStations = new ArrayList<>();
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feinstaub_stations);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mStations = intent.getParcelableArrayListExtra(MainActivity.FEINSTAUB_STATIONS);
        //
        // mStations = new ArrayList<FeinstaubStation>(parcelables);

        FeinstaubStationsAdapter adapter = new FeinstaubStationsAdapter(this, mStations);
        mRecyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);
    }
}

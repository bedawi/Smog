package de.benjamindahlhoff.smog.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.benjamindahlhoff.smog.Adapters.StationsAdapter;
import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.R;

/**
 * Created by benjamin on 04.02.17.
 */

public class StationsListActivity extends AppCompatActivity {
    ArrayList<Station> mStations = new ArrayList<>();
    @BindView(R.id.stationsListRecylerView) RecyclerView mStationsListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mStations = intent.getParcelableArrayListExtra(MainActivity.FEINSTAUB_STATIONS);

        StationsAdapter stationsAdapter = new StationsAdapter(this, mStations);
        mStationsListRecyclerView.setAdapter(stationsAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mStationsListRecyclerView.setLayoutManager(layoutManager);

        mStationsListRecyclerView.setHasFixedSize(true);
    }
}

package de.benjamindahlhoff.smog.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import de.benjamindahlhoff.smog.Adapters.StationsAdapter;
import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.R;

import static de.benjamindahlhoff.smog.UI.MainActivity.FEINSTAUB_STATIONS;

/**
 * This fragment shows the list of stations.
 * It replaces "activity_stations"
 * Created by Benjamin Dahlhoff on 11.03.17.
 * @author Benjamin Dahlhoff
 */

public class StationslistFragment extends Fragment {
    ArrayList<Station> mStations = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mStations = getArguments().getParcelableArrayList(FEINSTAUB_STATIONS);
        View view = inflater.inflate(R.layout.fragment_stationslist, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.stationsListRecylerView);
        StationsAdapter stationsAdapter = new StationsAdapter(getActivity(), mStations);
        recyclerView.setAdapter(stationsAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }
}

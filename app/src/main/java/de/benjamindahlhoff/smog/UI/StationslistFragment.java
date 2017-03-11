package de.benjamindahlhoff.smog.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.benjamindahlhoff.smog.R;

/**
 * Created by benjamin on 11.03.17.
 */

public class StationslistFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stationslist, container, false);
        return view;
    }
}

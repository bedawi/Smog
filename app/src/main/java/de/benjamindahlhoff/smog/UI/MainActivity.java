package de.benjamindahlhoff.smog.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import de.benjamindahlhoff.smog.Data.Pollution;
import de.benjamindahlhoff.smog.R;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();

    private Pollution mPollution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}

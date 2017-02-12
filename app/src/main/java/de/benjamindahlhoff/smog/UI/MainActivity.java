package de.benjamindahlhoff.smog.UI;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.benjamindahlhoff.smog.Data.Interpreter_Luftdaten;
import de.benjamindahlhoff.smog.Data.Interpreter_WAQI;
import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.Data.Stations;
import de.benjamindahlhoff.smog.Data.Position;
import de.benjamindahlhoff.smog.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    public static final String FEINSTAUB_STATIONS = "FEINSTAUB_STATIONS";
    private static final String KEY_STATIONS = "KEY_STATIONS";

    // Our current position is stored in
    private Position mCurrentPosition = new Position();


    // Feinstaub
    private Stations mStations = new Stations();


    // Buttons
    @BindView(R.id.reloadButton) Button mReloadButton;
    @BindView(R.id.showStationsButton) Button mStationsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mCurrentPosition.getLongitude() == 0) {
            getCoarsePosition();
        }

        if (mStations.getStations().size() == 0) {
            // Quick and dirty. Fix later.
            // Necessary because there are not enough stations avaiable and API will not return
            // anything with precise coordinates given.
            double lat = mCurrentPosition.getLatitude();
            int latInt = (int) lat;

            double longi = mCurrentPosition.getLongitude();
            int longInt = (int) longi;

            //pullFromServer("http://api.openweathermap.org/pollution/v1/co/" + latInt + "," + longInt + "/current.json?appid="+getString(R.string.openweathermap), "CO_from_OpenWeatherMap");
            pullFromServer("http://api.luftdaten.info/static/v1/data.json", "Feinstaub_from_LuftdatenInfo");

            pullFromServer("https://api.waqi.info/feed/here/?token="+getString(R.string.aqi_open_data), "WAQI");
        }

        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadButton.setVisibility(View.INVISIBLE);
                //pullFromServer("http://api.openweathermap.org/pollution/v1/co/" + mCurrentPosition.getLatitude()+ "," + mCurrentPosition.getLongitude()+ "/current.json?appid="+getString(R.string.openweathermap), "CO_from_OpenWeatherMap");
                pullFromServer("http://api.luftdaten.info/static/v1/data.json", "Feinstaub_from_LuftdatenInfo");
                mReloadButton.setVisibility(View.VISIBLE);


            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_STATIONS, mStations.getStations());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Station> stations = new ArrayList<>();
        stations = (savedInstanceState.getParcelableArrayList(KEY_STATIONS));
        mStations.setStations(stations);
        //refreshActivity();
    }

    private void getCoarsePosition() {
        // Checking if permission to get coarse location was granted.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If not: Ask for it!
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // If permission was granted, get the Position
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null) {
                mCurrentPosition.setLatitude(lastKnownLocation.getLatitude());
                mCurrentPosition.setLongitude(lastKnownLocation.getLongitude());
                mStations.setCurrentPosition(mCurrentPosition);

            }
        }
    }

    private void pullFromServer(String url, final String service) {
        Log.v(TAG, "URL: "+ url);
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertUserAboutError();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            extractDataForService(service, jsonData);
                        } else {
                            //alertUserAboutError();
                            Log.e(TAG, "Error: Received invalid data!");
                        }
                    } catch (IOException e) {
                        Log.e(TAG, getString(R.string.exception_caught), e);
                    } catch (JSONException e) {
                        Log.e(TAG, getString(R.string.exception_caught), e);
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Network unavailable", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    private void extractDataForService(String service, String data) throws JSONException {
        if (service == "CO_from_OpenWeatherMap") {


        }
        if (service == "Feinstaub_from_LuftdatenInfo") {
            // Lets loop through the data and add the stations to the List.
            mStations.clear();
            Interpreter_Luftdaten luftdaten = new Interpreter_Luftdaten();

            mStations.injectStations(luftdaten.converter(new JSONArray(data), mCurrentPosition));
            mStations.sortByDistance();
            mStations.calculateMeans();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshActivity();
                }
            });
        } // *** END if (service == "Feinstaub_from_LuftdatenInfo")

        if (service == "WAQI") {
            Interpreter_WAQI waqi = new Interpreter_WAQI();
            ArrayList<Station> temporaryTestListDeleteMe = new ArrayList<>();
            temporaryTestListDeleteMe.addAll(waqi.converter(data, mCurrentPosition));
            Log.v(TAG, "Back from WAQI interpretation");
        }

    }

    private void refreshActivity() {

    }

    @OnClick(R.id.showStationsButton)
    public void startFeinstaubStationsActivity (View view) {
        Intent intent = new Intent(this, StationsListActivity.class);
        ArrayList<Station> parcel = mStations.getStations();

        intent.putParcelableArrayListExtra(FEINSTAUB_STATIONS, parcel);
        startActivity(intent);

    }
}

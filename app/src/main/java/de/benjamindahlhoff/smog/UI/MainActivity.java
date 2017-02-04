package de.benjamindahlhoff.smog.UI;

import android.Manifest;
import android.content.Context;
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
import java.io.StreamCorruptedException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.benjamindahlhoff.smog.Data.COStation;
import de.benjamindahlhoff.smog.Data.COStations;
import de.benjamindahlhoff.smog.Data.FeinstaubStations;
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

    // Our current position is stored in
    private Position mCurrentPosition = new Position();

    // All Pollution-Data goes into the Pollution class
    //private Pollution mPollution = new Pollution();

    // CO
    private COStations mCOStations = new COStations();

    // Feinstaub
    private FeinstaubStations mFeinstaubStations = new FeinstaubStations();

    // Feinstaub Views
    @BindView(R.id.pm25ValueView) TextView mPM25ValueView;
    @BindView(R.id.pm25DistanceView) TextView mPM25DistanceView;
    @BindView(R.id.pm10ValueView) TextView mPM10ValueView;
    @BindView(R.id.pm10DistanceView) TextView mPM10DistanceView;
    @BindView(R.id.temperatureValueView) TextView mTemperatureValueView;
    @BindView(R.id.temperatureDistanceView) TextView mTemperatureDistanceView;

    // CO Views
    @BindView(R.id.valueCOButton) RelativeLayout mCOButtonLayout;
    @BindView(R.id.coValueView) TextView mCOValueView;
    @BindView(R.id.coDistanceView) TextView mCODistanceView;
    @BindView(R.id.reloadButton) Button mReloadButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getCoarsePosition();

        mReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReloadButton.setVisibility(View.INVISIBLE);
                //pullFromServer("http://api.openweathermap.org/pollution/v1/co/" + mCurrentPosition.getLatitude()+ "," + mCurrentPosition.getLongitude()+ "/current.json?appid="+getString(R.string.openweathermap), "CO_from_OpenWeatherMap");
                pullFromServer("http://api.luftdaten.info/static/v1/data.json", "Feinstaub_from_LuftdatenInfo");
                mReloadButton.setVisibility(View.VISIBLE);
            }
        });

        // Quick and dirty. Fix later.
        // Necessary because there are not enough stations avaiable and API will not return
        // anything with precise coordinates given.
        double lat = mCurrentPosition.getLatitude();
        int latInt = (int) lat;

        double longi = mCurrentPosition.getLongitude();
        int longInt = (int) longi;

        pullFromServer("http://api.openweathermap.org/pollution/v1/co/" + latInt + "," + longInt + "/current.json?appid="+getString(R.string.openweathermap), "CO_from_OpenWeatherMap");
        pullFromServer("http://api.luftdaten.info/static/v1/data.json", "Feinstaub_from_LuftdatenInfo");


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
                mFeinstaubStations.setCurrentPosition(mCurrentPosition);
                mCOStations.setCurrentPosition(mCurrentPosition);
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
            // Getting Carbon Monoxide Data from OpenWeatherMap
            JSONObject COStationData = new JSONObject(data);
            mCOStations.clear();
            mCOStations.addStation(COStationData);
            mCOStations.sortByDistance();

            // Time to bring the newly acquired data onto the screen:
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mCOValueView.setText(String.valueOf(mCOStations.getStationByIndex(0).getCarbonMonoxideMeanValue()));
                    mCODistanceView.setText(String.valueOf(mCOStations.getStationByIndex(0).getDistance()) + " "+"KM" +" "+ getString(R.string.away));
                    //Math.round(mPollution.getCarbonMonoxide().getCarbonMonoxideVolumeMixingRatio() *100.0)/100.0
                    //mCoValueView.setText(getString(R.string.mixingRatio)+": "+ String.valueOf(Math.round(mPollution.getCarbonMonoxide().getCarbonMonoxideVolumeMixingRatio()*1000000000))+ " ppb");
                    //mCoPrecisionView.setText(String.valueOf(mPollution.getCarbonMonoxide().getMeasurementPrecision()));
                    //mCoPressureView.setText(getString(R.string.pressure)+" "+ String.valueOf(mPollution.getCarbonMonoxide().getAtmosphericPressure())+" hPa");

                    // Close temporarily:
                    mCOButtonLayout.setVisibility(View.INVISIBLE);
                }
            });
        }
        if (service == "Feinstaub_from_LuftdatenInfo") {
            // Data comes as huge Array:
            JSONArray ParticulatesData = new JSONArray(data);

            // Lets loop through the data and add the stations to the List.
            mFeinstaubStations.clear();
            for (int i = 0; i<ParticulatesData.length(); i++) {
                mFeinstaubStations.addDataToStation(ParticulatesData.getJSONObject(i));
            }
            mFeinstaubStations.sortByDistance();
            mFeinstaubStations.calculateMeans();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //mP1ValueView.setText("PM10: "+ String.valueOf(mFeinstaubStations.getStationByIndex(0).getPM10Mean())+ " µg/m³");
                    mPM25ValueView.setText(String.valueOf(mFeinstaubStations.getStationByIndex(0).getPM25Mean()));
                    mPM25DistanceView.setText(String.valueOf(mFeinstaubStations.getStationByIndex(0).getDistance()) + " "+"KM" +" "+ getString(R.string.away));

                    mPM10ValueView.setText(String.valueOf(mFeinstaubStations.getStationByIndex(0).getPM10Mean()));
                    mPM10DistanceView.setText(String.valueOf(mFeinstaubStations.getStationByIndex(0).getDistance()) + " "+"KM" +" "+ getString(R.string.away));

                    mTemperatureValueView.setText(String.valueOf(mFeinstaubStations.getStationByIndex(0).getTemperature()));
                    mTemperatureDistanceView.setText(String.valueOf(mFeinstaubStations.getStationByIndex(0).getDistance()) + " "+"KM" +" "+ getString(R.string.away));

                    //mTemperatureView.setText("Temp: "+ String.valueOf(mFeinstaubStations.getStationByIndex(0).getTemperature()) +"°C");
                    //mHumidityView.setText("Humidity: "+ String.valueOf(mFeinstaubStations.getStationByIndex(0).getHumidity()));

                }
            });
        } // *** END if (service == "Feinstaub_from_LuftdatenInfo")


    }
}

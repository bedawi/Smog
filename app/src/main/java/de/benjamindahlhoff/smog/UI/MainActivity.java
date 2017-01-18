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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.benjamindahlhoff.smog.Data.CarbonMonoxideData;
import de.benjamindahlhoff.smog.Data.Pollution;
import de.benjamindahlhoff.smog.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    // All Pollution-Data goes into the Pollution class
    private Pollution mPollution = new Pollution();

    // Views for Carbonmonoxide:
    @BindView(R.id.coValueView) TextView mCoValueView;
    @BindView(R.id.coPrecisionView) TextView mCoPrecisionView;
    @BindView(R.id.coPressureView) TextView mCoPressureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getPosition();

        // Pull CO-Data (Unfortunately this provider does not offer data for precise positions)
        String latitude = "50";
        String longitude = "8";
        String apiUrl = "http://api.openweathermap.org/pollution/v1/co/" +latitude+ "," +longitude+ "/current.json?appid="+getString(R.string.openweathermap);
        // To keep things simple, every request is done through the method "pullFromServer"
        pullFromServer(apiUrl, "CO_from_OpenWeatherMap");


    }

    private void getPosition() {
        String latitude;
        String longitude;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.ACCESS_COARSE_LOCATION },
                    PERMISSION_ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            String locationProvider = LocationManager.NETWORK_PROVIDER;
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null) {
                latitude = lastKnownLocation.getLatitude();
                longitude = lastKnownLocation.getLongitude();
            }
        }
    }

    private void pullFromServer(String url, final String service) {
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
                            alertUserAboutError();
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
            JSONObject AllCOData = new JSONObject(data);
            JSONArray CODataArray = AllCOData.getJSONArray("data");
            JSONObject CODataset = CODataArray.getJSONObject(0);
            CarbonMonoxideData carbonMonoxideData = new CarbonMonoxideData(CODataset.getDouble("value"), CODataset.getDouble("pressure"), CODataset.getDouble("precision"));
            Log.v(TAG, "Pressure: " + carbonMonoxideData.getAtmosphericPressure());
            mPollution.setCarbonMonoxide(carbonMonoxideData);
        }

        // Time to bring the newly acquired data onto the screen:
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Math.round(mPollution.getCarbonMonoxide().getCarbonMonoxideVolumeMixingRatio() *100.0)/100.0
                mCoValueView.setText(getString(R.string.mixingRatio)+": "+ String.valueOf(Math.round(mPollution.getCarbonMonoxide().getCarbonMonoxideVolumeMixingRatio()*1000000000))+ " ppb");
                mCoPrecisionView.setText(String.valueOf(mPollution.getCarbonMonoxide().getMeasurementPrecision()));
                mCoPressureView.setText(getString(R.string.pressure)+" "+ String.valueOf(mPollution.getCarbonMonoxide().getAtmosphericPressure())+" hPa");
            }
        });
    }
}

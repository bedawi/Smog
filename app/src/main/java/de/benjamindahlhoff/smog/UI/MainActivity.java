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
import de.benjamindahlhoff.smog.Data.Particulates;
import de.benjamindahlhoff.smog.Data.ParticulatesData;
import de.benjamindahlhoff.smog.Data.Pollution;
import de.benjamindahlhoff.smog.Data.Position;
import de.benjamindahlhoff.smog.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.os.Build.VERSION_CODES.M;


public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    // Our current position is stored in
    private Position mCurrentPosition = new Position();

    // All Pollution-Data goes into the Pollution class
    private Pollution mPollution = new Pollution();

    // Feinstaub
    private Particulates[] mParticulates;

    // Views for Carbonmonoxide:
    @BindView(R.id.coValueView) TextView mCoValueView;
    @BindView(R.id.coPrecisionView) TextView mCoPrecisionView;
    @BindView(R.id.coPressureView) TextView mCoPressureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        getCoarsePosition();


        pullFromServer("http://api.openweathermap.org/pollution/v1/co/" + mCurrentPosition.getLatitude()+ "," + mCurrentPosition.getLongitude()+ "/current.json?appid="+getString(R.string.openweathermap), "CO_from_OpenWeatherMap");
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
        if (service == "Feinstaub_from_LuftdatenInfo") {
            // Data comes as huge Array:
            JSONArray ParticulatesData = new JSONArray(data);

            mParticulates = new Particulates[ParticulatesData.length()];

            // Lets loop through the data:
            for (int i = 0; i<ParticulatesData.length(); i++) {
                // Grab object at position i
                JSONObject ParticulatesObject = ParticulatesData.getJSONObject(i);

                // Inside every object there is another array with measurement data
                JSONArray MeasurementsArray = ParticulatesObject.getJSONArray("sensordatavalues");

                // Lets create tmpParticulatesData and fill it with the measurement data
                ParticulatesData[] tmpParticulatesData = new ParticulatesData[MeasurementsArray.length()];
                for (int j = 0; j < MeasurementsArray.length(); j++)
                {
                    JSONObject tempMeasurementsObject = MeasurementsArray.getJSONObject(j);

                    ParticulatesData particulatesData = new ParticulatesData();
                    particulatesData.setValue(tempMeasurementsObject.getDouble("value"));
                    particulatesData.setValueType(tempMeasurementsObject.getString("value_type"));

                    tmpParticulatesData[j] = particulatesData;
                }

                // what else can we get?
                JSONObject LocationObject = ParticulatesObject.getJSONObject("location");
                double tmpLatitute = LocationObject.getDouble("latitude");
                double tmpLogitude = LocationObject.getDouble("longitude");
                Position tmpPosition = new Position(tmpLatitute, tmpLogitude);

                String tmpTimestamp = ParticulatesObject.getString("timestamp");

                Particulates tmpParticulates = new Particulates(tmpTimestamp, tmpPosition, tmpParticulatesData);
                mParticulates[i] = tmpParticulates;

                Log.v(TAG, "Finished: " + i);
            }

        }


    }
}

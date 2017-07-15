package de.benjamindahlhoff.smog.UI;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.benjamindahlhoff.smog.Data.Interpreter_Luftdaten;
import de.benjamindahlhoff.smog.Data.Interpreter_WAQI;
import de.benjamindahlhoff.smog.Data.Position;
import de.benjamindahlhoff.smog.Data.Station;
import de.benjamindahlhoff.smog.Data.Stations;
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
    public static final String STATIONSLIST_FRAGMENT = "stationslist_fragment";
    public static final String SUMMARY_FRAGMENT = "summary_fragment";

    public static Context mContext;

    // Our current position is stored in
    private Position mCurrentPosition = new Position();


    // Weatherstations
    private Stations mStations = new Stations(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        // ButterKnife.bind(this);

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

            loadFromStations("All");
        }

        SummaryFragment savedFragment = (SummaryFragment) getSupportFragmentManager().findFragmentByTag(SUMMARY_FRAGMENT);

        if (savedFragment == null) {
            ArrayList<Station> parcel = mStations.getStations();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(FEINSTAUB_STATIONS, parcel);

            SummaryFragment fragment = new SummaryFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.placeholder, fragment, SUMMARY_FRAGMENT);
            fragmentTransaction.commit();
        }
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

    /**
     * Trigger loading data from weather services
     * @param whichStations ["All"\Name]
     */
    private void loadFromStations (String whichStations) {
        if (whichStations == "All") {
            Log.v(TAG, "Loading from all Stations");
            pullFromServer("http://api.luftdaten.info/static/v1/data.json", "Feinstaub_from_LuftdatenInfo");
            pullFromServer("https://api.waqi.info/feed/geo:"
                    + mCurrentPosition.getLatitude()
                    + ";"
                    + mCurrentPosition.getLongitude()
                    + "/?token="
                    + getString(R.string.aqi_open_data), "WAQI");
            //pullFromServer("http://api.openweathermap.org/pollution/v1/co/" + latInt + "," + longInt + "/current.json?appid="+getString(R.string.openweathermap), "CO_from_OpenWeatherMap");
        }
    }

    /**
     * Network access, loading data from single service provider
     * @param url       API url
     * @param service   Name of service
     */
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

    /**
     * Extract data from server's response
     * @param service   Name of service
     * @param data      Data
     * @throws JSONException
     */
    private void extractDataForService(String service, String data) throws JSONException {
        if (service == "CO_from_OpenWeatherMap") {

        }
        if (service == "Feinstaub_from_LuftdatenInfo") {
            // Lets loop through the data and add the stations to the List.
            mStations.clear();
            Interpreter_Luftdaten luftdaten = new Interpreter_Luftdaten();

            mStations.injectStations(luftdaten.converter(new JSONArray(data), mCurrentPosition));
            mStations.sortByDistance();
            mStations.calculateMeanValues();

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
            temporaryTestListDeleteMe.addAll(waqi.converter(new JSONObject(data), mCurrentPosition));
            Log.v(TAG, "Back from WAQI interpretation");
        }

    }

    /**
     * Checks if Network is available
     * @return  bool: [true|false]
     */
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


    private void refreshActivity() {

        //int entries = getSupportFragmentManager().getBackStackEntryCount();
        //FragmentManager.BackStackEntry backStackEntryAt = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount());
        //int currentFragment = backStackEntryAt.getId();
        //Fragment fragment = getSupportFragmentManager().findFragmentById(currentFragment);
        /**
         * // Reload current fragment
         Fragment frg = null;
         frg = getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
         final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
         ft.detach(frg);
         ft.attach(frg);
         ft.commit();
         */

        Log.v(TAG, "Restarting Fragment");

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(SUMMARY_FRAGMENT);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.detach(fragment);
        fragmentTransaction.attach(fragment);
        fragmentTransaction.commit();

        /*
        //SummaryFragment fragment = new SummaryFragment();

        ArrayList<Station> parcel = mStations.getStations();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FEINSTAUB_STATIONS, parcel);
        fragment.setArguments(bundle);

        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.placeholder, fragment, SUMMARY_FRAGMENT);
        fragmentTransaction.commit(); */


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_stations) {
            ArrayList<Station> parcel = mStations.getStations();

            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(FEINSTAUB_STATIONS, parcel);

            StationslistFragment fragment = new StationslistFragment();
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.placeholder, fragment, STATIONSLIST_FRAGMENT);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }

        if (id == R.id.action_reload) {
            loadFromStations("All");
        }
        return super.onOptionsItemSelected(item);
    }
}

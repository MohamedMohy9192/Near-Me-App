package com.androideradev.www.nearme;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.androideradev.www.nearme.adapter.PlaceTypeAdapter;
import com.androideradev.www.nearme.utilities.PermissionUtilities;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        PlaceTypeAdapter.OnPlaceTypeItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private static final int REQUEST_CHECK_SETTINGS = 15;
    private static final String LAT_KEY = "lat_key";
    private static final String LON_KEY = "lon_key";

    public static final String PLACE_TYPE_NAME_VALUE = "place_type_value";
    public static final String LAT_EXTRA_KEY = "lat_extra_key";
    public static final String LON_EXTRA_KEY = "lon_extra_key";

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;

    // A default location (Sydney, Australia) to use when location permission is
    // not granted.
    public static final double mDefaultLatitude = -33.8523341;
    public static final double mDefaultLongitude = 151.2106085;

    private double mLatitude;
    private double mLongitude;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rv_place_type)
    RecyclerView mRecyclerView;

    private PlaceTypeAdapter mPlaceTypeAdapter;

    private boolean mLocationPermissionIsGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceTypeAdapter = new PlaceTypeAdapter(this);
        mRecyclerView.setAdapter(mPlaceTypeAdapter);

        String[] placeTypeNameArray = getResources().getStringArray(R.array.names_of_places);
        mPlaceTypeAdapter.setPlaceTypeList(placeTypeNameArray);
//        List<String> placeTypeNameList = new ArrayList<>(Arrays.asList(placeTypeNameArray));


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location currentDeviceLocation = locationResult.getLastLocation();

                double lat = currentDeviceLocation.getLatitude();
                double lon = currentDeviceLocation.getLongitude();

                mLatitude = lat;
                mLongitude = lon;

                SharedPreferences sharedPreferences =
                        MainActivity.this.getPreferences(Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(LAT_KEY, String.valueOf(lat));
                editor.putString(LON_KEY, String.valueOf(lon));

                editor.apply();

                Log.i(LOG_TAG, "onLocationResult: Latitude: " + lat);
                Log.i(LOG_TAG, "onLocationResult: Longitude: " + lon);

            }


        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /*
             If android version greater than or equal to android marshmallow,
             ask the user to grant the location permission at runtime.
              */
            requestLocationPermission();
        } else {
            // if android version lower than android marshmallow, access the device location immediately.
            setupLocationRequestSettings();
        }


    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            PermissionUtilities.requestFineLocationPermission(this);
//            }
        } else {
            // Permission has already been granted
            setupLocationRequestSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtilities.REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted.
                    setupLocationRequestSettings();
                } else {
                    // permission denied, check if user has selected don't ask again
                    if (PermissionUtilities.shouldShowPermissionExplanationDialog(this)) {
                        PermissionUtilities.showPermissionExplanationDialog(this);
                    } else {
                        mLatitude = mDefaultLatitude;
                        mLongitude = mDefaultLongitude;
                    }

                }
            }

        }
    }

    private void getDeviceLastLocation() {
        SharedPreferences sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        String lat = sharedPreferences.getString(LAT_KEY, String.valueOf(mDefaultLatitude));
        String lon = sharedPreferences.getString(LON_KEY, String.valueOf(mDefaultLongitude));

        mLatitude = Double.valueOf(lat);
        mLongitude = Double.valueOf(lon);

    }

    private void startLocationUpdates() {
        try {

            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null);
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "startLocationUpdates: " + e.getMessage());
        }
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void setupLocationRequestSettings() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                startLocationUpdates();

            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {

                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this,
                                REQUEST_CHECK_SETTINGS);

                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case MainActivity.RESULT_OK:
                        // All required changes were successfully made
                        startLocationUpdates();
                        break;
                    case MainActivity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // get last location if exist or default if not.
                        getDeviceLastLocation();
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    @Override
    public void onPlaceTypeItemClicked(int placeTypeItemPosition) {
        String[] placeTypeValueList = getResources().getStringArray(R.array.values_of_places);

        String placeTypeNameValue = placeTypeValueList[placeTypeItemPosition];

        Intent openPlacesTypeContentActivity =
                new Intent(this, PlaceTypeContentActivity.class);

        openPlacesTypeContentActivity.putExtra(PLACE_TYPE_NAME_VALUE, placeTypeNameValue);
        openPlacesTypeContentActivity.putExtra(LAT_EXTRA_KEY, mLatitude);
        openPlacesTypeContentActivity.putExtra(LON_EXTRA_KEY, mLongitude);

        startActivity(openPlacesTypeContentActivity);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_my_places) {
            Intent intent = new Intent(this, MyPlacesActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

package com.androideradev.www.nearme;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();

    private GoogleMap mMap;

    public static final String MAPS_LAT_INTENT_EXTRA = "lat";
    public static final String MAPS_LNG_INTENT_EXTRA = "lng";
    public static final String MAPS_PLACE_NAME_INTENT_EXTRA = "place_name";
    public static final String MAPS_PLACE_TYPE_INTENT_EXTRA = "place_type";

    private double mLat;
    private double mLng;
    private String mPlaceName;
    private String mPlaceType;

    private static final double mDefaultLatitude = -33.8523341;
    private static final double mDefaultLongitude = 151.2106085;

    private static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Intent intent = getIntent();
        if (intent.hasExtra(MAPS_LAT_INTENT_EXTRA)) {
            mLat = intent.getDoubleExtra(MAPS_LAT_INTENT_EXTRA, mDefaultLatitude);
        }
        if (intent.hasExtra(MAPS_LNG_INTENT_EXTRA)) {
            mLng = intent.getDoubleExtra(MAPS_LNG_INTENT_EXTRA, mDefaultLongitude);
        }
        if (intent.hasExtra(MAPS_PLACE_NAME_INTENT_EXTRA)) {
            mPlaceName = intent.getStringExtra(MAPS_PLACE_NAME_INTENT_EXTRA);
        }
        if (intent.hasExtra(MAPS_PLACE_TYPE_INTENT_EXTRA)) {
            mPlaceType = intent.getStringExtra(MAPS_PLACE_TYPE_INTENT_EXTRA);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng placeLatLng = new LatLng(mLat, mLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeLatLng, DEFAULT_ZOOM));

        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(placeLatLng)
                .title(mPlaceName)
                .snippet(mPlaceType));

        marker.showInfoWindow();

    }


}

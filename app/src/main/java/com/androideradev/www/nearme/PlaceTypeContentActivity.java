package com.androideradev.www.nearme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androideradev.www.nearme.adapter.PlaceTypeContentAdapter;
import com.androideradev.www.nearme.model.Place;
import com.androideradev.www.nearme.utilities.JsonUtilities;
import com.androideradev.www.nearme.utilities.NetworkUtilities;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceTypeContentActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Place>>,
        PlaceTypeContentAdapter.OnPlaceTypeContentItemClickListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 22;
    public static final String PLACE_ID_INTENT_EXTRA = "place_id_extra";

    private String mPlaceTypeNameValue;
    private double mLatitude;
    private double mLongitude;

    private PlaceTypeContentAdapter mTypeContentAdapter;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingProgressBar;
    @BindView(R.id.rv_type_content)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_type_content);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(MainActivity.PLACE_TYPE_NAME_VALUE)) {
                mPlaceTypeNameValue = intent.getStringExtra(MainActivity.PLACE_TYPE_NAME_VALUE);
            }
            if (intent.hasExtra(MainActivity.LAT_EXTRA_KEY)) {
                mLatitude = intent.getDoubleExtra(MainActivity.LAT_EXTRA_KEY, MainActivity.mDefaultLatitude);
                Log.i(LOG_TAG, "onCreate: mLatitude " + mLatitude);
            }
            if (intent.hasExtra(MainActivity.LAT_EXTRA_KEY)) {
                mLongitude = intent.getDoubleExtra(MainActivity.LON_EXTRA_KEY, MainActivity.mDefaultLongitude);
                Log.i(LOG_TAG, "onCreate: mLongitude " + mLongitude);
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mTypeContentAdapter = new PlaceTypeContentAdapter(this, this);
        mRecyclerView.setAdapter(mTypeContentAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<List<Place>> onCreateLoader(int id, @Nullable Bundle args) {
        return new TypeContentTaskLoader(this, mPlaceTypeNameValue, mLatitude, mLongitude);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Place>> loader, List<Place> data) {
        mLoadingProgressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            mTypeContentAdapter.setPlaces(data);
        } else {

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Place>> loader) {

    }

    @Override
    public void onPlaceTypeItemClicked(String placeId) {
        Intent openPlaceDetailsActivity = new Intent(this, PlaceDetailsActivity.class);
        openPlaceDetailsActivity.putExtra(PLACE_ID_INTENT_EXTRA, placeId);

        startActivity(openPlaceDetailsActivity);
    }

    private static class TypeContentTaskLoader extends AsyncTaskLoader<List<Place>> {

        private List<Place> mPlaces;
        private WeakReference<PlaceTypeContentActivity> mTypeContentActivityWeakReference;
        private String mPlaceTypeNameValue;
        private double mLatitude;
        private double mLongitude;

        public TypeContentTaskLoader(PlaceTypeContentActivity contentActivity, String placeType, double lat, double log) {
            super(contentActivity);
            this.mTypeContentActivityWeakReference = new WeakReference<>(contentActivity);
            this.mPlaceTypeNameValue = placeType;
            this.mLatitude = lat;
            this.mLongitude = log;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mPlaces != null) {
                deliverResult(mPlaces);
            } else {
                if (mTypeContentActivityWeakReference.get() != null) {
                    mTypeContentActivityWeakReference.get().mLoadingProgressBar.setVisibility(View.VISIBLE);
                }
                forceLoad();
            }
        }

        @Override
        public List<Place> loadInBackground() {

            String location = String.valueOf(mLatitude) + "," + String.valueOf(mLongitude);
            Log.i(LOG_TAG, "loadInBackground: " + location);

            URL requestUrl = NetworkUtilities.buildTypeContentUrl(location, mPlaceTypeNameValue);
            Log.i(LOG_TAG, "loadInBackground: " + requestUrl);

            if (requestUrl != null) {
                String jsonResponse = NetworkUtilities.getJsonResponseFromHttpUrl(requestUrl);
                if (jsonResponse != null) {
                    return JsonUtilities.extractPlacesFromJson(jsonResponse);
                }
            }
            return null;
        }

        @Override
        public void deliverResult(List<Place> data) {
            this.mPlaces = data;
            super.deliverResult(data);
        }
    }
}

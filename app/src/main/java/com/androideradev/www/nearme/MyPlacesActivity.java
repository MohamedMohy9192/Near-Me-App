package com.androideradev.www.nearme;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androideradev.www.nearme.adapter.PlaceTypeContentAdapter;
import com.androideradev.www.nearme.data.PlaceContract.PlaceEntry;
import com.androideradev.www.nearme.model.Place;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPlacesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PlaceTypeContentAdapter.OnPlaceTypeContentItemClickListener {

    private static final String LOG_TAG = MyPlacesActivity.class.getSimpleName();
    private static final int CURSOR_LOADER_ID = 200;

    private PlaceTypeContentAdapter mPlaceTypeContentAdapter;

    @BindView(R.id.rv_my_places_Activity)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_my_places_indicator)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceTypeContentAdapter = new PlaceTypeContentAdapter(this, this);
        mRecyclerView.setAdapter(mPlaceTypeContentAdapter);

        getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        mProgressBar.setVisibility(View.INVISIBLE);
        return new CursorLoader(this,
                PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mProgressBar.setVisibility(View.INVISIBLE);
//        if (data == null || data.getCount() == 0) {
//            Toast.makeText(this, R.string.no_saved_places, Toast.LENGTH_LONG).show();
//            return;
//        }
        List<Place> places = new ArrayList<>();

        while (data.moveToNext()) {
            String placeID = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_ID));
            String placeName = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_NAME));
            String placeType = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_TYPE));
            String placePhone = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_PHONE));
            String placeAddress = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_ADDRESS));
            String placeLat = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_LAT));
            String placeLng = data.getString(data.getColumnIndex(PlaceEntry.COLUMN_PLACE_LNG));

            Place place = new Place(placeID, placeName, placePhone, placeAddress, Double.valueOf(placeLat), Double.valueOf(placeLng), placeType);
            places.add(place);
        }
        mPlaceTypeContentAdapter.setPlaces(places);


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onPlaceTypeItemClicked(String placeId) {
        Intent openPlaceDetailsActivity = new Intent(this, PlaceDetailsActivity.class);
        openPlaceDetailsActivity.putExtra(PlaceTypeContentActivity.PLACE_ID_INTENT_EXTRA, placeId);

        startActivity(openPlaceDetailsActivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }
}

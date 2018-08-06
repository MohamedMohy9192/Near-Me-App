package com.androideradev.www.nearme;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androideradev.www.nearme.adapter.PlaceTypeContentAdapter;
import com.androideradev.www.nearme.data.PlaceContract;
import com.androideradev.www.nearme.model.Place;
import com.androideradev.www.nearme.utilities.NetworkUtilities;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyPlacesActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PlaceTypeContentAdapter.OnPlaceTypeContentItemClickListener {

    private static final String LOG_TAG = MyPlacesActivity.class.getSimpleName();

    private PlaceTypeContentAdapter mPlaceTypeContentAdapter;

    @BindView(R.id.rv_my_places_Activity)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_places);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceTypeContentAdapter = new PlaceTypeContentAdapter(this, this);
        mRecyclerView.setAdapter(mPlaceTypeContentAdapter);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                PlaceContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() == 0) {
            Toast.makeText(this, "No Saved Places!", Toast.LENGTH_LONG).show();
            return;
        }
        List<String> placesId = new ArrayList<>();

        while (data.moveToNext()) {
            String placeId = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID));
            placesId.add(placeId);
        }

        new LoadPlacesBasedOnIds(this).execute(placesId);

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

    private static class LoadPlacesBasedOnIds extends AsyncTask<List<String>, Void, List<Place>> {
        private WeakReference<MyPlacesActivity> mMyPlacesActivityWeakReference;

        LoadPlacesBasedOnIds(MyPlacesActivity placesActivity) {
            this.mMyPlacesActivityWeakReference = new WeakReference<>(placesActivity);
        }

        @SafeVarargs
        @Override
        protected final List<Place> doInBackground(List<String>... lists) {
            List<String> placesId = lists[0];
            List<Place> places = new ArrayList<>();
            for (int i = 0; i < placesId.size(); i++) {
                String placeId = placesId.get(i);
                if (mMyPlacesActivityWeakReference.get() != null) {
                    URL requestUrl = NetworkUtilities.buildPlaceDetailsUrl(placeId);
                    Log.i(LOG_TAG, "doInBackground: " + requestUrl);
                    if (requestUrl != null) {
                        String jsonResponse = NetworkUtilities.getJsonResponseFromHttpUrl(requestUrl);
                        if (jsonResponse != null) {
//                            Place place = NetworkUtilities.extractPlaceBasedOnId(jsonResponse);
//                            places.add(place);
                        }
                    }
                }
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<Place> places) {
            if (places != null) {
                if (mMyPlacesActivityWeakReference.get() != null) {
                    mMyPlacesActivityWeakReference.get().mPlaceTypeContentAdapter.setPlaces(places);
                }

            } else {

            }
        }
    }
}

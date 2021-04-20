package com.androideradev.www.nearme;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.androideradev.www.nearme.adapter.PlaceTypeContentAdapter;
import com.androideradev.www.nearme.data.PlaceContract;
import com.androideradev.www.nearme.model.Place;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WidgetConfigurationActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PlaceTypeContentAdapter.OnPlaceTypeContentItemClickListener {

    private static final int WIDGET_LOADER_ID = 40;

    @BindView(R.id.rv_widget_place)
    RecyclerView mRecyclerView;

    @BindView(R.id.pb_widget_loading_indicator)
    ProgressBar mLoadingIndicatorProgressBar;

    private PlaceTypeContentAdapter mPlaceTypeContentAdapter;

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private AppWidgetManager mAppWidgetManger;
    private RemoteViews mRemoteViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        setResult(RESULT_CANCELED);

        mAppWidgetManger = AppWidgetManager.getInstance(this);

        mRemoteViews = new RemoteViews(this.getPackageName(), R.layout.my_places_widget);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceTypeContentAdapter = new PlaceTypeContentAdapter(this, this);
        mRecyclerView.setAdapter(mPlaceTypeContentAdapter);

        getSupportLoaderManager().initLoader(WIDGET_LOADER_ID, null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        mLoadingIndicatorProgressBar.setVisibility(View.INVISIBLE);
        return new CursorLoader(this,
                PlaceContract.PlaceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mLoadingIndicatorProgressBar.setVisibility(View.INVISIBLE);
        List<Place> places = new ArrayList<>();

        while (data.moveToNext()) {
            String placeID = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ID));
            String placeName = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_NAME));
            String placeType = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_TYPE));
            String placePhone = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_PHONE));
            String placeAddress = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_ADDRESS));
            String placeLat = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_LAT));
            String placeLng = data.getString(data.getColumnIndex(PlaceContract.PlaceEntry.COLUMN_PLACE_LNG));

            Place place = new Place(placeID, placeName, placePhone, placeAddress, Double.valueOf(placeLat), Double.valueOf(placeLng), placeType);
            places.add(place);
        }
        mPlaceTypeContentAdapter.setPlaces(places);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onPlaceTypeItemClicked(String placeId, String placeName, String placeType) {


        mRemoteViews.setTextViewText(R.id.tv_appwidget_place_name, placeName);
        mRemoteViews.setTextViewText(R.id.tv_appwidget_place_type, placeType);

        mAppWidgetManger.updateAppWidget(mAppWidgetId, mRemoteViews);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);

        setResult(RESULT_OK, resultValue);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportLoaderManager().restartLoader(WIDGET_LOADER_ID, null, this);
    }
}

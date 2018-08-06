package com.androideradev.www.nearme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.androideradev.www.nearme.fragment.PlaceOverviewFragment;
import com.androideradev.www.nearme.fragment.PlacePhotosFragment;
import com.androideradev.www.nearme.fragment.PlaceReviewsFragment;
import com.androideradev.www.nearme.model.Place;
import com.androideradev.www.nearme.model.PlaceReview;
import com.androideradev.www.nearme.utilities.JsonUtilities;
import com.androideradev.www.nearme.utilities.NetworkUtilities;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Place> {

    private static final String LOG_TAG = PlaceDetailsActivity.class.getSimpleName();

    private static final int PLACE_DETAILS_LOADER_ID = 18;

    private String mPlaceId;

    private ViewPagerDetailsAdapter mViewPagerDetailsAdapter;

    @BindView(R.id.pb_photos_loading_indicator)
    ProgressBar mPhotosLoadingIndicator;
    @BindView(R.id.tab_layout_details)
    TabLayout mDetailsTabLayout;
    @BindView(R.id.viewpager_details)
    ViewPager mDetailsViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(PlaceTypeContentActivity.PLACE_ID_INTENT_EXTRA)) {
                mPlaceId = intent.getStringExtra(PlaceTypeContentActivity.PLACE_ID_INTENT_EXTRA);
            }
        }

        mViewPagerDetailsAdapter = new ViewPagerDetailsAdapter(getSupportFragmentManager());
        mDetailsViewPager.setAdapter(mViewPagerDetailsAdapter);
        mDetailsTabLayout.setupWithViewPager(mDetailsViewPager);
        getSupportLoaderManager().initLoader(PLACE_DETAILS_LOADER_ID, null, this);

    }

    @NonNull
    @Override
    public Loader<Place> onCreateLoader(int id, @Nullable Bundle args) {
        return new PlaceDetailsAsyncTaskLoader(this, mPlaceId);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Place> loader, Place data) {
        mPhotosLoadingIndicator.setVisibility(View.INVISIBLE);
        if (data != null) {
            PlaceOverviewFragment placeOverviewFragment = PlaceOverviewFragment.newInstance(data);
            mViewPagerDetailsAdapter.addFragment(placeOverviewFragment, getString(R.string.overview_page_name));
            PlaceReviewsFragment placeReviewsFragment = PlaceReviewsFragment.newInstance(data.getPlaceReviews());
            mViewPagerDetailsAdapter.addFragment(placeReviewsFragment, getString(R.string.reviews_page_name));
            PlacePhotosFragment placePhotosFragment = PlacePhotosFragment.newInstance(data.getPlacePhotos());
            mViewPagerDetailsAdapter.addFragment(placePhotosFragment, getString(R.string.photos_page_name));
        } else {

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Place> loader) {

    }

    private class ViewPagerDetailsAdapter extends FragmentPagerAdapter {

        List<Fragment> mFragments = new ArrayList<>();
        List<String> mPagesTitle = new ArrayList<>();

        public ViewPagerDetailsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mPagesTitle.get(position);
        }

        public void addFragment(Fragment fragment, String pageName) {
            mFragments.add(fragment);
            mPagesTitle.add(pageName);
            notifyDataSetChanged();

        }


    }

    private static class PlaceDetailsAsyncTaskLoader extends AsyncTaskLoader<Place> {

        private Place mPlace;
        private WeakReference<PlaceDetailsActivity> mPlaceDetailsActivityWeakReference;
        private String mPlaceId;


        public PlaceDetailsAsyncTaskLoader(PlaceDetailsActivity placeDetailsActivity, String placeId) {
            super(placeDetailsActivity);
            this.mPlaceDetailsActivityWeakReference = new WeakReference<>(placeDetailsActivity);
            this.mPlaceId = placeId;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (mPlace != null) {
                deliverResult(mPlace);
            } else {
                if (mPlaceDetailsActivityWeakReference.get() != null) {
                    mPlaceDetailsActivityWeakReference.get().mPhotosLoadingIndicator.setVisibility(View.VISIBLE);
                }
                forceLoad();
            }
        }

        @Override
        public Place loadInBackground() {
            URL requestUrl = NetworkUtilities.buildPlaceDetailsUrl(mPlaceId);
            Log.i(LOG_TAG, "loadInBackground: " + requestUrl);

            if (requestUrl != null) {
                String jsonResponse = NetworkUtilities.getJsonResponseFromHttpUrl(requestUrl);
                if (jsonResponse != null) {
                    return JsonUtilities.extractPlaceFromJson(jsonResponse);
                }
            }
            return null;
        }

        @Override
        public void deliverResult(Place data) {
            this.mPlace = data;
            super.deliverResult(data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

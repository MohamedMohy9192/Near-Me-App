package com.androideradev.www.nearme.fragment;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androideradev.www.nearme.PlaceTypeContentActivity;
import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.model.Place;
import com.androideradev.www.nearme.utilities.JsonUtilities;
import com.androideradev.www.nearme.utilities.NetworkUtilities;

import java.lang.ref.WeakReference;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceOverviewFragment extends Fragment {

    private static final String PLACE_OVERVIEW_KEY = "place_overview";
    private static final String PLACE_ID_KEY = "place_ID";

    @BindView(R.id.tv_details_place_ratting)
    TextView mPlaceRattingTextView;
    @BindView(R.id.rb_details_place_ratting)
    RatingBar mPlaceRattingRattingBar;
    @BindView(R.id.tv_details_place_address)
    TextView mPlaceAddressTextView;
    @BindView(R.id.tv_details_place_status)
    TextView mPlaceStatusTextView;
    @BindView(R.id.tv_details_place_phone)
    TextView mPlacePhoneTextView;
    @BindView(R.id.tv_likes_count)
    TextView mLikesCountTextView;

    private Place mPlace;
    private String mPlaceID;

    public PlaceOverviewFragment() {
        // Required empty public constructor
    }

    public static PlaceOverviewFragment newInstance(Place place, String placeID) {
        PlaceOverviewFragment fragment = new PlaceOverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(PLACE_OVERVIEW_KEY, place);
        args.putString(PLACE_ID_KEY, placeID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlace = getArguments().getParcelable(PLACE_OVERVIEW_KEY);
            mPlaceID = getArguments().getString(PLACE_ID_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_overview, container, false);
        ButterKnife.bind(this, rootView);

        String placeRatting = mPlace.getRatting();
        if (TextUtils.isEmpty(placeRatting)) {
            placeRatting = "0.0";
        }

        String noInfo = getString(R.string.no_info_available);
        mPlaceRattingTextView.setText(placeRatting);
        mPlaceRattingRattingBar.setRating(Float.valueOf(placeRatting));

        String placeAddress = mPlace.getAddress();
        if (TextUtils.isEmpty(placeAddress)) placeAddress = noInfo;
        mPlaceAddressTextView.setText(placeAddress);

        String placeStatus = mPlace.getPlaceStatus();
        if (TextUtils.isEmpty(placeStatus)) placeStatus = noInfo;
        mPlaceStatusTextView.setText(placeStatus);

        String placePhone = mPlace.getPhone();
        if (TextUtils.isEmpty(placePhone)) placePhone = noInfo;
        mPlacePhoneTextView.setText(placePhone);

        String url = NetworkUtilities.buildLikesUrl(mPlaceID);
        new LikesCountAsyncTask(this).execute(url);
        return rootView;
    }

    private static class LikesCountAsyncTask extends AsyncTask<String, Void, String> {

        private WeakReference<PlaceOverviewFragment> mFragmentWeakReference;

        public LikesCountAsyncTask(PlaceOverviewFragment overviewFragment) {

            this.mFragmentWeakReference = new WeakReference<>(overviewFragment);


        }

        @Override
        protected String doInBackground(String... strings) {
            String requestUrl = strings[0];

            URL url = NetworkUtilities.buildUrl(requestUrl);
            if (requestUrl != null) {
                String jsonResponse = NetworkUtilities.getJsonResponseFromHttpUrl(url);
                if (jsonResponse != null) {
                    return JsonUtilities.extractLikeCountFromJson(jsonResponse);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!TextUtils.isEmpty(s)) {
                if (mFragmentWeakReference.get() != null) {
                    mFragmentWeakReference.get().mLikesCountTextView.setText(s);
                }
            }
        }
    }

}

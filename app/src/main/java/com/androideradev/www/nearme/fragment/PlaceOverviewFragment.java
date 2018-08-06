package com.androideradev.www.nearme.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.model.Place;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceOverviewFragment extends Fragment {

    private static final String PLACE_OVERVIEW_KEY = "place_overview";

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

    private Place mPlace;

    public PlaceOverviewFragment() {
        // Required empty public constructor
    }

    public static PlaceOverviewFragment newInstance(Place place) {
        PlaceOverviewFragment fragment = new PlaceOverviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(PLACE_OVERVIEW_KEY, place);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlace = getArguments().getParcelable(PLACE_OVERVIEW_KEY);
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
        mPlaceRattingTextView.setText(placeRatting);
        mPlaceRattingRattingBar.setRating(Float.valueOf(placeRatting));

        mPlaceAddressTextView.setText(mPlace.getAddress());
        boolean isOpenNow = mPlace.isOpen();
        String placeStatus = getString(R.string.place_closed_status);
        if (isOpenNow) {
            placeStatus = getString(R.string.place_open_status);
        }
        mPlaceStatusTextView.setText(mPlace.getPlaceStatus());
        mPlacePhoneTextView.setText(mPlace.getPhone());
        return rootView;
    }

}

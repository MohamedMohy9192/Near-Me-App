package com.androideradev.www.nearme.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.adapter.PlaceReviewsAdapter;
import com.androideradev.www.nearme.model.PlaceReview;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceReviewsFragment extends Fragment {

    private static final String PLACE_REVIEWS_KEY = "place_review";

    @BindView(R.id.rv_place_reviews)
    RecyclerView mRecyclerView;

    private List<PlaceReview> mPlaceReviews;

    private PlaceReviewsAdapter mPlaceReviewsAdapter;

    public PlaceReviewsFragment() {
        // Required empty public constructor
    }

    public static PlaceReviewsFragment newInstance(List<PlaceReview> placeReviews) {
        PlaceReviewsFragment fragment = new PlaceReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(PLACE_REVIEWS_KEY, (ArrayList<? extends Parcelable>) placeReviews);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPlaceReviews = getArguments().getParcelableArrayList(PLACE_REVIEWS_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_place_reviews, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPlaceReviewsAdapter = new PlaceReviewsAdapter(mPlaceReviews, getContext());
        mRecyclerView.setAdapter(mPlaceReviewsAdapter);


        return rootView;
    }


}

package com.androideradev.www.nearme.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.adapter.PhotosAdapter;
import com.androideradev.www.nearme.model.Photo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlacePhotosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlacePhotosFragment extends Fragment {

    private static final String PHOTOS_REFERENCE_KEY = "photos_reference";

    private List<Photo> mPhotos;
    private String mParam2;

    private PhotosAdapter mPhotosAdapter;

    @BindView(R.id.rv_place_photos)
    RecyclerView mRecyclerView;

    public PlacePhotosFragment() {

    }

    public static PlacePhotosFragment newInstance(List<Photo> photos) {
        PlacePhotosFragment fragment = new PlacePhotosFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(PHOTOS_REFERENCE_KEY, (ArrayList<? extends Parcelable>) photos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPhotos = getArguments().getParcelableArrayList(PHOTOS_REFERENCE_KEY);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_place_photos, container, false);
        ButterKnife.bind(this, rootView);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mPhotosAdapter = new PhotosAdapter(getContext());
        mRecyclerView.setAdapter(mPhotosAdapter);

        mPhotosAdapter.setPhotosReference(mPhotos);
        // Inflate the layout for this fragment
        return rootView;
    }

}

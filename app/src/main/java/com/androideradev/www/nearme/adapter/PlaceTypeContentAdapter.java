package com.androideradev.www.nearme.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.data.PlaceContract.PlaceEntry;
import com.androideradev.www.nearme.model.Place;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceTypeContentAdapter extends RecyclerView.Adapter<PlaceTypeContentAdapter.PlaceItemViewHolder> {

    private List<Place> mPlaces;
    private Context mContext;
    private int mIsFavorite;

    private OnPlaceTypeContentItemClickListener mTypeContentItemClickListener;

    public interface OnPlaceTypeContentItemClickListener {
        void onPlaceTypeItemClicked(String placeId);
    }

    public PlaceTypeContentAdapter(Context context, OnPlaceTypeContentItemClickListener itemClickListener) {
        this.mContext = context;
        this.mTypeContentItemClickListener = itemClickListener;
        mIsFavorite = 0;
    }

    @NonNull
    @Override
    public PlaceItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).
                inflate(R.layout.place_list_item, parent, false);

        return new PlaceItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceItemViewHolder holder, int position) {
        final Place place = mPlaces.get(position);


        holder.placeNameTextView.setText(place.getName());
        holder.placeTypeTextView.setText(place.getPlaceType());

        final ImageView favoriteImageVie = holder.addToFavoriteImageView;
        checkFavoriteState(place.getPlaceId());
        setIsFavoriteIcon(favoriteImageVie);
        favoriteImageVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String placeID = place.getPlaceId();
                String placeName = place.getName();
                String placePhone = place.getPhone();
                if (TextUtils.isEmpty(placePhone)) {
                    placePhone = "No Info Available!";
                }
                String placeAddress = place.getAddress();
                double placeLat = place.getLat();
                double placeLng = place.getLng();
                String placeType = place.getPlaceType();

                ContentResolver contentResolver = mContext.getContentResolver();
                if (mIsFavorite == PlaceEntry.PLACE_NOT_FAVORITE) {
                    ContentValues values = new ContentValues();
                    values.put(PlaceEntry.COLUMN_PLACE_IS_FAVORITE, PlaceEntry.PLACE_IS_FAVORITE);
                    values.put(PlaceEntry.COLUMN_PLACE_ID, placeID);
                    values.put(PlaceEntry.COLUMN_PLACE_NAME, placeName);
                    values.put(PlaceEntry.COLUMN_PLACE_PHONE, placePhone);
                    values.put(PlaceEntry.COLUMN_PLACE_ADDRESS, placeAddress);
                    values.put(PlaceEntry.COLUMN_PLACE_LAT, placeLat);
                    values.put(PlaceEntry.COLUMN_PLACE_LNG, placeLng);
                    values.put(PlaceEntry.COLUMN_PLACE_TYPE, placeType);
                    Uri placeUri = contentResolver.insert(PlaceEntry.CONTENT_URI, values);
                    if (placeUri != null) {
                        Toast.makeText(mContext, R.string.place_added_to_favorites, Toast.LENGTH_LONG).show();
                    }
                    checkFavoriteState(placeID);
                    setIsFavoriteIcon(favoriteImageVie);

                } else {
                    Uri placeUri = PlaceEntry.CONTENT_URI.buildUpon().appendPath(placeID).build();
                    int deletedNum = contentResolver.delete(placeUri, null, null);
                    if (deletedNum > 0) {
                        Toast.makeText(mContext, R.string.place_deleted_from_favorite, Toast.LENGTH_LONG).show();
                    }
                    checkFavoriteState(placeID);
                    setIsFavoriteIcon(favoriteImageVie);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        if (mPlaces == null) return 0;
        return mPlaces.size();
    }

    public void setPlaces(List<Place> places) {
        this.mPlaces = places;
        if (mPlaces != null) {
            notifyDataSetChanged();
        }
    }

    private void checkFavoriteState(String placeID) {
        String[] projection = new String[]{PlaceEntry.COLUMN_PLACE_IS_FAVORITE};
        Uri placeUri = PlaceEntry.CONTENT_URI.buildUpon().appendPath(placeID).build();

        Cursor cursor = mContext.getContentResolver().query(
                placeUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToPosition(0)) {
                mIsFavorite = cursor.getInt(cursor.getColumnIndex(PlaceEntry.COLUMN_PLACE_IS_FAVORITE));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    private void setIsFavoriteIcon(ImageView isFavoriteImageView) {
        if (mIsFavorite == PlaceEntry.PLACE_IS_FAVORITE) {
            isFavoriteImageView.setImageResource(R.drawable.ic_place_is_favorite);
        } else {
            isFavoriteImageView.setImageResource(R.drawable.ic_place_not_favorite);
        }
    }

    public class PlaceItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_place_name)
        TextView placeNameTextView;
        @BindView(R.id.tv_place_type)
        TextView placeTypeTextView;
        @BindView(R.id.iv_favorite_place)
        ImageView addToFavoriteImageView;
        @BindView(R.id.iv_open_map)
        ImageView openMapImageView;

        public PlaceItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemPosition = getAdapterPosition();
            Place place = mPlaces.get(itemPosition);
            mTypeContentItemClickListener.onPlaceTypeItemClicked(place.getPlaceId());
        }
    }


}

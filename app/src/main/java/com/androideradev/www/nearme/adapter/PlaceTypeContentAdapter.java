package com.androideradev.www.nearme.adapter;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.androideradev.www.nearme.MapsActivity;
import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.data.PlaceContract.PlaceEntry;
import com.androideradev.www.nearme.model.Place;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceTypeContentAdapter extends RecyclerView.Adapter<PlaceTypeContentAdapter.PlaceItemViewHolder> {

    private List<Place> mPlaces;
    private Context mContext;

    private OnPlaceTypeContentItemClickListener mTypeContentItemClickListener;

    public interface OnPlaceTypeContentItemClickListener {
        void onPlaceTypeItemClicked(String placeId, String placeName, String placeType);
    }

    public PlaceTypeContentAdapter(Context context, OnPlaceTypeContentItemClickListener itemClickListener) {
        this.mContext = context;
        this.mTypeContentItemClickListener = itemClickListener;
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

        int isFavorite = checkFavoriteState(place.getPlaceId());
        setIsFavoriteIcon(favoriteImageVie, isFavorite);
        favoriteImageVie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noInfo = mContext.getString(R.string.no_info_available);

                String placeID = place.getPlaceId();
                int isFavorite = checkFavoriteState(placeID);

                String placeName = place.getName();
                if (TextUtils.isEmpty(placeName)) placeName = noInfo;

                String placePhone = place.getPhone();
                if (TextUtils.isEmpty(placePhone)) {
                    placePhone = noInfo;
                }
                String placeAddress = place.getAddress();
                if (TextUtils.isEmpty(placeAddress)) placeAddress = noInfo;

                double placeLat = place.getLat();
                double placeLng = place.getLng();

                String placeType = place.getPlaceType();
                if (TextUtils.isEmpty(placeType)) placeType = noInfo;

                ContentResolver contentResolver = mContext.getContentResolver();
                if (isFavorite == PlaceEntry.PLACE_NOT_FAVORITE) {
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
                    isFavorite = PlaceEntry.PLACE_IS_FAVORITE;
                    setIsFavoriteIcon(favoriteImageVie, isFavorite);

                } else {
                    Uri placeUri = PlaceEntry.CONTENT_URI.buildUpon().appendPath(placeID).build();
                    int deletedNum = contentResolver.delete(placeUri, null, null);
                    if (deletedNum > 0) {
                        Toast.makeText(mContext, R.string.place_deleted_from_favorite, Toast.LENGTH_LONG).show();
                    }
                    isFavorite = PlaceEntry.PLACE_NOT_FAVORITE;
                    setIsFavoriteIcon(favoriteImageVie, isFavorite);
                }
            }
        });

        ImageView openMapImageView = holder.openMapImageView;
        openMapImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openMapsActivity = new Intent(mContext, MapsActivity.class);
                openMapsActivity.putExtra(MapsActivity.MAPS_LAT_INTENT_EXTRA, place.getLat());
                openMapsActivity.putExtra(MapsActivity.MAPS_LNG_INTENT_EXTRA, place.getLng());
                openMapsActivity.putExtra(MapsActivity.MAPS_PLACE_NAME_INTENT_EXTRA, place.getName());
                openMapsActivity.putExtra(MapsActivity.MAPS_PLACE_TYPE_INTENT_EXTRA, place.getPlaceType());
                mContext.startActivity(openMapsActivity);
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

    private int checkFavoriteState(String placeID) {
        int isFavorite = 0;
        String[] projection = new String[]{PlaceEntry.COLUMN_PLACE_IS_FAVORITE};
        Uri placeUri = PlaceEntry.CONTENT_URI.buildUpon().appendPath(placeID).build();

        Cursor cursor = mContext.getContentResolver().query(
                placeUri, projection, null, null, null);

        if (cursor != null) {
            if (cursor.moveToPosition(0)) {
                isFavorite = cursor.getInt(cursor.getColumnIndex(PlaceEntry.COLUMN_PLACE_IS_FAVORITE));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return isFavorite;
    }

    private void setIsFavoriteIcon(ImageView isFavoriteImageView, int isFavorite) {
        if (isFavorite == PlaceEntry.PLACE_IS_FAVORITE) {
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
            mTypeContentItemClickListener.onPlaceTypeItemClicked(place.getPlaceId(), place.getName(), place.getPlaceType());
        }
    }


}

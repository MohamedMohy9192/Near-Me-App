package com.androideradev.www.nearme.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.model.Photo;
import com.androideradev.www.nearme.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotosViewHolder> {

    private List<Photo> mPhotos;
    private Context mContext;

    public PhotosAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public PhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.photo_list_item, parent, false);

        return new PhotosViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotosViewHolder holder, int position) {
        Photo photo = mPhotos.get(position);

        String photoUrl = NetworkUtilities.buildPlacePhotoUrl(photo.getPrefix(), photo.getSuffix());
        Picasso.get()
                .load(photoUrl)
                .placeholder(R.drawable.place_pic_holder)
                .error(R.drawable.place_pic_holder)
                .into(holder.placePhotoImageView);

    }

    @Override
    public int getItemCount() {
        if (mPhotos == null) return 0;

        return mPhotos.size();
    }

    public void setPhotosReference(List<Photo> photos) {
        this.mPhotos = photos;

        if (mPhotos != null) {
            notifyDataSetChanged();
        }
    }

    public class PhotosViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_place_photo_list_item)
        ImageView placePhotoImageView;

        public PhotosViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}

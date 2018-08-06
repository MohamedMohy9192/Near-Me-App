package com.androideradev.www.nearme.adapter;

import android.content.Context;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androideradev.www.nearme.R;
import com.androideradev.www.nearme.model.Photo;
import com.androideradev.www.nearme.model.PlaceReview;
import com.androideradev.www.nearme.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceReviewsAdapter extends RecyclerView.Adapter<PlaceReviewsAdapter.PlaceReviewsViewHolder> {

    private List<PlaceReview> mPlaceReviews;
    private Context mContext;

    public PlaceReviewsAdapter(List<PlaceReview> placeReviews, Context context) {
        this.mPlaceReviews = placeReviews;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PlaceReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_revviews_list_item, parent, false);
        return new PlaceReviewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceReviewsViewHolder holder, int position) {
        PlaceReview placeReview = mPlaceReviews.get(position);

        holder.authorNameTextView.setText(placeReview.getUserName());

        Photo photo = placeReview.getUserPhoto();
        String photoUrl = NetworkUtilities.buildUserPhotoUrl(photo.getPrefix(), photo.getSuffix());
        Picasso.get()
                .load(photoUrl)
                .placeholder(R.drawable.user_pic_place_holder)
                .error(R.drawable.user_pic_place_holder)
                .into(holder.authorPhotoImageView);

        String authorRatting = placeReview.getTipRatting();
        if (TextUtils.isEmpty(authorRatting)) {
            authorRatting = "0.0";
        }
        holder.authorRatting.setRating(Float.valueOf(authorRatting));

        String timeStamp = placeReview.getTipTime();

        if (!TextUtils.isEmpty(timeStamp)) {
            Date date = new Date(Long.valueOf(timeStamp) * 1000L);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
            timeStamp = simpleDateFormat.format(date);

        } else {
            timeStamp = mContext.getString(R.string.no_info_available);
        }
        holder.authorTimeTextView.setText(timeStamp);
        String text = placeReview.getTipText();
        if (TextUtils.isEmpty(text)) text = mContext.getString(R.string.no_info_available);
        holder.authorTextTextView.setText(text);


    }

    @Override
    public int getItemCount() {
        if (mPlaceReviews == null) return 0;
        return mPlaceReviews.size();
    }

    public class PlaceReviewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_author_name)
        TextView authorNameTextView;
        @BindView(R.id.iv_author_photo)
        ImageView authorPhotoImageView;
        @BindView(R.id.ratingBar)
        RatingBar authorRatting;
        @BindView(R.id.tv_author_time)
        TextView authorTimeTextView;
        @BindView(R.id.tv_author_text)
        TextView authorTextTextView;

        public PlaceReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

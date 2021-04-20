package com.androideradev.www.nearme.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androideradev.www.nearme.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceTypeAdapter extends RecyclerView.Adapter<PlaceTypeAdapter.PlaceTypeViewHolder> {

    private String[] mPlaceTypeList;

    private OnPlaceTypeItemClickListener mPlaceTypeItemClickListener;

    public interface OnPlaceTypeItemClickListener {
        void onPlaceTypeItemClicked(int placeTypeItemPosition);
    }

    public PlaceTypeAdapter(OnPlaceTypeItemClickListener placeTypeItemClickListener) {
        this.mPlaceTypeItemClickListener = placeTypeItemClickListener;
    }

    @NonNull
    @Override
    public PlaceTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.place_type_list_item, parent, false);

        return new PlaceTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceTypeViewHolder holder, int position) {
        String placeTypeName = mPlaceTypeList[position];

        holder.placeTypeNameTextView.setText(placeTypeName);

    }

    @Override
    public int getItemCount() {
        if (mPlaceTypeList == null) return 0;

        return mPlaceTypeList.length;
    }

    public void setPlaceTypeList(String[] placeTypeList) {
        this.mPlaceTypeList = placeTypeList;

        if (mPlaceTypeList != null) {
            notifyDataSetChanged();
        }

    }

    public class PlaceTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_place_type_name)
        TextView placeTypeNameTextView;

        public PlaceTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int placeTypeItemPosition = getAdapterPosition();

            mPlaceTypeItemClickListener.onPlaceTypeItemClicked(placeTypeItemPosition);
        }
    }
}

package app.downloadaccess.places.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.downloadaccess.places.R;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> places;
    private Context context;
    private PlacesCallback callback;

    public PlacesAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    public void setAdapterCallback(PlacesCallback callback) {
        this.callback = callback;
    }

    public void setDataList(List<Place> places) {
        this.places = places;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getName());
        holder.placeDesc.setText(place.getDescription());
        if (place.getWww() != null && !place.getWww().isEmpty()) {
            holder.locationLayout.setVisibility(View.VISIBLE);
            holder.websiteTv.setText(place.getWww());
        } else {
            holder.locationLayout.setVisibility(View.GONE);
        }
        holder.websiteTv.setText(place.getWww());
        Picasso.get().load(RetrofitClientInstance.BASE_URL + "/get-image/" + place.getId()).into(holder.image);

        if (place.isFavourite()) {
            holder.placeFav.setVisibility(View.VISIBLE);
            holder.placeFav.setImageResource(R.drawable.ic_heart_filled);
        } else {
            holder.placeFav.setVisibility(View.VISIBLE);
            holder.placeFav.setImageResource(R.drawable.ic_heart);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_place_recycler, parent, false));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeName, placeDesc, websiteTv;
        private ImageView placeFav, image;
        private ConstraintLayout place_layout;
        private LinearLayout locationLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationLayout = itemView.findViewById(R.id.locationLayout);
            place_layout = itemView.findViewById(R.id.place_layout);
            place_layout.setOnClickListener(v -> {
                callback.onPlaceClick(getAdapterPosition());
            });
            placeName = itemView.findViewById(R.id.placeName);
            placeName.setOnClickListener(v -> {
                callback.onPlaceClick(getAdapterPosition());
            });
            placeDesc = itemView.findViewById(R.id.placeDesc);
            placeDesc.setOnClickListener(v -> {
                callback.onPlaceClick(getAdapterPosition());
            });
            placeFav = itemView.findViewById(R.id.placeFav);
            placeFav.setOnClickListener(v -> {
                callback.onFavouriteClick(getAdapterPosition());
            });
            websiteTv = itemView.findViewById(R.id.websiteTv);
            websiteTv.setOnClickListener(v -> {
                callback.onWebsiteClick(getAdapterPosition());

            });
            image = itemView.findViewById(R.id.image);
            image.setOnClickListener(v -> {
                callback.onPlaceClick(getAdapterPosition());
            });
        }
    }

    public interface PlacesCallback {
        void onFavouriteClick(int position);

        void onPlaceClick(int position);

        void onWebsiteClick(int position);
    }
}

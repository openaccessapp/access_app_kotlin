package app.downloadaccess.administrator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.downloadaccess.administrator.R;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> places;
    private List<Place> placesFull;
    private Context context;
    private PlacesCallback callback;

    public PlacesAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
        this.placesFull = new ArrayList<>(this.places);
    }

    public void setAdapterCallback(PlacesCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getName());
        holder.placeDesc.setText(place.getDescription());
        holder.websiteTv.setText(place.getWww());
        Picasso.get().load(RetrofitClientInstance.BASE_URL + ".netlify/functions/get-image/" + place.getId()).into(holder.image);
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
        private ImageView image;
        private ConstraintLayout cardLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardLayout = itemView.findViewById(R.id.card_history_museum);
            cardLayout.setOnClickListener(v -> {
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
        void onPlaceClick(int position);
        void onWebsiteClick(int position);
    }
}

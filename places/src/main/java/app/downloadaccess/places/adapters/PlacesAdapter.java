package app.downloadaccess.places.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.downloadaccess.places.R;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> implements Filterable {

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

    public void setDataList(List<Place> places) {
        this.places = places;
        this.placesFull = new ArrayList<>(this.places);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getName());
        holder.placeDesc.setText(place.getDescription());
        holder.websiteTv.setText(place.getWww());
        Picasso.get().load(RetrofitClientInstance.BASE_URL + "/api/image/" + place.getId()).into(holder.image);

        if (place.isFavourite()) {
            holder.placeFav.setImageResource(R.drawable.ic_heart_filled);
        } else {
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

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence type) {
            List<Place> filteredList = new ArrayList<>();
            if (type == null || type.length() == 0) {
                filteredList.addAll(placesFull);
            } else {
                String filterPattern = type.toString().toLowerCase().trim();
                String keyword = "";
                switch (filterPattern) {
                    case "parks":
                        keyword = "park";
                        break;
                    case "museums":
                        keyword = "museum";
                        break;
                    case "fav":
                        keyword = "fav";
                        break;
                    default:
                        keyword = "all";
                        break;
                }
                for (Place item : placesFull) {
                    if (keyword.equals("fav")) {
//                        if (item.isFavourite) {
//                            filteredList.add(item);
//                        }
                    } else if (keyword.equals("all")) {
                        filteredList.add(item);
                    } else {
//                        if (item.type != null && item.type.toLowerCase().equals(keyword)) {
//                            filteredList.add(item);
//                        }
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            places.clear();
            places.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeName, placeDesc, websiteTv;
        private ImageView placeFav, image;
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
        void onPlaceClick(int position);

        void onWebsiteClick(int position);

        void onFavouriteClick(int position);
    }
}

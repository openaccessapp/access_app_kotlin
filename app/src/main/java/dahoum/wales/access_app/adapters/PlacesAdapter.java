package dahoum.wales.access_app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.models.Place;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    private List<Place> places;
    private Context context;

    public PlacesAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.placeName.setText(place.getName());
        holder.placeDesc.setText(place.getDescription());
        if (place.getIsFavourite()) {
            holder.placeFav.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        } else {
            holder.placeFav.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        }
        holder.websiteTv.setText(place.getWww());
        if (place.getImage() != null) {
            byte[] decodedString = Base64.decode(place.getImage().substring(23), Base64.DEFAULT);
            Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.image.setImageBitmap(base64Bitmap);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_place_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeName, placeDesc, websiteTv;
        private ImageView placeFav, image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeName = itemView.findViewById(R.id.placeName);
            placeDesc = itemView.findViewById(R.id.placeDesc);
            placeFav = itemView.findViewById(R.id.placeFav);
            websiteTv = itemView.findViewById(R.id.websiteTv);
            image = itemView.findViewById(R.id.image);
        }
    }
}

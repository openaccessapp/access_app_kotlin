package dahoum.wales.access_app.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.models.Place;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<Place> places;
    private Context context;

    public HomeAdapter(Context context, List<Place> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Place place = places.get(position);
        holder.name.setText(place.getName());
        if (place.getPriority().equals("Normal")) {
            holder.priority.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        } else if (place.getPriority().equals("Priority")) {
            holder.priority.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
        }
        holder.priority.setText(place.getPriority());
        holder.capacity.setText(place.getCapacity());
        holder.hoursTv.setText(place.getHours());
        for (int i = 0; i < place.getCurrentCount() - 1; i++) {
            ImageView person = new ImageView(context);
            person.setImageResource(R.drawable.ic_person_outline_24px);
            person.setLayoutParams(new LinearLayout.LayoutParams(
                    (int) (24 * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT)),
                    (int) (24 * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT))));
            holder.persons.addView(person);
        }
        holder.personCount.setText(String.valueOf(place.getCurrentCount()));
        holder.calendarDate.setText(place.getDate());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_home_recycler, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, capacity, calendarDate, personCount, hoursTv;
        private MaterialButton priority;
        private LinearLayout persons;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.placeName);
            capacity = itemView.findViewById(R.id.placeCapacity);
            priority = itemView.findViewById(R.id.priority_text);
            calendarDate = itemView.findViewById(R.id.calendar_date_rv);
            personCount = itemView.findViewById(R.id.personCountTv);
            hoursTv = itemView.findViewById(R.id.hours_tv);
            persons = itemView.findViewById(R.id.personsLayout);
        }
    }
}

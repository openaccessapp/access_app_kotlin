package app.downloadaccess.places.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import app.downloadaccess.places.R;
import app.downloadaccess.resources.models.Slot;

public class PlanVisitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Slot> dataList = new ArrayList<>();
    private AdapterCallback callback;
    private Context context;

    public PlanVisitAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            //            itemViewHolder.itemView.setOnClickListener(v -> {
//                callback.onItemClick(itemViewHolder.getAdapterPosition());
//            });
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false));

        } else {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_planner_header, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Slot slot = dataList.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.hourFromTo.setText(slot.getFrom() + " - " + slot.getTo());

            itemViewHolder.priorityText.setText(slot.getType().charAt(0) + "");
            itemViewHolder.occupiedMax.setText(slot.getOccupiedSlots() + "/" + slot.getMaxSlots());
            if (slot.isPlanned()) {
                itemViewHolder.mainLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                if (slot.getType().equals("Standard")) itemViewHolder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                else itemViewHolder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                itemViewHolder.hourFromTo.setTextColor(ContextCompat.getColor(context, R.color.white));
                itemViewHolder.occupiedMax.setTextColor(ContextCompat.getColor(context, R.color.white));
                itemViewHolder.checkIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
                itemViewHolder.personIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
            } else {
                itemViewHolder.mainLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                itemViewHolder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.disabled_tint));
                itemViewHolder.hourFromTo.setTextColor(ContextCompat.getColor(context, R.color.disabled_tint));
                itemViewHolder.occupiedMax.setTextColor(ContextCompat.getColor(context, R.color.disabled_tint));
                itemViewHolder.checkIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.grey));
                itemViewHolder.personIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.disabled_tint));
            }
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            try {
                cal.setTime(sdf.parse(slot.getFrom()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            headerViewHolder.shortDate.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime()));
            headerViewHolder.date.setText(cal.get(Calendar.DAY_OF_MONTH) + "th " + new SimpleDateFormat("MMM").format(cal.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).getViewType();
    }

    public void setDataList(List<Slot> items) {
        dataList = items;
    }

    public void addDataList(List<Slot> items) {
        if (items != null) {
            int start = dataList.size();
            dataList.addAll(items);
            notifyItemRangeInserted(start, items.size());
        }
    }

    public Context getContext() {
        return context;
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    private static final class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView hourFromTo, priorityText, occupiedMax;
        RelativeLayout mainLayout;
        ImageView personIcon, checkIcon;

        ItemViewHolder(View itemView) {
            super(itemView);

            personIcon = itemView.findViewById(R.id.personIconSlot);
            checkIcon = itemView.findViewById(R.id.checkIcon);
            mainLayout = itemView.findViewById(R.id.mainLayout);
            hourFromTo = itemView.findViewById(R.id.hourFromTo);
            priorityText = itemView.findViewById(R.id.priority_text);
            occupiedMax = itemView.findViewById(R.id.occupiedMax);
        }
    }

    private static final class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView shortDate, date;

        HeaderViewHolder(View itemView) {
            super(itemView);

            shortDate = itemView.findViewById(R.id.shortDay);
            date = itemView.findViewById(R.id.date);
        }
    }

    public interface AdapterCallback {
        void onItemClick(int position);
    }
}
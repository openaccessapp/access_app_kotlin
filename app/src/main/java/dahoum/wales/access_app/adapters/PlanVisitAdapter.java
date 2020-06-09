package dahoum.wales.access_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.models.Slot;
import dahoum.wales.access_app.stickyheaders.AdapterDataProvider;

public class PlanVisitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterDataProvider {

    private final List<Slot> dataList = new ArrayList<>();
    private AdapterCallback callback;
    private Context context;

    public PlanVisitAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            ItemViewHolder itemViewHolder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot, parent, false));
            itemViewHolder.itemView.setOnClickListener(v -> {
                callback.onItemClick(itemViewHolder.getAdapterPosition());
            });
            return itemViewHolder;

        } else {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_planner_header, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Slot slot = dataList.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            try {
                Calendar from = Calendar.getInstance();
                Calendar to = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                from.setTime(sdf.parse(slot.getFrom()));
                to.setTime(sdf.parse(slot.getTo()));
                itemViewHolder.hourFromTo.setText(from.get(Calendar.HOUR_OF_DAY) + ":" + from.get(Calendar.MINUTE) + " - " + to.get(Calendar.HOUR_OF_DAY) + ":" + to.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemViewHolder.priorityText.setText(slot.getType());
            itemViewHolder.occupiedMax.setText(slot.getOccupiedSlots() + "/" + slot.getMaxSlots());
            if (slot.getIsPlanned()) {
                itemViewHolder.linearLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                itemViewHolder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                itemViewHolder.hourFromTo.setTextColor(ContextCompat.getColor(context, R.color.white));
                itemViewHolder.occupiedMax.setTextColor(ContextCompat.getColor(context, R.color.white));
                itemViewHolder.checkIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
                itemViewHolder.personIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
            } else {
                itemViewHolder.linearLayout.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                itemViewHolder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.disabled_tint));
                itemViewHolder.hourFromTo.setTextColor(ContextCompat.getColor(context, R.color.disabled_tint));
                itemViewHolder.occupiedMax.setTextColor(ContextCompat.getColor(context, R.color.disabled_tint));
                itemViewHolder.checkIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.grey));
                itemViewHolder.personIcon.setImageTintList(ContextCompat.getColorStateList(context, R.color.disabled_tint));
            }
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
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

    @Override
    public List<Slot> getAdapterData() {
        return dataList;
    }

    public void setDataList(List<Slot> items) {
        dataList.clear();
        dataList.addAll(items);
        notifyDataSetChanged();
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

    public void deleteItem(int position) {
        mRecentlyDeletedItem = mListItems.get(position);
        mRecentlyDeletedItemPosition = position;
        notifyItemRemoved(position);
        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        View view = mActivity.findViewById(R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        mListItems.add(mRecentlyDeletedItemPosition,
                mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    private static final class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView hourFromTo, priorityText, occupiedMax;
        LinearLayout linearLayout;
        ImageView personIcon, checkIcon;

        ItemViewHolder(View itemView) {
            super(itemView);

            personIcon = itemView.findViewById(R.id.personIconSlot);
            checkIcon = itemView.findViewById(R.id.checkIcon);
            linearLayout = itemView.findViewById(R.id.linearLayout);
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
package dahoum.wales.access_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.models.Slot;
import dahoum.wales.access_app.stickyheaders.AdapterDataProvider;

public class PlanVisitAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterDataProvider {

    private final List<Slot> dataList = new ArrayList<>();
    private AdapterCallback callback;

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
//        final Slot slot = dataList.get(position);
//        if (holder instanceof ItemViewHolder) {
//            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
//            itemViewHolder.hourFromTo.setText(slot.getHourFromTo());
//            itemViewHolder.priorityText.setText(slot.getPriorityText());
//        } else if (holder instanceof HeaderViewHolder) {
//            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
//            headerViewHolder.shortDate.setText(slot.getShortDate());
//            headerViewHolder.date.setText(slot.getDate());
//        }
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

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    private static final class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView hourFromTo;
        TextView priorityText;

        ItemViewHolder(View itemView) {
            super(itemView);

            hourFromTo = itemView.findViewById(R.id.hourFromToSlot);
            priorityText = itemView.findViewById(R.id.priority_text);
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
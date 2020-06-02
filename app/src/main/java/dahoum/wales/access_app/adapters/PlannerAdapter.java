package dahoum.wales.access_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.models.Plan;
import dahoum.wales.access_app.stickyheaders.AdapterDataProvider;

public class PlannerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterDataProvider {

    private final List<Plan> dataList = new ArrayList<>();

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_planner, parent, false));
        } else {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_planner_header, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Plan item = dataList.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.placeName.setText(item.getPlaceName());
            itemViewHolder.priorityText.setText(item.getPriorityText());
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.shortDate.setText(item.getPlaceName());
            headerViewHolder.date.setText(item.getPriorityText());
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
    public List<Plan> getAdapterData() {
        return dataList;
    }

    public void setDataList(List<Plan> items) {
        dataList.clear();
        dataList.addAll(items);
        notifyDataSetChanged();
    }

    public void addDataList(List<Plan> items) {
        if (items != null) {
            int start = dataList.size();
            dataList.addAll(items);
            notifyItemRangeInserted(start, items.size());
        }
    }

    private static final class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView placeName;
        TextView priorityText;

        ItemViewHolder(View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.titlePlanner);
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
}
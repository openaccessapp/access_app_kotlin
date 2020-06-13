package dahoum.wales.access_app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.models.Visit;
import dahoum.wales.access_app.stickyheaders.AdapterDataProvider;

public class VisitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterDataProvider {

    private List<Visit> visitList = new ArrayList<>();

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
        final Visit visit = visitList.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            try {
                Calendar from = Calendar.getInstance();
                Calendar to = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                from.setTime(sdf.parse(visit.getStartTime()));
                to.setTime(sdf.parse(visit.getEndTime()));
                itemViewHolder.hourFrom.setText(from.get(Calendar.HOUR_OF_DAY) + ":" + from.get(Calendar.MINUTE));
                itemViewHolder.hourTo.setText(to.get(Calendar.HOUR_OF_DAY) + ":" + to.get(Calendar.MINUTE));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemViewHolder.priorityText.setText(visit.getType());
            itemViewHolder.occupiedMax.setText(visit.getOccupiedSlots() + "/" + visit.getMaxSlots());
        } else if (holder instanceof HeaderViewHolder) {Calendar cal = Calendar.getInstance();
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            try {
                cal.setTime(sdf.parse(visit.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            headerViewHolder.shortDate.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime()));
            headerViewHolder.date.setText(cal.get(Calendar.DAY_OF_MONTH) + "th " + new SimpleDateFormat("MMM").format(cal.getTime()));
        }
    }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return visitList.get(position).getViewType();
    }

    @Override
    public List<Visit> getAdapterData() {
        return visitList;
    }

    public void setDataList(List<Visit> items) {
        visitList = items;
    }

    private static final class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView hourFrom, hourTo, placeName, priorityText, occupiedMax;

        ItemViewHolder(View itemView) {
            super(itemView);

            hourFrom = itemView.findViewById(R.id.hourFrom);
            hourTo = itemView.findViewById(R.id.hourTo);
            placeName = itemView.findViewById(R.id.titlePlanner);
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
}
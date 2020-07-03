package app.downloadaccess.visitor.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import app.downloadaccess.resources.ShadowConstraintLayout;
import app.downloadaccess.resources.models.Visit;
import app.downloadaccess.visitor.R;
import app.downloadaccess.visitor.stickyheaders.AdapterDataProvider;

public class VisitsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements AdapterDataProvider {

    private List<Visit> visitList = new ArrayList<>();
    private VisitsAdapter.AdapterCallback callback;
    private Activity mActivity;

    public VisitsAdapter(Activity activity) {
        mActivity = activity;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_planner_header, parent, false));
        } else {
            ItemViewHolder itemViewHolder = new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_planner, parent, false));

            //standard is blue color, also is type 2
            if (viewType != 0)
                itemViewHolder.priorityText.setBackgroundTintList(ContextCompat.getColorStateList(mActivity, R.color.colorPrimary));

            itemViewHolder.visitedSlot.setOnClickListener(v -> callback.onItemClick(itemViewHolder.getAdapterPosition()));
            return itemViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final Visit visit = visitList.get(position);
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.hourFrom.setText(visit.getStartTime());
            itemViewHolder.hourTo.setText(visit.getEndTime());
            itemViewHolder.priorityText.setText(visit.getType());
            itemViewHolder.occupiedMax.setText(visit.getOccupiedSlots() + "/" + visit.getMaxSlots());
            itemViewHolder.visitors.setText(visit.getVisitors() + "");
            itemViewHolder.placeName.setText(visit.getName());
        } else if (holder instanceof HeaderViewHolder) {
            Calendar cal = Calendar.getInstance();
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
            try {
                cal.setTime(sdf.parse(visit.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            headerViewHolder.shortDate.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime()));
            headerViewHolder.date.setText(cal.get(Calendar.DAY_OF_MONTH) + "th " + new SimpleDateFormat("MMM").format(cal.getTime()));
        }
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return visitList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Visit item = visitList.get(position);
        if (item.getViewType() == 1) return 1;
        else if (item.getType().equals("Standard")) return 2;
        return 0;
    }

    @Override
    public List<Visit> getAdapterData() {
        return visitList;
    }

    public void setDataList(List<Visit> items) {
        visitList = items;
    }

    private static final class ItemViewHolder extends RecyclerView.ViewHolder {

        ShadowConstraintLayout visitedSlot;
        MaterialButton priorityText;
        TextView hourFrom, hourTo, placeName, occupiedMax, visitors;

        ItemViewHolder(View itemView) {
            super(itemView);

            hourFrom = itemView.findViewById(R.id.hourFrom);
            hourTo = itemView.findViewById(R.id.hourTo);
            placeName = itemView.findViewById(R.id.titlePlanner);
            priorityText = itemView.findViewById(R.id.priority_text);
            occupiedMax = itemView.findViewById(R.id.occupiedMax);
            visitors = itemView.findViewById(R.id.personCountTv);
            visitedSlot = itemView.findViewById(R.id.visitedSlot);

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
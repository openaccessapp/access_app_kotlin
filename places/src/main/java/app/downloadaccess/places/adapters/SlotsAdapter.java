package app.downloadaccess.places.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import app.downloadaccess.places.R;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Header;
import app.downloadaccess.resources.models.Slot;

public class SlotsAdapter extends RecyclerView.Adapter<SlotsAdapter.HeaderViewHolder> {

    private List<Header> headerList;
    private AdapterCallback callback;
    private Context context;
    private static int currentPosition = 0;

    public SlotsAdapter(Context context) {
        this.context = context;
    }

    @NotNull
    @Override
    public SlotsAdapter.HeaderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HeaderViewHolder holder = new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_slot_header, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(SlotsAdapter.HeaderViewHolder holder, final int position) {
        final Header header = headerList.get(position);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            cal.setTime(sdf.parse(header.getKey()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.shortDate.setText(new SimpleDateFormat("EEE", Locale.getDefault()).format(cal.getTime()));
        holder.date.setText(cal.get(Calendar.DAY_OF_MONTH) + "th " + new SimpleDateFormat("MMM").format(cal.getTime()));

        // start for child loading
        int noOfChildTextViews = holder.linearLayout_childItems.getChildCount();
        for (int index = 0; index < noOfChildTextViews; index++) {
            View currentTextView = holder.linearLayout_childItems.getChildAt(index);
            currentTextView.setVisibility(View.VISIBLE);
        }

        int noOfChild = header.getSlots().size();
        if (noOfChild < noOfChildTextViews) {
            for (int index = noOfChild; index < noOfChildTextViews; index++) {
                View currentTextView = holder.linearLayout_childItems.getChildAt(index);
                currentTextView.setVisibility(View.GONE);
            }
        }
        for (int textViewIndex = 0; textViewIndex < noOfChild; textViewIndex++) {
            Slot slot = header.getSlots().get(textViewIndex);
            View currentView = holder.linearLayout_childItems.getChildAt(textViewIndex);
            ((TextView) currentView.findViewById(R.id.hourFromTo)).setText(slot.getFrom() + " - " + slot.getTo());

            ((TextView) currentView.findViewById(R.id.priority_text)).setText(slot.getType().charAt(0) + "");
            ((TextView) currentView.findViewById(R.id.occupiedMax)).setText(slot.getOccupiedSlots() + "/" + slot.getMaxSlots());
            if (slot.isPlanned()) {
                currentView.findViewById(R.id.mainLayout).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorAccent));
                if (slot.getType().equals("Standard"))
                    ((TextView) currentView.findViewById(R.id.priority_text)).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
                else
                    ((TextView) currentView.findViewById(R.id.priority_text)).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.green));
                ((TextView) currentView.findViewById(R.id.hourFromTo)).setTextColor(ContextCompat.getColor(context, R.color.white));
                ((TextView) currentView.findViewById(R.id.occupiedMax)).setTextColor(ContextCompat.getColor(context, R.color.white));
                ((ImageView) currentView.findViewById(R.id.checkIcon)).setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
                ((ImageView) currentView.findViewById(R.id.personIconSlot)).setImageTintList(ContextCompat.getColorStateList(context, R.color.white));
            } else {
                currentView.findViewById(R.id.mainLayout).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
                ((TextView) currentView.findViewById(R.id.priority_text)).setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.disabled_tint));
                ((TextView) currentView.findViewById(R.id.hourFromTo)).setTextColor(ContextCompat.getColor(context, R.color.disabled_tint));
                ((TextView) currentView.findViewById(R.id.occupiedMax)).setTextColor(ContextCompat.getColor(context, R.color.disabled_tint));
                ((ImageView) currentView.findViewById(R.id.checkIcon)).setImageTintList(ContextCompat.getColorStateList(context, R.color.grey));
                ((ImageView) currentView.findViewById(R.id.personIconSlot)).setImageTintList(ContextCompat.getColorStateList(context, R.color.disabled_tint));
            }
            int finalTextViewIndex = textViewIndex;
            currentView.setOnClickListener(view -> {
                callback.onItemClick(position, finalTextViewIndex);
            });
        }

    }

    @Override
    public int getItemCount() {
        return headerList.size();
    }

    public void setHeaders(List<Header> headerList) {
        this.headerList = headerList;
    }

    public Context getContext() {
        return context;
    }

    public void setAdapterCallback(AdapterCallback callback) {
        this.callback = callback;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView shortDate, date;
        LinearLayout headerView, linearLayout_childItems;

        HeaderViewHolder(View itemView) {
            super(itemView);

            shortDate = itemView.findViewById(R.id.shortDay);
            date = itemView.findViewById(R.id.date);
            headerView = itemView.findViewById(R.id.headerView);
            linearLayout_childItems = itemView.findViewById(R.id.ll_child_items);
            linearLayout_childItems.setVisibility(View.GONE);

            int intMaxNoOfChild = 0;
            for (int index = 0; index < headerList.size(); index++) {
                int intMaxSizeTemp = headerList.get(index).getSlots().size();
                if (intMaxSizeTemp > intMaxNoOfChild) intMaxNoOfChild = intMaxSizeTemp;
            }
            for (int indexView = 0; indexView < intMaxNoOfChild; indexView++) {
                View slotView = LayoutInflater.from(context).inflate(R.layout.item_slot, null);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, Utils.dpToPx(context, 10.0f), 0, 0);
                linearLayout_childItems.addView(slotView, layoutParams);
            }
            headerView.setOnClickListener(v -> {
                if (linearLayout_childItems.getVisibility() == View.VISIBLE) {
                    linearLayout_childItems.setVisibility(View.GONE);
                } else {
                    linearLayout_childItems.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public interface AdapterCallback {
        void onItemClick(int headerPosition, int childPosition);
    }
}
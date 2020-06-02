package dahoum.wales.access_app.navigation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;
import dahoum.wales.access_app.adapters.PlannerAdapter;
import dahoum.wales.access_app.models.Plan;
import dahoum.wales.access_app.stickyheaders.StickyLinearLayoutManager;

public class PlannerFragment extends Fragment {

    private RecyclerView recyclerView;
    private PlannerAdapter adapter;
    private List<Plan> plans = new ArrayList<>();

    public PlannerFragment() {
        // Required empty public constructor
    }

    public static PlannerFragment newInstance() {
        PlannerFragment fragment = new PlannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });

        recyclerView = view.findViewById(R.id.recyclerViewPlanner);
        getData();
        adapter = new PlannerAdapter();
        adapter.setDataList(plans);
        StickyLinearLayoutManager layoutManager = new StickyLinearLayoutManager(view.getContext(), adapter) {
            @Override
            public boolean isAutoMeasureEnabled() {
                return true;
            }

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                RecyclerView.SmoothScroller smoothScroller = new TopSmoothScroller(recyclerView.getContext());
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

            class TopSmoothScroller extends LinearSmoothScroller {

                TopSmoothScroller(Context context) {
                    super(context);
                }

                @Override
                public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
                    return boxStart - viewStart;
                }
            }
        };
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        layoutManager.setStickyHeaderListener(new StickyLinearLayoutManager.StickyHeaderListener() {
            @Override
            public void headerAttached(View headerView, int adapterPosition) {
                Log.d("StickyHeader", "Header Attached : " + adapterPosition);
            }

            @Override
            public void headerDetached(View headerView, int adapterPosition) {
                Log.d("StickyHeader", "Header Detached : " + adapterPosition);
            }
        });
    }

    private void getData() {
        plans.add(new Plan("MON", "27th May", 1));
        plans.add(new Plan("Central Park", "Priority", 0));
        plans.add(new Plan("History Museum", "Normal", 0));
        plans.add(new Plan("South Park", "Normal", 0));
        plans.add(new Plan("TUE", "28th May", 1));
        plans.add(new Plan("Central Park", "Priority", 0));
        plans.add(new Plan("History Museum", "Normal", 0));
        plans.add(new Plan("South Park", "Normal", 0));
        plans.add(new Plan("WED", "29th May", 1));
        plans.add(new Plan("Central Park", "Priority", 0));
        plans.add(new Plan("History Museum", "Normal", 0));
        plans.add(new Plan("South Park", "Normal", 0));
        plans.add(new Plan("THU", "30th May", 1));
        plans.add(new Plan("Central Park", "Priority", 0));
        plans.add(new Plan("History Museum", "Normal", 0));
        plans.add(new Plan("South Park", "Normal", 0));

    }
}

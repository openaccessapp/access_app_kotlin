package app.downloadaccess.visitor.navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.downloadaccess.resources.RecyclerViewEmptySupport;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Visit;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import app.downloadaccess.visitor.MainActivity;
import app.downloadaccess.visitor.ProfileActivity;
import app.downloadaccess.visitor.R;
import app.downloadaccess.visitor.adapters.VisitsAdapter;
import app.downloadaccess.visitor.navigation.child.BookingDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlannerFragment extends Fragment implements VisitsAdapter.AdapterCallback, View.OnClickListener {

    private static final String TAG = PlannerFragment.class.getSimpleName();
    private RecyclerViewEmptySupport recyclerView;
    private VisitsAdapter adapter;
    private List<Visit> visits = new ArrayList<>();
    private RetrofitService retrofitService;
    private SharedPreferences prefs;

    private BookingDialog dialog;

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
    public void onResume() {
        super.onResume();
        getAllVisits();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.dialog = new BookingDialog(getView(), getContext(), getActivity(), t -> getAllVisits());
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
        view.findViewById(R.id.plan_visit_button).setOnClickListener(v -> {
            ((MainActivity) getActivity()).bottomNavigationView.setSelectedItemId(R.id.placesNav);
        });
        prefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
        recyclerView = view.findViewById(R.id.recyclerViewPlanner);
        recyclerView.setEmptyView(view.findViewById(R.id.emptyViewPlanner));
        adapter = new VisitsAdapter(getActivity(), visits);
        adapter.setAdapterCallback(this);
//        StickyLinearLayoutManager layoutManager = new StickyLinearLayoutManager(view.getContext(), adapter) {
//            @Override
//            public boolean isAutoMeasureEnabled() {
//                return true;
//            }
//
//            @Override
//            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//                RecyclerView.SmoothScroller smoothScroller = new TopSmoothScroller(recyclerView.getContext());
//                smoothScroller.setTargetPosition(position);
//                startSmoothScroll(smoothScroller);
//            }
//
//            class TopSmoothScroller extends LinearSmoothScroller {
//
//                TopSmoothScroller(Context context) {
//                    super(context);
//                }
//
//                @Override
//                public int calculateDtToFit(int viewStart, int viewEnd, int boxStart, int boxEnd, int snapPreference) {
//                    return boxStart - viewStart;
//                }
//            }
//        };
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
//        layoutManager.setStickyHeaderListener(new StickyLinearLayoutManager.StickyHeaderListener() {
//            @Override
//            public void headerAttached(View headerView, int adapterPosition) {
//                Log.d("StickyHeader", "Header Attached : " + adapterPosition);
//            }
//
//            @Override
//            public void headerDetached(View headerView, int adapterPosition) {
//                Log.d("StickyHeader", "Header Detached : " + adapterPosition);
//            }
//        });
    }

    public void getAllVisits() {
        retrofitService.getUserVisits(Utils.getJwtToken(getContext()), prefs.getString("userId", null)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("visits") != null) {
                    visits.clear();
                    Gson gson = new Gson();
                    Set<Map.Entry<String, JsonElement>> entries = response.body().getAsJsonObject().get("visits").getAsJsonObject().entrySet();
                    for (Map.Entry<String, JsonElement> entry : entries) {
                        visits.add(new Visit(entry.getKey(), 1));
                        visits.addAll(gson.fromJson(entry.getValue().getAsJsonArray(), new TypeToken<ArrayList<Visit>>() {
                        }.getType()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Visit visit = visits.get(position);

        dialog.itemClick(this, visit.getSlotId(), visit.getOccupiedSlots(), visit.getMaxSlots(),
                visit.getType(), visit.getStartTime(), visit.getEndTime(), visit.getName(), visit.getVisitors(),
                t -> dialog.postPlanVisit(visit.getSlotId(), Integer.parseInt(t)));
    }

    @Override
    public void onClick(View v) {
        dialog.click(v);
    }

}

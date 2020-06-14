package dahoum.wales.access_app.navigation;

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
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dahoum.wales.access_app.MainActivity2;
import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;
import dahoum.wales.access_app.RecyclerViewEmptySupport;
import dahoum.wales.access_app.adapters.VisitsAdapter;
import dahoum.wales.access_app.models.Visit;
import dahoum.wales.access_app.network.RetrofitClientInstance;
import dahoum.wales.access_app.network.RetrofitService;
import dahoum.wales.access_app.stickyheaders.StickyLinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlannerFragment extends Fragment {

    private static final String TAG = PlannerFragment.class.getSimpleName();
    private RecyclerViewEmptySupport recyclerView;
    private VisitsAdapter adapter;
    private List<Visit> visits = new ArrayList<>();
    private RetrofitService retrofitService;
    private SharedPreferences prefs;

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
        view.findViewById(R.id.plan_visit_button).setOnClickListener(v -> {
            ((MainActivity2) getActivity()).bottomNavigationView.setSelectedItemId(R.id.placesNav);
        });
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        recyclerView = view.findViewById(R.id.recyclerViewPlanner);
        recyclerView.setEmptyView(view.findViewById(R.id.emptyViewPlanner));
        adapter = new VisitsAdapter();
        adapter.setDataList(visits);
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

    public void getAllVisits() {
        retrofitService.getUserVisits(prefs.getString("userId", null)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body().get("visits").getAsJsonObject() != null) {
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
}

package dahoum.wales.access_app.navigation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class PlannerFragment extends Fragment implements VisitsAdapter.AdapterCallback, View.OnClickListener  {

    private static final String TAG = PlannerFragment.class.getSimpleName();
    private RecyclerViewEmptySupport recyclerView;
    private VisitsAdapter adapter;
    private List<Visit> visits = new ArrayList<>();
    private RetrofitService retrofitService;
    private SharedPreferences prefs;

    private MaterialButton[] btn = new MaterialButton[7];
    private MaterialButton btn_unfocus;
    private int[] btn_id = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven};

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
        adapter = new VisitsAdapter(getActivity());
        adapter.setDataList(visits);
        adapter.setAdapterCallback(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                setFocus(btn_unfocus, btn[0]);
                break;
            case R.id.two:
                setFocus(btn_unfocus, btn[1]);
                break;
            case R.id.three:
                setFocus(btn_unfocus, btn[2]);
                break;
            case R.id.four:
                setFocus(btn_unfocus, btn[3]);
                break;
            case R.id.five:
                setFocus(btn_unfocus, btn[4]);
                break;
            case R.id.six:
                setFocus(btn_unfocus, btn[5]);
                break;
            case R.id.seven:
                setFocus(btn_unfocus, btn[6]);
                break;
        }
    }
    private void setFocus(MaterialButton btn_unfocus, MaterialButton btn_focus) {
        btn_unfocus.setTextColor(ContextCompat.getColor(getContext(), R.color.text_grey));
        btn_unfocus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.grey));
        btn_focus.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        btn_focus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
        this.btn_unfocus = btn_focus;
    }

    @Override
    public void onItemClick(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        Visit visit = visits.get(position);

        TextView occupied = dialogView.findViewById(R.id.occupiedMax);
        occupied.setText(visit.getOccupiedSlots() + "/" + visit.getMaxSlots());

        MaterialButton type = dialogView.findViewById(R.id.priority_text);
        type.setText(visit.getType());
        if (visit.getType().equals("Standard"))
            type.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));

        TextView hourFrom = dialogView.findViewById(R.id.hourFrom);
        hourFrom.setText(visit.getStartTime());

        TextView hourTo = dialogView.findViewById(R.id.hourTo);
        hourTo.setText(visit.getEndTime());

        TextView placeName = dialogView.findViewById(R.id.titlePlanner);
        placeName.setText(visit.getName());

        for (int i = 0; i < btn.length; i++) {
            btn[i] = dialogView.findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }

        btn_unfocus = btn[0];

        if (visit.getVisitors() > 0) {
            int btnId = visit.getVisitors() - 1;
            setFocus(btn_unfocus, btn[btnId]);
        }


        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        saveButton.setOnClickListener(v -> {
            postPlanVisit(visit.getSlotId(), Integer.parseInt(btn_unfocus.getText().toString()));
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
    private void postPlanVisit(String slotId, int visitorsCount) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("slotId", slotId);
        body.put("visitors", visitorsCount);
        retrofitService.planVisit(prefs.getString("userId", null), body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.code() != 204 && response.errorBody() != null) {
                    try {
                        JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                        Toast.makeText(getContext(), errorObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getAllVisits();
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

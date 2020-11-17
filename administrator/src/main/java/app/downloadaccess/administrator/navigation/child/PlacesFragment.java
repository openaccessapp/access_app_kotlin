package app.downloadaccess.administrator.navigation.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.downloadaccess.administrator.R;
import app.downloadaccess.administrator.adapters.PlacesAdapter;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesFragment extends Fragment implements PlacesAdapter.PlacesCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = PlacesFragment.class.getSimpleName();
    private FragmentCallback callback;
    private ChipGroup chipGroup;
    private RetrofitService retrofitService;
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private List<Place> places;
    private SharedPreferences prefs;
    private int currentPage = 0;
    private int visibleThreshold = 7;
    private boolean isLoading = false;
    private boolean reachedEnd = false;
    private int checkedId = -1;
    private Boolean currentChipState;
    private SwipeRefreshLayout swipeRefreshLayout;

    public PlacesFragment() {
        // Required empty public constructor
    }

    public static PlacesFragment newInstance() {
        PlacesFragment fragment = new PlacesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "Attached Places Fragment");
    }

    public void setListener(FragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chipGroup = view.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            this.checkedId = checkedId;
            Chip chip = group.findViewById(checkedId);
            if (chip.getText().equals("Approved")) {
                currentChipState = true;
            } else if (chip.getText().equals("Non-approved")) {
                currentChipState = false;
            } else if (chip.getText().equals("All")) {
                currentChipState = null;
            }
            getAllPlaces(currentChipState);
        });

        places = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
        recyclerView = view.findViewById(R.id.placesRecyclerView);
        adapter = new PlacesAdapter(getContext(), places);
        adapter.setAdapterCallback(this);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == places.size() - 1) {
                        //bottom of list!
                        Log.d(TAG, "Load more!");
                        isLoading = true;
                    }
                }
            }
        });

        prefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (key.equals("userId") && places.isEmpty()) {
                if (checkedId == -1) {
                    checkedId = chipGroup.getCheckedChipId();
                }
                Chip chip = chipGroup.findViewById(chipGroup.getCheckedChipId());
                if (chip.getText().equals("Approved")) {
                    currentChipState = true;
                } else if (chip.getText().equals("Non-approved")) {
                    currentChipState = false;
                } else if (chip.getText().equals("All")) {
                    currentChipState = null;
                }
                getAllPlaces(currentChipState);
            }
        });
        if (prefs.getString("userId", null) != null) {
            if (checkedId == -1) {
                checkedId = chipGroup.getCheckedChipId();
            }
            chipGroup.check(checkedId);
            Chip chip = chipGroup.findViewById(checkedId);
            if (chip.getText().equals("Approved")) {
                currentChipState = true;
            } else if (chip.getText().equals("Non-approved")) {
                currentChipState = false;
            } else if (chip.getText().equals("All")) {
                currentChipState = null;
            }
            getAllPlaces(currentChipState);
        }
    }

    @Override
    public void onPlaceClick(int position) {
        callback.onPlaceClicked(places.get(position));
    }

    @Override
    public void onWebsiteClick(int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(places.get(position).getWww()));
        startActivity(intent);
    }

    private void reset() {
        currentPage = 0;
        places.clear();
        adapter.notifyDataSetChanged();
        getAllPlaces(currentChipState);
    }

    void getAllPlaces(Boolean isApproved) {
        isLoading = true;
        HashMap<String, Object> body = new HashMap<>();
        if (isApproved != null) {
            body.put("approved", isApproved);
        }
        retrofitService.getAllPlaces(Utils.getJwtToken(getContext()), prefs.getString("userId", null), body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                swipeRefreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                if (response.body() != null) {
                    ArrayList<Place> newPlaces = gson.fromJson(response.body().get("places"), new TypeToken<ArrayList<Place>>() {
                    }.getType());

                    if (newPlaces.isEmpty()) {
                        reachedEnd = true;
                        return;
                    }

                    places.clear();
                    places.addAll(newPlaces);
                    adapter.notifyDataSetChanged();
                }
                isLoading = false;
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Log.d(TAG, t.getLocalizedMessage());
                isLoading = false;
            }
        });

    }

    @Override
    public void onRefresh() {
        reset();
    }

}

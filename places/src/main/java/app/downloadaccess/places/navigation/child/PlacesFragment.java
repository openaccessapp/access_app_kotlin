package app.downloadaccess.places.navigation.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.downloadaccess.places.R;
import app.downloadaccess.places.adapters.PlacesAdapter;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class PlacesFragment extends Fragment implements PlacesAdapter.PlacesCallback, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = PlacesFragment.class.getSimpleName();
    private FragmentCallback callback;
    private ChipGroup chipGroup;
    private RetrofitService retrofitService;
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private List<Place> places;
    private SharedPreferences prefs;
    private MaterialButton addPlaceButton;
    private Integer currentPage = 0;
    private Integer visibleThreshold = 7;
    private Boolean isLoading = false;
    private Boolean reachedEnd = false;
    private String search = null;
    private Integer typeId = null;
    private Boolean onlyFavourites = false;
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
        addPlaceButton = view.findViewById(R.id.add_place_button);
        addPlaceButton.setOnClickListener(v -> {
            callback.onAddPlaceClicked(null);
        });
        chipGroup = view.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip.getText().equals("Favourites")) {
                onlyFavourites = true;
                typeId = null;
            } else if (chip.getText().equals("All")) {
                onlyFavourites = null;
                typeId = null;
            } else if (chip.getText().equals("Parks")) {
                onlyFavourites = null;
                typeId = 0;
            } else if (chip.getText().equals("Museums")) {
                onlyFavourites = null;
                typeId = 1;
            }
            reset();
        });

        places = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
        recyclerView = view.findViewById(R.id.placesRecyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (llm != null) {
                    int lastItem = llm.findLastVisibleItemPosition();
                    if (lastItem > (currentPage * visibleThreshold) - 4 && !isLoading) {
                        getAllPlaces();
                    }
                }

            }
        });
        adapter = new PlacesAdapter(getContext(), places);
        adapter.setAdapterCallback(this);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        prefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (key.equals("userId") && places.isEmpty()) {
                getAllPlaces();
            }
        });
//        if (prefs.getString("userId", null) != null) {
//            getAllPlaces();
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        reset();
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
        getAllPlaces();
    }

    @Override
    public void onFavouriteClick(int position) {
        places.get(position).setFavourite(!places.get(position).isFavourite());
        adapter.notifyDataSetChanged();
        retrofitService.addRemoveFavourite(Utils.getJwtToken(getContext()), prefs.getString("userId", null), places.get(position).getId()).enqueue(new Callback<JsonObject>() {
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
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getAllPlaces() {
        isLoading = true;
        HashMap<String, Object> map = new HashMap<>();
        map.put("own", true);
//        if (currentPage != null) {
//            map.put("skip", currentPage++ * visibleThreshold);
//        }
//        if (visibleThreshold != null) {
//            map.put("load", visibleThreshold);
//        }
        if (search != null) {
            map.put("name", search);
        }
        if (typeId != null) {
            map.put("typeId", typeId);
        }
        if (onlyFavourites != null) {
            map.put("onlyFavourites", onlyFavourites);
        }

        retrofitService.getAllPlaces(Utils.getJwtToken(getContext()), prefs.getString("userId", null), map).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                swipeRefreshLayout.setRefreshing(false);
                Gson gson = new Gson();
                if (response.body().get("places").getAsJsonArray() != null) {
                    ArrayList<Place> newPlaces = gson.fromJson(response.body().get("places"), new TypeToken<ArrayList<Place>>() {
                    }.getType());

                    if (newPlaces.isEmpty()) {
                        return;
                    }

                    places.clear();
                    places.addAll(newPlaces);

                    adapter.setDataList(places);
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

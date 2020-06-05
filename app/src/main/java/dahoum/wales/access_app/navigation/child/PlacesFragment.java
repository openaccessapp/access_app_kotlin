package dahoum.wales.access_app.navigation.child;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;
import dahoum.wales.access_app.adapters.PlacesAdapter;
import dahoum.wales.access_app.models.Place;
import dahoum.wales.access_app.network.RetrofitClientInstance;
import dahoum.wales.access_app.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlacesFragment extends Fragment implements PlacesAdapter.PlacesCallback {

    private static final String TAG = PlacesFragment.class.getSimpleName();
    private FragmentCallback callback;
    private ChipGroup chipGroup;
    private RetrofitService retrofitService;
    private RecyclerView recyclerView;
    private PlacesAdapter adapter;
    private List<Place> places = new ArrayList<>();
    private SharedPreferences prefs;
    private RelativeLayout loadingPanel;

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
        loadingPanel = view.findViewById(R.id.loadingPanel);
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
        chipGroup = view.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            if (chip.getText().equals("Favourites")) {
                adapter.getFilter().filter("fav");
            } else if (chip.getText().equals("All")) {
                adapter.getFilter().filter("all");
            } else {
                adapter.getFilter().filter(chip.getText());
            }
        });

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        recyclerView = view.findViewById(R.id.placesRecyclerView);
        adapter = new PlacesAdapter(getContext(), places);
        adapter.setAdapterCallback(this);
        LinearLayoutManager manager = new LinearLayoutManager(view.getContext());
        manager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            if (key.equals("userId") && places.isEmpty()) {
                getAllPlaces();
            }
        });
        if (prefs.getString("userId", null) != null) {
            getAllPlaces();
        }
    }

    @Override
    public void onFavouriteClick(ImageView imageView, int position) {
        places.get(position).setIsFavourite(!places.get(position).getIsFavourite());
        adapter.notifyDataSetChanged();
        retrofitService.addRemoveFavourite(prefs.getString("userId", null), places.get(position).getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful() && response.errorBody() != null) {
                    Toast.makeText(getContext(), response.errorBody().toString(), Toast.LENGTH_SHORT).show();
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
    public void onPlaceClick(int position) {
        callback.onPlaceClicked(position);
    }

    private void getAllPlaces() {
        loadingPanel.setVisibility(View.GONE);
        retrofitService.getAllPlaces(prefs.getString("userId", null)).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                Gson gson = new Gson();
                if (response.body().get("places").getAsJsonArray() != null) {
                    places = gson.fromJson(response.body().get("places"), new TypeToken<ArrayList<Place>>() {
                    }.getType());
                    adapter.setDataLists(places);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
    }
}

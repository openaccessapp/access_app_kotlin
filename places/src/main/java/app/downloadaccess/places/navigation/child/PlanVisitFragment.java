package app.downloadaccess.places.navigation.child;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import app.downloadaccess.places.R;
import app.downloadaccess.places.adapters.PlanVisitAdapter;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.models.Slot;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanVisitFragment extends Fragment implements PlanVisitAdapter.AdapterCallback {

    private static final String TAG = PlanVisitFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private PlanVisitAdapter adapter;
    private List<Slot> slots = new ArrayList<>();
    private FragmentCallback callback;
    private Place place;
    private TextView placeName, placeDesc, websiteTv;
    private ImageView image;
    private RetrofitService retrofitService;
    private SharedPreferences prefs;
    private BookingDialog dialog;

    public PlanVisitFragment() {
        // Required empty public constructor
    }

    public static PlanVisitFragment newInstance(Place place) {
        PlanVisitFragment fragment = new PlanVisitFragment();
        Bundle args = new Bundle();
        args.putSerializable("place", place);
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(FragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_visit, container, false);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "Attached PlanVisitFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        place = (Place) getArguments().getSerializable("place");
        dialog = new BookingDialog(getActivity(), place);
        TextView button = view.findViewById(R.id.addSlot);
        prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        View goBackButton = view.findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> getParentFragment().getChildFragmentManager().popBackStack());

        OnBackPressedCallback backButton = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backButton);
        ConstraintLayout placeBox = view.findViewById(R.id.placeBox);
        placeBox.setOnClickListener(v -> {
            callback.editPlace(place);
        });
        setupPlace(view);
        button.setOnClickListener(v -> {
            dialog.setupDialog();
        });

        recyclerView = view.findViewById(R.id.recyclerPlanVisit);
        adapter = new PlanVisitAdapter(view.getContext());
        adapter.setDataList(slots);
        adapter.setAdapterCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        getSlotsPlace();
    }

    private void setupPlace(View view) {
        placeName = view.findViewById(R.id.placeName);
        placeName.setText(place.getName());
        placeDesc = view.findViewById(R.id.placeDesc);
        placeDesc.setText(place.getDescription());
        websiteTv = view.findViewById(R.id.websiteTv);
        websiteTv.setText(place.getWww());
        image = view.findViewById(R.id.image);
        Picasso.get().load(RetrofitClientInstance.BASE_URL + "/api/image/" + place.getId()).into(image);
    }

    private void getSlotsPlace() {
        retrofitService.getSlotsPlace(prefs.getString("userId", null), place.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.code() != 204 && response.errorBody() != null) {
                    try {
                        JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                        Toast.makeText(getContext(), errorObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.body().get("slots").getAsJsonObject() != null) {
                    slots.clear();
                    adapter.notifyDataSetChanged();
                    Gson gson = new Gson();
                    Set<Map.Entry<String, JsonElement>> entries = response.body().getAsJsonObject().get("slots").getAsJsonObject().entrySet();
                    for (Map.Entry<String, JsonElement> entry : entries) {
                        slots.add(new Slot(entry.getKey(), 1));
                        slots.addAll(gson.fromJson(entry.getValue().getAsJsonArray(), new TypeToken<ArrayList<Slot>>() {
                        }.getType()));
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Slot slot = slots.get(position);
    }
}

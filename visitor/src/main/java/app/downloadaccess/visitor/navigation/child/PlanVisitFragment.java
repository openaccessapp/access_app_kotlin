package app.downloadaccess.visitor.navigation.child;

import android.content.Context;
import android.content.Intent;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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

import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.models.Slot;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import app.downloadaccess.visitor.Consumer;
import app.downloadaccess.visitor.ProfileActivity;
import app.downloadaccess.visitor.R;
import app.downloadaccess.visitor.adapters.PlanVisitAdapter;
import app.downloadaccess.visitor.adapters.SwipeToDeleteCallback;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanVisitFragment extends Fragment implements PlanVisitAdapter.AdapterCallback, View.OnClickListener {

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

        this.dialog = new BookingDialog(getView(), getContext(), getActivity(), new Consumer<Void>() {
            public void accept(Void t) {
                getSlotsPlace();
            }
        });
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
        View placeBox = view.findViewById(R.id.place_card);
        placeBox.setOnClickListener(v -> {
            callback.onInfoClicked(place);
        });
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
        place = (Place) getArguments().getSerializable("place");
        setupPlace(view);

        recyclerView = view.findViewById(R.id.recyclerPlanVisit);
        adapter = new PlanVisitAdapter(getActivity());
        adapter.setDataList(slots);
        adapter.setAdapterCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

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
        if (place.getWww() != null && !place.getWww().isEmpty()) {
            view.findViewById(R.id.locationLayout).setVisibility(View.VISIBLE);
            websiteTv.setText(place.getWww());
        } else {
            view.findViewById(R.id.locationLayout).setVisibility(View.GONE);
        }
        image = view.findViewById(R.id.image);
        Picasso.get().load(RetrofitClientInstance.BASE_URL + "/api/image/" + place.getId()).into(image);
    }

    private void getSlotsPlace() {
        retrofitService.getSlotsPlace(Utils.getJwtToken(getContext()), prefs.getString("userId", null), place.getId()).enqueue(new Callback<JsonObject>() {
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
                        List<Slot> slots = gson.fromJson(entry.getValue().getAsJsonArray(), new TypeToken<ArrayList<Slot>>() {
                        }.getType());
                        //1 = header
                        //10 = standard
                        //11 = standard planned
                        //12 = standard disabled
                        //20 = priority
                        //21 = priority planned
                        //22 = priority disabled
                        int plannedSlots = 0;
                        for (Slot slot : slots) {
                            if (slot.getType().equals("Standard")) slot.setViewType(10);
                            else slot.setViewType(20);
                            if (slot.isPlanned()) {
                                plannedSlots++;
                                slot.setViewType(slot.getViewType() + 1);
                            } else if (slot.getOccupiedSlots().equals(slot.getMaxSlots()))
                                slot.setViewType(slot.getViewType() + 2);
                        }
                        if (plannedSlots >= 2) for (Slot slot : slots) {
                            if (slot.getViewType() % 10 == 0)
                                slot.setViewType(slot.getViewType() + 2);
                        }
                        ;
                        PlanVisitFragment.this.slots.addAll(slots);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                if (t.getLocalizedMessage() != null) {
                    Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        Slot slot = slots.get(position);
        dialog.itemClick(this, slot.getId(), slot.getOccupiedSlots(), slot.getMaxSlots(),
                slot.getType(), slot.getFrom(), slot.getTo(), place.getName(), slot.getFriends(),
                new Consumer<String>() {
                    public void accept(String t) {
                        dialog.postPlanVisit(slot.getId(), Integer.parseInt(t));
                    }
                });
    }

    @Override
    public void onClick(View v) {
        dialog.click(v);
    }
}

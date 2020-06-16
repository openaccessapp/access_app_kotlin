package dahoum.wales.access_app.navigation.child;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;
import dahoum.wales.access_app.adapters.PlanVisitAdapter;
import dahoum.wales.access_app.adapters.SwipeToDeleteCallback;
import dahoum.wales.access_app.models.Place;
import dahoum.wales.access_app.models.Slot;
import dahoum.wales.access_app.network.RetrofitClientInstance;
import dahoum.wales.access_app.network.RetrofitService;
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
    private MaterialButton[] btn = new MaterialButton[7];
    private MaterialButton btn_unfocus;
    private int[] btn_id = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven};

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

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
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
        Picasso.get().load("http://80.100.38.7:3001/api/image/" + place.getId()).into(image);
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
                            if (slot.getIsPlanned()) {
                                plannedSlots++;
                                slot.setViewType(slot.getViewType()+1);
                            }
                        }
                        if (plannedSlots >= 2) for (Slot slot : slots) {
                            if (slot.getViewType() % 10 == 0) slot.setViewType(slot.getViewType() + 2);
                        };
                        PlanVisitFragment.this.slots.addAll(slots);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                //todo this toast was crashing the app
//                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        Slot slot = slots.get(position);

        TextView occupied = dialogView.findViewById(R.id.occupiedMax);
        occupied.setText(slot.getOccupiedSlots() + "/" + slot.getMaxSlots());

        MaterialButton type = dialogView.findViewById(R.id.priority_text);
        type.setText(slot.getType());
        if (slot.getType().equals("Standard"))
            type.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorPrimary));

        TextView hourFrom = dialogView.findViewById(R.id.hourFrom);
        hourFrom.setText(slot.getFrom());

        TextView hourTo = dialogView.findViewById(R.id.hourTo);
        hourTo.setText(slot.getTo());

        TextView placeName = dialogView.findViewById(R.id.titlePlanner);
        placeName.setText(place.getName());

        for (int i = 0; i < btn.length; i++) {
            btn[i] = dialogView.findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }

        btn_unfocus = btn[0];

        if (slot.getFriends() > 0) {
            int btnId = slot.getFriends() - 1;
            setFocus(btn_unfocus, btn[btnId]);
        }


        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        saveButton.setOnClickListener(v -> {
            postPlanVisit(slot.getId(), Integer.parseInt(btn_unfocus.getText().toString()));
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
                        Toast.makeText(getContext(), errorObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                getSlotsPlace();
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
        btn_unfocus.setTextColor(ContextCompat.getColor(getContext(), R.color.text_gray));
        btn_unfocus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.gray));
        btn_focus.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        btn_focus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
        this.btn_unfocus = btn_focus;
    }
}

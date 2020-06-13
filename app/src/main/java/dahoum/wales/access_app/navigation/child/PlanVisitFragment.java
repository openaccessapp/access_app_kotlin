package dahoum.wales.access_app.navigation.child;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private ImageView infoButton;
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
        view.findViewById(R.id.goBack).setOnClickListener(v -> getParentFragment().getChildFragmentManager().popBackStack());
        infoButton = view.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> {
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
        if (place.getImage() != null) {
            byte[] decodedString = Base64.decode(place.getImage(), Base64.DEFAULT);
            Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            image.setImageBitmap(base64Bitmap);
        }
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
                } else if (response.body().get("slots").getAsJsonArray() != null) {
                    //todo slots is not longer an arraylist, it's a map<the date as string, ArrayList<Slot>>
                    //crashes the app when opened
                    slots = new Gson().fromJson(response.body().get("slots"), new TypeToken<ArrayList<Slot>>() {
                    }.getType());
                    if (!slots.isEmpty()) {
                        slots.add(0, new Slot(slots.get(0).getFrom(), 1));
                    }
                    adapter.setDataList(slots);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        for (int i = 0; i < btn.length; i++) {
            btn[i] = dialogView.findViewById(btn_id[i]);
            btn[i].setOnClickListener(this);
        }

        btn_unfocus = btn[0];


        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        saveButton.setOnClickListener(v -> {
            postPlanVisit(slots.get(position).getId(), Integer.parseInt(btn_unfocus.getText().toString()));
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
        btn_unfocus.setTextColor(ContextCompat.getColor(getContext(), R.color.text_grey));
        btn_unfocus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.grey));
        btn_focus.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        btn_focus.setBackgroundTintList(ContextCompat.getColorStateList(getContext(), R.color.colorPrimary));
        this.btn_unfocus = btn_focus;
    }
}

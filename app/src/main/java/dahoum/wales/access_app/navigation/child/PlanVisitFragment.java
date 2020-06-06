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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;
import dahoum.wales.access_app.adapters.PlanVisitAdapter;
import dahoum.wales.access_app.models.Place;
import dahoum.wales.access_app.models.Slot;
import dahoum.wales.access_app.network.RetrofitClientInstance;
import dahoum.wales.access_app.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanVisitFragment extends Fragment implements PlanVisitAdapter.AdapterCallback {

    private static final String TAG = PlanVisitFragment.class.getSimpleName();
    private ImageView goBackButton, infoButton;
    private RecyclerView recyclerView;
    private PlanVisitAdapter adapter;
    private List<Slot> slots = new ArrayList<>();
    private FragmentCallback callback;
    private Place place;
    private TextView placeName, placeDesc, websiteTv;
    private ImageView image;
    private RetrofitService retrofitService;

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

        goBackButton = view.findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> {
            getParentFragment().getChildFragmentManager().popBackStack();
        });
        infoButton = view.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(v -> {
            callback.onInfoClicked(1);
        });
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
        place = (Place) getArguments().getSerializable("place");
        setupPlace(view);

        recyclerView = view.findViewById(R.id.recyclerPlanVisit);
        adapter = new PlanVisitAdapter();
        adapter.setDataList(slots);
        adapter.setAdapterCallback(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        retrofitService.getSlotsPlace(prefs.getString("userId", null), place.getId()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                Gson gson = new Gson();
                if (response.body().get("slots").getAsJsonArray() != null) {
                    slots = gson.fromJson(response.body().get("slots"), new TypeToken<ArrayList<Slot>>() {
                    }.getType());
                    slots.add(0, new Slot(slots.get(0).getFrom(), 1));
                    adapter.setDataList(slots);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d(TAG, t.getLocalizedMessage());
            }
        });
    }

//    private void getData() {
//        slots.add(new Slot(null, null, null, "MON", "27th May", 1));
//        slots.add(new Slot("10:00 - 11:00", "Priority", "14/20", null, null, 0));
//        slots.add(new Slot("11:00 - 12:00", "Priority", "12/20", null, null, 0));
//        slots.add(new Slot("12:00 - 13:00", "Standart", "11/20", null, null, 0));
//        slots.add(new Slot("13:00 - 14:00", "Standart", "18/20", null, null, 0));
//        slots.add(new Slot("14:00 - 15:00", "Standart", "20/20", null, null, 0));
//        slots.add(new Slot(null, null, null, "TUE", "28th May", 1));
//        slots.add(new Slot("10:00 - 11:00", "Priority", "16/20", null, null, 0));
//        slots.add(new Slot("11:00 - 12:00", "Priority", "18/20", null, null, 0));
//        slots.add(new Slot("12:00 - 13:00", "Standart", "11/20", null, null, 0));
//        slots.add(new Slot("13:00 - 14:00", "Standart", "14/20", null, null, 0));
//        slots.add(new Slot("14:00 - 15:00", "Priority", "20/20", null, null, 0));
//        slots.add(new Slot(null, null, null, "WED", "29th May", 1));
//        slots.add(new Slot("10:00 - 11:00", "Priority", "18/20", null, null, 0));
//        slots.add(new Slot("11:00 - 12:00", "Standart", "11/20", null, null, 0));
//        slots.add(new Slot("12:00 - 13:00", "Standart", "13/20", null, null, 0));
//        slots.add(new Slot("13:00 - 14:00", "Priority", "20/20", null, null, 0));
//        slots.add(new Slot("14:00 - 15:00", "Standart", "10/20", null, null, 0));
//    }

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

    @Override
    public void onItemClick(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_dialog, viewGroup, false);
        Button buttonOk=dialogView.findViewById(R.id.save_button);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        buttonOk.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        alertDialog.show();
    }
}

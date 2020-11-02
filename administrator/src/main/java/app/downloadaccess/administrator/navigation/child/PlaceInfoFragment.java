package app.downloadaccess.administrator.navigation.child;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.downloadaccess.administrator.R;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.models.Slot;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceInfoFragment extends Fragment {

    private static final String TAG = PlaceInfoFragment.class.getSimpleName();
    private RecyclerView recyclerView;
    private List<Slot> slots = new ArrayList<>();
    private FragmentCallback callback;
    private Place place;
    private TextView placeName, placeDesc, websiteTv, locationTv;
    private ImageView image, shieldImage;
    private RetrofitService retrofitService;
    private SharedPreferences prefs;
    private SwitchCompat approvedSwitch;

    public PlaceInfoFragment() {
        // Required empty public constructor
    }

    public static PlaceInfoFragment newInstance(Place place) {
        PlaceInfoFragment fragment = new PlaceInfoFragment();
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
        return inflater.inflate(R.layout.fragment_place_info, container, false);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "Attached PlanVisitFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        place = (Place) getArguments().getSerializable("place");
        View goBackButton = view.findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> getParentFragment().getChildFragmentManager().popBackStack());

        OnBackPressedCallback backButton = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backButton);
        setupPlace(view);

        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
    }

    private void setupPlace(View view) {
        placeName = view.findViewById(R.id.placeName);
        placeName.setText(place.getName());
        placeDesc = view.findViewById(R.id.placeDesc);
        placeDesc.setText(place.getDescription());
        websiteTv = view.findViewById(R.id.websiteTv);
        websiteTv.setText(place.getWww());
        locationTv = view.findViewById(R.id.locationTv);
        locationTv.setText(place.getAddress());
        image = view.findViewById(R.id.image);
        shieldImage = view.findViewById(R.id.shieldImage);
        approvedSwitch = view.findViewById(R.id.approvePlace);
        approvedSwitch.setChecked(place.getApproved());
        isPlaceApproved(place.getApproved());
        approvedSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isPlaceApproved(isChecked);
            retrofitService.setApproved(Utils.getJwtToken(getContext()), place.getId(), isChecked).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d(TAG, t.getLocalizedMessage());
                }
            });
        });
        Picasso.get().load(RetrofitClientInstance.BASE_URL + "/get-image/" + place.getId()).into(image);
    }

    private void isPlaceApproved(boolean isChecked) {
        if (isChecked) {
            shieldImage.setImageResource(R.drawable.ic_shield_active);
        } else {
            shieldImage.setImageResource(R.drawable.ic_shield_inactive);
        }
    }
}

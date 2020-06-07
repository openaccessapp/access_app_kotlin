package dahoum.wales.access_app.navigation.child;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;


import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;
import dahoum.wales.access_app.adapters.PlacesAdapter;
import dahoum.wales.access_app.models.Place;
import dahoum.wales.access_app.network.RetrofitService;



public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();
    private ImageView goBackButton, infoButton;
    private FragmentCallback callback;
    private ChipGroup chipGroup;
    private List<Place> places = new ArrayList<>();
    private RetrofitService retrofitService;
    private SharedPreferences prefs;
    private PlacesAdapter adapter;
    private RelativeLayout loadingPanel;

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance() {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "Attached Location Fragment");
    }

    public void setListener(FragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location, container, false);
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


    }
}
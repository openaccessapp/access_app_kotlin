package dahoum.wales.access_app.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.chip.ChipGroup;

import dahoum.wales.access_app.R;

public class PlacesFragment extends Fragment {

    private ChipGroup chipGroup;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_places, container, false);
        chipGroup = rootView.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {

        });
        return rootView;
    }
}

package dahoum.wales.access_app.navigation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import dahoum.wales.access_app.R;

public class PlacesHomeFragment extends Fragment {

    private FragmentCallback callback;
    private ChipGroup chipGroup;
    private CardView cardView;

    public PlacesHomeFragment() {
        // Required empty public constructor
    }

    public static PlacesHomeFragment newInstance() {
        PlacesHomeFragment fragment = new PlacesHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setListener(FragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardView = view.findViewById(R.id.card_history_museum);
        cardView.setOnClickListener(v -> {
            callback.onPlaceClicked();
        });
        chipGroup = view.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip = (Chip) group.getChildAt(checkedId - 1);
                chip.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                Toast.makeText(getContext(), "" + checkedId, Toast.LENGTH_SHORT).show();
            }
        });
    }


}

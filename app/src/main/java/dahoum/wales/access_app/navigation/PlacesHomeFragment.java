package dahoum.wales.access_app.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import dahoum.wales.access_app.ProfileActivity;
import dahoum.wales.access_app.R;

public class PlacesHomeFragment extends Fragment {

    private static final String TAG = PlacesHomeFragment.class.getSimpleName();
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
        view.findViewById(R.id.openProfile).setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });
        cardView = view.findViewById(R.id.card_history_museum);
        cardView.setOnClickListener(v -> {
            callback.onPlaceClicked();
        });
        chipGroup = view.findViewById(R.id.chip_group);
        chipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Chip chip = group.findViewById(checkedId);
            Log.d(TAG, chip.getText().toString());
        });
    }


}

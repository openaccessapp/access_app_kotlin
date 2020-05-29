package dahoum.wales.access_app.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dahoum.wales.access_app.R;

public class PlacesFragment extends Fragment implements FragmentCallback {

    private static final String TAG = PlacesFragment.class.getSimpleName();

    public PlacesFragment() {
        // Required empty public constructor
    }

    public static PlacesFragment newInstance() {
        PlacesFragment fragment = new PlacesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.places_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        if (fragment instanceof PlacesHomeFragment) {
            PlacesHomeFragment placesHomeFragment = (PlacesHomeFragment) fragment;
            placesHomeFragment.setListener(this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlacesHomeFragment placesHomeFragment = PlacesHomeFragment.newInstance();
        placesHomeFragment.setListener(this);
        openFragment(placesHomeFragment);
    }

    @Override
    public void onPlaceClicked() {
        PlanVisitFragment planVisitFragment = PlanVisitFragment.newInstance();
        openFragment(planVisitFragment);
    }
}

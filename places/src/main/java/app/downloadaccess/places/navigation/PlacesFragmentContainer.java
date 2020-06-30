package app.downloadaccess.places.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import app.downloadaccess.places.R;
import app.downloadaccess.places.models.Place;
import app.downloadaccess.places.navigation.child.AddPlaceFragment;
import app.downloadaccess.places.navigation.child.FragmentCallback;
import app.downloadaccess.places.navigation.child.PlacesFragment;
import app.downloadaccess.places.navigation.child.PlanVisitFragment;
import app.downloadaccess.places.navigation.child.VisitInfoFragment;

public class PlacesFragmentContainer extends Fragment implements FragmentCallback {

    private static final String TAG = PlacesFragmentContainer.class.getSimpleName();

    public PlacesFragmentContainer() {
        // Required empty public constructor
    }

    public static PlacesFragmentContainer newInstance() {
        PlacesFragmentContainer fragment = new PlacesFragmentContainer();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void openFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.places_container, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_places_container, container, false);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (childFragment instanceof PlacesFragment) {
            ((PlacesFragment) childFragment).setListener(this);
            Log.d(TAG, "PlacesFragment attached");
        } else if (childFragment instanceof PlanVisitFragment) {
            ((PlanVisitFragment) childFragment).setListener(this);
            Log.d(TAG, "PlanVisit attached");
        } else if (childFragment instanceof VisitInfoFragment) {
            ((VisitInfoFragment) childFragment).setListener(this);
            Log.d(TAG, "VisitInfo attached");
        } else if (childFragment instanceof AddPlaceFragment) {
            ((AddPlaceFragment) childFragment).setListener(this);
            Log.d(TAG, "Place attached");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        PlacesFragment placesFragment = PlacesFragment.newInstance();
        openFragment(placesFragment);
    }

    @Override
    public void onPlaceClicked(Place place) {
        PlanVisitFragment planVisitFragment = PlanVisitFragment.newInstance(place);
        planVisitFragment.setListener(this);
        openFragment(planVisitFragment);
    }

    @Override
    public void onAddPlaceClicked(Place place) {
        AddPlaceFragment fragment = AddPlaceFragment.newInstance(place);
        fragment.setListener(this);
        openFragment(fragment);
    }

    @Override
    public void editPlace(Place place) {
        AddPlaceFragment fragment = AddPlaceFragment.newInstance(place);
        fragment.setListener(this);
        openFragment(fragment);
    }
}

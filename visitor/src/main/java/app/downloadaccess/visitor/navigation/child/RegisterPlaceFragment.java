package app.downloadaccess.visitor.navigation.child;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitService;
import app.downloadaccess.visitor.ProfileActivity;
import app.downloadaccess.visitor.R;
import app.downloadaccess.visitor.adapters.PlacesAdapter;

public class RegisterPlaceFragment extends Fragment {
    private static final String TAG = LocationFragment.class.getSimpleName();
    private ImageView goBackButton;
    private FragmentCallback callback;
    private List<Place> places = new ArrayList<>();
    private ArrayList<String> arr = new ArrayList<>();
    //arr.add("Sofia");
    private RetrofitService retrofitService;
    private SharedPreferences prefs;
    private PlacesAdapter adapter;

    public RegisterPlaceFragment() {
        // Required empty public constructor
    }

    public static RegisterPlaceFragment newInstance() {
        RegisterPlaceFragment fragment = new RegisterPlaceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "Attached RegisterPlaceFragment");
    }

    public void setListener(FragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AutoCompleteTextView editText = view.findViewById(R.id.LocEt);

        //ArrayAdapter<Place> adapter = new ArrayAdapter<Place>(this,R.layout.fragment_add_place,places);
        //editText.setAdapter(adapter);

        goBackButton = view.findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> getParentFragment().getChildFragmentManager().popBackStack());


    }


}



package dahoum.wales.access_app.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dahoum.wales.access_app.R;
import dahoum.wales.access_app.adapters.HomeAdapter;
import dahoum.wales.access_app.models.Place;

public class PlanVisitFragment extends Fragment {

    private List<Place> places;
    private ImageView goBackButton;

    public PlanVisitFragment() {
        // Required empty public constructor
    }

    public static PlanVisitFragment newInstance() {
        PlanVisitFragment fragment = new PlanVisitFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_visit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        goBackButton = view.findViewById(R.id.goBack);
        goBackButton.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }
}

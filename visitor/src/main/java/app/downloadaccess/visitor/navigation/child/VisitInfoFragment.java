package app.downloadaccess.visitor.navigation.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.visitor.R;

public class VisitInfoFragment extends Fragment {

    private ImageView goBackButton;
    private FragmentCallback callback;

    private TextView placeName;
    private Place place;
    private ImageView image;


    public VisitInfoFragment() {
        // Required empty public constructor
    }

    public static VisitInfoFragment newInstance(Place place) {
        VisitInfoFragment fragment = new VisitInfoFragment();
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
        return inflater.inflate(R.layout.fragment_visitors_information, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        goBackButton = view.findViewById(R.id.goBack);

        goBackButton.setOnClickListener(v -> {
            getParentFragment().getChildFragmentManager().popBackStack();
        });
        OnBackPressedCallback backButton = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backButton);

        place = (Place) getArguments().getSerializable("place");
        setupPlace(view);
    }

    private void setupPlace(View view) {
        placeName = view.findViewById(R.id.placeName);
        placeName.setText(place.getName());
        image = view.findViewById(R.id.image);
        Picasso.get().load(RetrofitClientInstance.BASE_URL + "get-image/" + place.getId()).into(image);

    }
}

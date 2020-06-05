package dahoum.wales.access_app.navigation.child;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import dahoum.wales.access_app.R;

public class VisitInfoFragment extends Fragment {

    private ImageView goBackButton;
    private FragmentCallback callback;

    public VisitInfoFragment() {
        // Required empty public constructor
    }

    public static VisitInfoFragment newInstance() {
        VisitInfoFragment fragment = new VisitInfoFragment();
        Bundle args = new Bundle();
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

    }

}

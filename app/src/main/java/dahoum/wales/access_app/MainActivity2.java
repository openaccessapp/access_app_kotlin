package dahoum.wales.access_app;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import dahoum.wales.access_app.navigation.CalendarFragment;
import dahoum.wales.access_app.navigation.HomeFragment;
import dahoum.wales.access_app.navigation.PlacesFragment;
import dahoum.wales.access_app.navigation.ScanFragment;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity2.class.getSimpleName();
    private LinearLayout home, places, calendar, scan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home = findViewById(R.id.homeButton);
        home.setOnClickListener(this);
        places = findViewById(R.id.placesButton);
        places.setOnClickListener(this);
        calendar = findViewById(R.id.calendarButton);
        calendar.setOnClickListener(this);
        scan = findViewById(R.id.scanButton);
        scan.setOnClickListener(this);

        openFragment(HomeFragment.newInstance("", ""));
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.homeButton:
                ((ImageView) home.findViewById(R.id.homeImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                ((TextView) home.findViewById(R.id.homeTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                openFragment(HomeFragment.newInstance("", ""));
                break;
            case R.id.placesButton:
                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                ((ImageView) home.findViewById(R.id.homeImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) home.findViewById(R.id.homeTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                openFragment(PlacesFragment.newInstance());
                break;
            case R.id.calendarButton:
                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) home.findViewById(R.id.homeImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) home.findViewById(R.id.homeTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                openFragment(CalendarFragment.newInstance("", ""));
                break;
            case R.id.scanButton:
                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) home.findViewById(R.id.homeImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) home.findViewById(R.id.homeTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
                openFragment(ScanFragment.newInstance());
                break;
        }
    }
}



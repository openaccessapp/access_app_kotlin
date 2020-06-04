package dahoum.wales.access_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import dahoum.wales.access_app.navigation.PlacesFragment;
import dahoum.wales.access_app.navigation.PlannerFragment;
import dahoum.wales.access_app.navigation.ScanFragment;
import dahoum.wales.access_app.network.RetrofitClientInstance;
import dahoum.wales.access_app.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getSimpleName();
    private LinearLayout home, places, calendar, scan;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        retrofitService = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        if (prefs.getString("userId", null) == null) {
            retrofitService.getUserId().enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.body() != null && response.body().get("id").isJsonPrimitive()) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString("userId", response.body().get("id").getAsString());
                        editor.apply();
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d(TAG, t.getLocalizedMessage());
                }
            });
        }
//        places = findViewById(R.id.placesButton);
//        places.setOnClickListener(this);
//        calendar = findViewById(R.id.calendarButton);
//        calendar.setOnClickListener(this);
//        scan = findViewById(R.id.scanButton);
//        scan.setOnClickListener(this);

        openFragment(PlacesFragment.newInstance());
    }

    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.placesNav:
                    openFragment(PlacesFragment.newInstance());
                    return true;
                case R.id.plannerNav:
                    openFragment(PlannerFragment.newInstance());
                    return true;
                case R.id.scanNav:
                    openFragment(ScanFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.placesButton:
//                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
//                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
//                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
//                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
//                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
//                openFragment(PlacesFragment.newInstance());
//                break;
//            case R.id.calendarButton:
//                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
//                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
//                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
//                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
//                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
//                openFragment(PlannerFragment.newInstance());
//                break;
//            case R.id.scanButton:
//                ((ImageView) scan.findViewById(R.id.scanImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
//                ((TextView) scan.findViewById(R.id.scanTv)).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
//                ((ImageView) places.findViewById(R.id.placesImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
//                ((TextView) places.findViewById(R.id.placesTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
//                ((ImageView) calendar.findViewById(R.id.calendarImg)).setImageTintList(ContextCompat.getColorStateList(this, R.color.disabled_tint));
//                ((TextView) calendar.findViewById(R.id.calendarTv)).setTextColor(ContextCompat.getColor(this, R.color.disabled_tint));
//                openFragment(ScanFragment.newInstance());
//                break;
//        }
//    }
}



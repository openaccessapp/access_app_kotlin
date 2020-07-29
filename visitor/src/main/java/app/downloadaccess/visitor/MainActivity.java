package app.downloadaccess.visitor;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import java.util.Locale;

import app.downloadaccess.resources.CustomViewPager;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.ViewPagerAdapter;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import app.downloadaccess.visitor.navigation.PlacesFragmentContainer;
import app.downloadaccess.visitor.navigation.PlannerFragment;
import app.downloadaccess.visitor.navigation.ScanFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private LinearLayout home, places, calendar, scan;
    private RetrofitService retrofitService;
    private Fragment placeFragmentContainer, plannerFragment, scanFragment;
    private CustomViewPager viewPager;
    public BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        loadLocale();
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setPagingEnabled(false);
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);

        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
        if (prefs.getString("userId", null) == null) {
            retrofitService.getUserId(Utils.getJwtToken(this)).enqueue(new Callback<JsonObject>() {
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

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setupViewPager(viewPager);

        bottomNavigationView.setSelectedItemId(R.id.placesNav);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        placeFragmentContainer = PlacesFragmentContainer.newInstance();
        plannerFragment = PlannerFragment.newInstance();
        scanFragment = ScanFragment.newInstance();
        adapter.addFragment(placeFragmentContainer);
        adapter.addFragment(plannerFragment);
        adapter.addFragment(scanFragment);
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.placesNav:
                    viewPager.setCurrentItem(0, false);
                    return true;
                case R.id.plannerNav:
                    viewPager.setCurrentItem(1, false);
                    ((PlannerFragment) plannerFragment).getAllVisits();
                    return true;
                case R.id.scanNav:
                    viewPager.setCurrentItem(2, false);
                    return true;
            }
            return false;
        }
    };


    public void loadLocale() {
            Locale locale = new Locale(ProfileActivity.languageKey);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            setContentView(R.layout.activity_main);
    }

}



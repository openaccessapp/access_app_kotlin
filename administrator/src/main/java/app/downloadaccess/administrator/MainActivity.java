package app.downloadaccess.administrator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import app.downloadaccess.administrator.navigation.PlacesFragmentContainer;
import app.downloadaccess.resources.CustomViewPager;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.ViewPagerAdapter;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RetrofitService retrofitService;
    private Fragment placeFragmentContainer;
    private CustomViewPager viewPager;
    MenuItem prevMenuItem;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setPagingEnabled(false);

        prefs = getPreferences(MODE_PRIVATE);

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

        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        placeFragmentContainer = PlacesFragmentContainer.newInstance();
        adapter.addFragment(placeFragmentContainer);
        viewPager.setAdapter(adapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.placesNav) {
                viewPager.setCurrentItem(0, false);
                return true;
            }
            return false;
        }
    };
}



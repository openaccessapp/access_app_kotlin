package dahoum.wales.access_app;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PlanVisitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_visit);
    }

    public void goBack(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }
}

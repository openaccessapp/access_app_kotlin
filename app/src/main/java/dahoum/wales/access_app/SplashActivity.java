package dahoum.wales.access_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button getStarted = findViewById(R.id.get_started_button);
        getStarted.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        });

    }
}

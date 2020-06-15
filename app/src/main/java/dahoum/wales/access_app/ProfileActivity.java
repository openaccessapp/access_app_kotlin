package dahoum.wales.access_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_profile);
        findViewById(R.id.goBack).setOnClickListener(v -> finish());
        MaterialButton saveButton = findViewById(R.id.save_button);
        EditText ageField = findViewById(R.id.ageField);
        int age = prefs.getInt("userAge", 0);
        ageField.setText(age > 0 ? age + "" : "");

        saveButton.setOnClickListener(v -> {
            String input = String.valueOf(ageField.getText());
            if (!input.isEmpty()) {
                editor.putInt("userAge", Integer.parseInt(input));
            }else{
                editor.remove("userAge");
            }
            editor.apply();
            Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
        });
    }
}

package app.downloadaccess.visitor;

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
        // EditText nameField = findViewById(R.id.nameField);
        int age = prefs.getInt("userAge", 0);
        String name = prefs.getString("userName", null);
        ageField.setText(age > 0 ? age + "" : "");
        // nameField.setText(name != null && !name.trim().isEmpty() ? name : "");

        saveButton.setOnClickListener(v -> {
            String input = String.valueOf(ageField.getText());
            if (!input.isEmpty()) editor.putInt("userAge", Integer.parseInt(input));
            else editor.remove("userAge");

           // String nameInput = String.valueOf(nameField.getText());
          //  if (!input.isEmpty()) editor.putString("userName", nameInput);
          //  else editor.remove("userName");

            editor.apply();
            Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
        });
    }
}

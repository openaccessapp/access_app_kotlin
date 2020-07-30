package app.downloadaccess.visitor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    HashMap<String, String> language = new HashMap<>();
    String languageName;
    public static String languageKey ="bg";
    String languageCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        loadLocale();
        setContentView(R.layout.activity_profile);
        findViewById(R.id.goBack).setOnClickListener(v -> finish());
        MaterialButton saveButton = findViewById(R.id.save_button);
        EditText ageField = findViewById(R.id.ageField);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayList<String> spinnerLang = new ArrayList<>();
        spinnerLang.add("Български");
        spinnerLang.add("English");
        spinnerLang.add("German");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerLang);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        language.put("Български", "bg");
        language.put("English", "en");
        language.put("German", "de");
        languageCheck = languageKey;
        spinner.setOnItemSelectedListener(this);
        // EditText nameField = findViewById(R.id.nameField);
        int age = prefs.getInt("userAge", 0);
        String name = prefs.getString("userName", null);
        ageField.setText(age > 0 ? age + "" : "");
        // nameField.setText(name != null && !name.trim().isEmpty() ? name : "");

        saveButton.setOnClickListener(v -> {
            String input = String.valueOf(ageField.getText());
            if (!input.isEmpty()) editor.putInt("userAge", Integer.parseInt(input));
            else editor.remove("userAge");
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

            // String nameInput = String.valueOf(nameField.getText());
            //  if (!input.isEmpty()) editor.putString("userName", nameInput);
            //  else editor.remove("userName");

            editor.apply();
            Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spinner:
                languageName = parent.getItemAtPosition(position).toString();
                languageKey = language.get(languageName);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void loadLocale() {
            Locale locale = new Locale(languageKey);
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.locale = locale;
            getBaseContext().getResources().updateConfiguration(config,
                    getBaseContext().getResources().getDisplayMetrics());
            setContentView(R.layout.activity_profile);
    }

}

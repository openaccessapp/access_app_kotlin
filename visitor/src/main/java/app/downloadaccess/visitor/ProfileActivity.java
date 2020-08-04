package app.downloadaccess.visitor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import app.downloadaccess.resources.Utils;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    List<StringWithTag> languagesMap = new ArrayList<>();
    String languageName;
    String languageKey;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(0, 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        prefs = getSharedPreferences(Utils.PREFS_NAME, MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);
        SharedPreferences.Editor editor = prefs.edit();
        languageKey = prefs.getString("lang", "no");
        if (languageKey.equals("no")) {
            languageKey = "en";
            editor.putString("lang", languageKey);
        }

        Utils.loadLocale(this);

        findViewById(R.id.goBack).setOnClickListener(v -> {
            this.onBackPressed();
        });
        MaterialButton saveButton = findViewById(R.id.save_button);
        EditText ageField = findViewById(R.id.ageField);

        languagesMap.add(new StringWithTag("en", "English"));
        languagesMap.add(new StringWithTag("bg", "Български"));
        languagesMap.add(new StringWithTag("de", "Deutshland"));
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<StringWithTag> spinnerAdapter = new ArrayAdapter<>(this, R.layout.item_spinner, languagesMap);
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(this);
        for (StringWithTag stringWithTag : languagesMap) {
            if (stringWithTag.value.equals(languageKey)) {
                spinner.setSelection(spinnerAdapter.getPosition(stringWithTag));
            }
        }
        int age = prefs.getInt("userAge", 0);
        ageField.setText(age > 0 ? age + "" : "");

        saveButton.setOnClickListener(v -> {
            String input = String.valueOf(ageField.getText());
            if (!input.isEmpty()) editor.putInt("userAge", Integer.parseInt(input));
            else editor.remove("userAge");
            editor.putString("lang", languageKey);
            editor.apply();
            Utils.loadLocale(this);
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        languageName = ((StringWithTag) parent.getItemAtPosition(position)).key;
        languageKey = languagesMap.get(position).value;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("lang")) {
            recreate();
        }
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("lang_key", languageKey);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    private static class StringWithTag {
        public String key;
        public String value;

        public StringWithTag(String value, String key) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return key;
        }
    }
}
package app.downloadaccess.places.navigation.child;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import app.downloadaccess.places.R;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.models.Slot;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDialog {
    private Activity activity;
    private final RetrofitService retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
    private SharedPreferences prefs;
    private DatePicker datePicker;
    private TimePicker fromTimePicker, toTimePicker;
    private Place place;

    public BookingDialog(Activity activity, Place place) {
        this.prefs = activity.getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        this.activity = activity;
        this.place = place;
    }

    public void setupDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomAlertDialog);
        View dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_dialog, null, false);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);
        datePicker = dialogView.findViewById(R.id.datePicker);
        int yearSpinnerId = activity.getResources().getIdentifier("year", "id", "android");
        if (yearSpinnerId != 0) {
            View yearSpinner = datePicker.findViewById(yearSpinnerId);
            if (yearSpinner != null)
                yearSpinner.setVisibility(View.GONE);
        }
        fromTimePicker = dialogView.findViewById(R.id.hourFromTimePicker);
        int nowHour = fromTimePicker.getHour();
        if (++nowHour == 24) nowHour = 0;
        fromTimePicker.setIs24HourView(true);
        fromTimePicker.setHour(nowHour);
        fromTimePicker.setMinute(0);

        if (++nowHour == 24) nowHour = 0;
        toTimePicker = dialogView.findViewById(R.id.hourToTimePicker);
        toTimePicker.setIs24HourView(true);
        toTimePicker.setHour(nowHour);
        toTimePicker.setMinute(0);


        MaterialButton standard_button = dialogView.findViewById(R.id.standard_button);
        MaterialButton priority_button = dialogView.findViewById(R.id.priority_button);
        priority_button.setOnClickListener(v -> {
            priority_button.setTextColor(ContextCompat.getColor(activity, R.color.white));
            priority_button.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorAccent));
            standard_button.setTextColor(ContextCompat.getColor(activity, R.color.text_grey));
            standard_button.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.disabled_tint));
        });
        standard_button.setOnClickListener(v -> {
            standard_button.setTextColor(ContextCompat.getColor(activity, R.color.white));
            standard_button.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorPrimary));
            priority_button.setTextColor(ContextCompat.getColor(activity, R.color.text_grey));
            priority_button.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.disabled_tint));
        });

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        SwitchCompat repeatSwitch = dialogView.findViewById(R.id.repeatSwitch);
        EditText maxVisitors = dialogView.findViewById(R.id.visitorsCount);
        saveButton.setOnClickListener(v -> {
            Slot slot = new Slot();
            slot.setType(standard_button.isChecked() ? "Standard" : "Priority");
            slot.setFrom(datePicker.getDayOfMonth() + "." + datePicker.getMonth() + "." + datePicker.getYear() +
                    " " + fromTimePicker.getHour() + ":" + fromTimePicker.getMinute());
            slot.setTo(datePicker.getDayOfMonth() + "." + datePicker.getMonth() + "." + datePicker.getYear() +
                    " " + toTimePicker.getHour() + ":" + toTimePicker.getMinute());
            slot.setMaxSlots(Integer.parseInt(maxVisitors.getText().toString()));
            slot.setRepeat(repeatSwitch.isChecked());
            addNewSlot(place.getId(), slot);
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        View delete_button = dialogView.findViewById(R.id.delete_button);
//        if (visitors > 0) {
//            delete_button.setVisibility(View.VISIBLE);
//            delete_button.setOnClickListener(v -> {
//                removeVisit(slotId);
//                alertDialog.dismiss();
//            });
//        }
        alertDialog.show();
    }

    private void removeVisit(String slotId) {
        retrofitService.deleteVisit(Utils.getJwtToken(activity), prefs.getString("userId", null), slotId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.code() != 204 && response.errorBody() != null) {
                    try {
                        JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                        Toast.makeText(activity, errorObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(activity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addNewSlot(String placeId, Slot slot) {
        retrofitService.addSlot(Utils.getJwtToken(activity), placeId, prefs.getString("userId", null), slot).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if (response.code() != 201 && response.errorBody() != null) {
                    try {
                        JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                        Toast.makeText(activity, errorObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(activity, "Slot created!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                Toast.makeText(activity, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

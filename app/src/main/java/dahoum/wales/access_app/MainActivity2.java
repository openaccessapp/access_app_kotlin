package dahoum.wales.access_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity2 extends AppCompatActivity {

    private static final String TAG = MainActivity2.class.getSimpleName();

    private TextView fromDateTv, toDateTv;
    private Calendar calendarFrom, calendarTo;
    private RetrofitService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        fromDateTv = findViewById(R.id.fromDateTv);
        fromDateTv.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        calendarFrom.set(Calendar.YEAR, year);
                        calendarFrom.set(Calendar.MONTH, monthOfYear);
                        calendarFrom.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        showTimePicker();
                    }, calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });
        toDateTv = findViewById(R.id.toDateTv);

        toDateTv.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        calendarTo.set(Calendar.YEAR, year);
                        calendarTo.set(Calendar.MONTH, monthOfYear);
                        calendarTo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        showTimePickerTo();
                    }, calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.show();
        });

        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> getDemo = service.getDemo();
        getDemo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                try {
                    Date fromDate = simpleDateFormat.parse(response.body().getAsJsonObject().get("fromDate").getAsString());
                    calendarFrom.setTime(fromDate);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.getDefault());
                    fromDateTv.setText(dateFormat.format(fromDate));

                    Date toDate = simpleDateFormat.parse(response.body().getAsJsonObject().get("toDate").getAsString());
                    calendarTo.setTime(toDate);
                    SimpleDateFormat dateFormatTo = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.getDefault());
                    toDateTv.setText(dateFormatTo.format(toDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(MainActivity2.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, pHour, pMinute) -> {
                    calendarFrom.set(Calendar.HOUR_OF_DAY, pHour);
                    calendarFrom.set(Calendar.MINUTE, pMinute);
                    updateOnServer();

                }, calendarFrom.get(Calendar.HOUR_OF_DAY), calendarFrom.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void showTimePickerTo() {
        TimePickerDialog timePickerDialogTo = new TimePickerDialog(this,
                (view, pHourTo, pMinuteTo) -> {
                    calendarTo.set(Calendar.HOUR_OF_DAY, pHourTo);
                    calendarTo.set(Calendar.MINUTE, pMinuteTo);
                    updateOnServer();
                }, calendarTo.get(Calendar.HOUR_OF_DAY), calendarTo.get(Calendar.MINUTE), true);

        timePickerDialogTo.show();
    }


    private void updateOnServer() {
        HashMap<String, String> body = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        body.put("fromDate", simpleDateFormat.format(calendarFrom.getTime()));
        body.put("toDate", simpleDateFormat.format(calendarTo.getTime()));
        Call<JsonObject> updateTime = service.updatePlace("demo", body);
        updateTime.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
}



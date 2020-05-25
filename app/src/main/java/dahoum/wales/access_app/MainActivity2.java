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
        setContentView(R.layout.activity_main);

        calendarFrom = Calendar.getInstance();
        calendarTo = Calendar.getInstance();

        fromDateTv = findViewById(R.id.fromDateTv);
        fromDateTv.setOnClickListener(v -> {
            showDateTimePicker(calendarFrom);
        });

        toDateTv = findViewById(R.id.toDateTv);
        toDateTv.setOnClickListener(v -> {
            showDateTimePicker(calendarTo);
        });

        service = RetrofitClientInstance.getRetrofitInstance().create(RetrofitService.class);
        Call<JsonObject> getDemo = service.getDemo();
        getDemo.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat viewsDateFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.getDefault());
                try {
                    Date fromDate = serverDateFormat.parse(response.body().getAsJsonObject().get("fromDate").getAsString());
                    Date toDate = serverDateFormat.parse(response.body().getAsJsonObject().get("toDate").getAsString());
                    calendarFrom.setTime(fromDate);
                    calendarTo.setTime(toDate);
                    toDateTv.setText(viewsDateFormat.format(toDate));
                    fromDateTv.setText(viewsDateFormat.format(fromDate));
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

    private void showDateTimePicker(Calendar calendar) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (view1, pHour, pMinute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, pHour);
                                calendar.set(Calendar.MINUTE, pMinute);
                                updateOnServer();

                            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
                    timePickerDialog.show();
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
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
                SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat viewsDateFormat = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss", Locale.getDefault());
                try {
                    Date fromDate = serverDateFormat.parse(response.body().getAsJsonObject().get("fromDate").getAsString());
                    Date toDate = serverDateFormat.parse(response.body().getAsJsonObject().get("toDate").getAsString());
                    calendarFrom.setTime(fromDate);
                    calendarTo.setTime(toDate);
                    toDateTv.setText(viewsDateFormat.format(toDate));
                    fromDateTv.setText(viewsDateFormat.format(fromDate));
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
}



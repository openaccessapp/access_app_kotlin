package app.downloadaccess.visitor.navigation.child;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import app.downloadaccess.visitor.Consumer;
import app.downloadaccess.visitor.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDialog {
    private MaterialButton[] btn = new MaterialButton[7];
    private MaterialButton btn_unfocus;
    private int[] btn_id = {R.id.one, R.id.two, R.id.three, R.id.four, R.id.five, R.id.six, R.id.seven};
    private Context context;
    private Activity activity;
    private View view;
    private final RetrofitService retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
    private SharedPreferences prefs;
    private Consumer<Void> updateFunc;

    public BookingDialog(View view, Context context, Activity activity, Consumer<Void> updateFunc) {
        prefs = activity.getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        this.view = view;
        this.context = context;
        this.activity = activity;
        this.updateFunc = updateFunc;
    }

    public void itemClick(View.OnClickListener listener, String slotId, int occupiedSlots, int maxSlots,
                          String slotType, String hourFromValue, String hourToValue, String placeNameValue, int visitors,
                          Consumer<String> saveFunc) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialog);
        ViewGroup viewGroup = view.findViewById(R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_dialog, viewGroup, false);
        Button saveButton = dialogView.findViewById(R.id.save_button);
        Button cancelButton = dialogView.findViewById(R.id.cancel_button);

        TextView occupied = dialogView.findViewById(R.id.occupiedMax);
        occupied.setText(occupiedSlots + "/" + maxSlots);

        MaterialButton type = dialogView.findViewById(R.id.priority_text);
        type.setText(slotType);
        if (slotType.equals("Standard"))
            type.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.colorPrimary));

        TextView hourFrom = dialogView.findViewById(R.id.hourFrom);
        hourFrom.setText(hourFromValue);

        TextView hourTo = dialogView.findViewById(R.id.hourTo);
        hourTo.setText(hourToValue);

        TextView placeName = dialogView.findViewById(R.id.titlePlanner);
        placeName.setText(placeNameValue);
        int availableSlots = btn.length;

        if((maxSlots-occupiedSlots)<7){
            availableSlots = maxSlots - occupiedSlots;
        }

        if(maxSlots-occupiedSlots==0){
            //Add appropriate message
        }
        for (int i = 0; i < availableSlots ; i++) {
            btn[i] = dialogView.findViewById(btn_id[i]);
            btn[i].setOnClickListener(listener);
            btn[i].setVisibility(View.VISIBLE);
        }

        btn_unfocus = btn[0];

        if (visitors > 0) {
            int btnId = visitors - 1;
            setFocus(btn_unfocus, btn[btnId]);
        }

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        saveButton.setOnClickListener(v -> {
            saveFunc.accept(btn_unfocus.getText().toString());
            alertDialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> {
            alertDialog.dismiss();
        });
        View button = dialogView.findViewById(R.id.delete_button);
        if (visitors == 0) {
            button.setVisibility(View.GONE);
        } else {
            button.setOnClickListener(v -> {
                removeVisit(slotId, updateFunc);
                alertDialog.dismiss();
            });
        }
        alertDialog.show();
    }

    private void removeVisit(String slotId, Consumer<Void> updateFunc) {
        retrofitService.deleteVisit(Utils.getJwtToken(activity), prefs.getString("userId", null), slotId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.code() != 204 && response.errorBody() != null) {
                    try {
                        JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                        Toast.makeText(context, errorObject.get("message").getAsString(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                updateFunc.accept(null);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setFocus(MaterialButton btnUnfocus, MaterialButton btn_focus) {
        btnUnfocus.setTextColor(ContextCompat.getColor(context, R.color.text_grey));
        btnUnfocus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.grey));
        btn_focus.setTextColor(ContextCompat.getColor(context, R.color.white));
        btn_focus.setBackgroundTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
        btn_unfocus = btn_focus;
    }

    public void postPlanVisit(String slotId, int visitorsCount) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("slotId", slotId);
        body.put("visitors", visitorsCount);
        body.put("visitorId", prefs.getString("userId", null));
        retrofitService.planVisit(Utils.getJwtToken(activity), body).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
                if (response.code() != 204 && response.errorBody() != null) {
                    try {
                        JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                        Toast.makeText(context, errorObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                updateFunc.accept(null);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.one:
                setFocus(btn_unfocus, btn[0]);
                break;
            case R.id.two:
                setFocus(btn_unfocus, btn[1]);
                break;
            case R.id.three:
                setFocus(btn_unfocus, btn[2]);
                break;
            case R.id.four:
                setFocus(btn_unfocus, btn[3]);
                break;
            case R.id.five:
                setFocus(btn_unfocus, btn[4]);
                break;
            case R.id.six:
                setFocus(btn_unfocus, btn[5]);
                break;
            case R.id.seven:
                setFocus(btn_unfocus, btn[6]);
                break;
        }
    }
}

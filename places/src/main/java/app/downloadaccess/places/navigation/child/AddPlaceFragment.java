package app.downloadaccess.places.navigation.child;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import app.downloadaccess.places.R;
import app.downloadaccess.resources.Utils;
import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.network.RetrofitClientInstance;
import app.downloadaccess.resources.network.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlaceFragment extends Fragment {

    private static final String TAG = AddPlaceFragment.class.getSimpleName();
    private static final int SELECT_FILE = 1;
    private FragmentCallback callback;
    private Place place;
    private ImageView imagePlace;
    private RetrofitService retrofitService;
    private String img;
    private SharedPreferences prefs;
    private MaterialButton uploadImage, nextButton;
    private boolean imgChanged = false;

    public AddPlaceFragment() {
        // Required empty public constructor
    }

    public static AddPlaceFragment newInstance(Place place) {
        AddPlaceFragment fragment = new AddPlaceFragment();
        Bundle args = new Bundle();
        args.putSerializable("place", place);
        fragment.setArguments(args);
        fragment.place = place;
        return fragment;
    }

    public void setListener(FragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_place, container, false);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Log.d(TAG, "Attached PlanVisitFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        imagePlace = view.findViewById(R.id.imagePlaceholder);
        if (place != null) {
            ((TextView) view.findViewById(R.id.add_place_header)).setText("Edit Place");
            Picasso.get().load(RetrofitClientInstance.BASE_URL + "/api/image/" + place.getId()).into(imagePlace);
            ((EditText) view.findViewById(R.id.placeNameEt)).setText(place.getName());
            ((EditText) view.findViewById(R.id.LocEt)).setText(place.getAddress());
            ((EditText) view.findViewById(R.id.describeEt)).setText(place.getDescription());
            ((EditText) view.findViewById(R.id.urlEt)).setText(place.getWww());
        }

        prefs = getActivity().getSharedPreferences(Utils.PREFS_NAME, Context.MODE_PRIVATE);
        view.findViewById(R.id.goBack).setOnClickListener(v -> getParentFragment().getChildFragmentManager().popBackStack());

        EditText descriptionField = view.findViewById(R.id.describeEt);
        descriptionField.setMovementMethod(ScrollingMovementMethod.getInstance());
        descriptionField.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        descriptionField.setVerticalScrollBarEnabled(true);
        descriptionField.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        descriptionField.setRawInputType(InputType.TYPE_CLASS_TEXT);
        descriptionField.setOnTouchListener((view1, motionEvent) -> {
            view1.getParent().requestDisallowInterceptTouchEvent(true);
            if ((motionEvent.getAction() & MotionEvent.ACTION_UP) != 0 && (motionEvent.getActionMasked() & MotionEvent.ACTION_UP) != 0) {
                view1.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });

        OnBackPressedCallback backButton = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragment().getChildFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, backButton);
        uploadImage = view.findViewById(R.id.uploadImageButton);
        img = null;
        uploadImage.setOnClickListener(v -> {
            galleryIntent();
        });
        nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Saving...", Toast.LENGTH_SHORT).show();
            if (place == null) place = new Place();
            place.setTypeId(0);
            if (imgChanged) place.setImage(this.img);
            place.setName(((EditText) view.findViewById(R.id.placeNameEt)).getText().toString());
            place.setDescription(((EditText) view.findViewById(R.id.describeEt)).getText().toString());
            place.setWww(((EditText) view.findViewById(R.id.urlEt)).getText().toString());
            place.setLocation(((EditText) view.findViewById(R.id.LocEt)).getText().toString());
            place.setAddress("Sofia");
            if (place.getId() == null) {
                retrofitService.addPlace(Utils.getJwtToken(getContext()), place, prefs.getString("userId", null)).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (response.code() == 201) {
                            Toast.makeText(getContext(), "Place saved!", Toast.LENGTH_SHORT).show();
                            getParentFragment().getChildFragmentManager().popBackStack();
                            callback.onReturnFromAdd();
                        } else {
                            try {
                                JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                                Toast.makeText(getContext(), errorObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                retrofitService.editPlace(Utils.getJwtToken(getContext()), place, prefs.getString("userId", null), place.getId()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), "Place saved!", Toast.LENGTH_SHORT).show();
                            getParentFragment().getChildFragmentManager().popBackStack();
                        } else {
                            try {
                                JsonObject errorObject = new JsonParser().parse(response.errorBody().string()).getAsJsonObject();
                                Toast.makeText(getContext(), errorObject.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<Void> call, @NotNull Throwable t) {
                        Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        retrofitService = RetrofitClientInstance.INSTANCE.buildService(RetrofitService.class);
//        retrofitService.getPlaceTypes().enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//
//            }
//        });
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imagePlace.setImageBitmap(bitmap);
        this.img = getStringImage(bitmap);
        this.imgChanged = true;

    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showToastMessage(String message) {
        Log.v(TAG, String.format("showToastMessage :: message = %s", message));
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}

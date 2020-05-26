package dahoum.wales.access_app.network;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("/access")
    Call<List<JsonObject>> getAllPlaces();

    @GET("/access/demo")
    Call<JsonObject> getDemo();

    @PUT("/access/{placeId}")
    Call<JsonObject> updatePlace(@Path("placeId") String placeId, @Body HashMap<String, String> body);
}
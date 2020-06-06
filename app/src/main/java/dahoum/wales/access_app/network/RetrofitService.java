package dahoum.wales.access_app.network;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitService {

    @GET("/api/user")
    Call<JsonObject> getUserId();

    @GET("/api/place/{visitorId}")
    Call<JsonObject> getAllPlaces(@Path("visitorId") String visitorId);

    @GET("/api/place/{visitorId}/{placeId}")
    Call<JsonObject> getSlotsPlace(@Path("visitorId") String visitorId, @Path("placeId") String placeId);

    @GET("/api/user/{visitorId}/favourites/{placeId}")
    Call<JsonObject> addRemoveFavourite(@Path("visitorId") String visitorId, @Path("placeId") String placeId);

    @PUT("/access/{placeId}")
    Call<JsonObject> updatePlace(@Path("placeId") String placeId, @Body HashMap<String, String> body);
}
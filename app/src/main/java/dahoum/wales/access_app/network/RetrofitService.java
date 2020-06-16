package dahoum.wales.access_app.network;

import com.google.gson.JsonObject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET("/api/user")
    Call<JsonObject> getUserId();

    @GET("/api/place/{visitorId}")
    Call<JsonObject> getAllPlaces(@Path("visitorId") String visitorId, @Query("skip") Integer skip, @Query("load") Integer load,
                                  @Query("name") String name, @Query("typeId") Integer typeId, @Query("onlyFavourites") Boolean onlyFavourites);

    @GET("/api/place/{visitorId}/{placeId}")
    Call<JsonObject> getSlotsPlace(@Path("visitorId") String visitorId, @Path("placeId") String placeId);

    @GET("/api/user/{visitorId}/favourites/{placeId}")
    Call<JsonObject> addRemoveFavourite(@Path("visitorId") String visitorId, @Path("placeId") String placeId);

    @POST("/api/user/{visitorId}/visit")
    Call<JsonObject> planVisit(@Path("visitorId") String visitorId, @Body HashMap<String, Object> body);

    @GET("/api/user/{visitorId}/visits")
    Call<JsonObject> getUserVisits(@Path("visitorId") String visitorId);

    @DELETE("/api/user/{visitorId}/visit/{slotId}")
    Call<JsonObject> deleteVisit(@Path("visitorId") String visitorId, @Path("slotId") String slotId);
}
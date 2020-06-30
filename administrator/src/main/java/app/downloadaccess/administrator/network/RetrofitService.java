package app.downloadaccess.administrator.network;

import com.google.gson.JsonObject;

import java.util.Map;

import app.downloadaccess.resources.models.Place;
import app.downloadaccess.resources.models.Slot;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RetrofitService {

    @GET("/api/user")
    Call<JsonObject> getUserId();

    @GET("/api/place-types")
    Call<JsonObject> getPlaceTypes();

    @GET("/api/place/{visitorId}")
    Call<JsonObject> getAllPlaces(@Path("visitorId") String visitorId, @QueryMap Map<String, Object> map);

    @GET("/api/place/{placeId}/approve/{approvedStatus}")
    Call<JsonObject> setApproved(@Path("placeId") String placeId, @Path("approvedStatus") boolean approvedStatus);

    @GET("/api/place/{visitorId}/{placeId}")
    Call<JsonObject> getSlotsPlace(@Path("visitorId") String visitorId, @Path("placeId") String placeId);

    @GET("/api/user/{visitorId}/visits")
    Call<JsonObject> getUserVisits(@Path("visitorId") String visitorId);

    @DELETE("/api/user/{visitorId}/visit/{slotId}")
    Call<JsonObject> deleteVisit(@Path("visitorId") String visitorId, @Path("slotId") String slotId);

    @POST("/api/place/{visitorId}")
    Call<Void> addPlace(@Body Place place, @Path("visitorId") String visitorId);

    @PUT("/api/place/{visitorId}/{placeId}")
    Call<Void> editPlace(@Body Place place, @Path("visitorId") String visitorId, @Path("placeId") String placeId);

    @POST("/api/slot/{placeId}")
    Call<Void> addSlot(@Path("placeId") String placeId, @Body Slot slot);

}
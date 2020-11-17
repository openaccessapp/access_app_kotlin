package app.downloadaccess.resources.network

import app.downloadaccess.resources.models.Place
import app.downloadaccess.resources.models.Slot
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("generate-user-id")
    fun getUserId(@Header("Authorization") token: String?): Call<JsonObject>

    @GET("get-place-types")
    fun getPlaceTypes(@Header("Authorization") token: String?): Call<JsonObject>

    @POST("approve-place/{placeId}/{approvedStatus}")
    fun setApproved(
        @Header("Authorization") token: String?,
        @Path("placeId") placeId: String?,
        @Path("approvedStatus") approvedStatus: Boolean
    ): Call<JsonObject?>?

    @POST("get-places/{userId}")
    fun getAllPlaces(
        @Header("Authorization") token: String?,
        @Path("userId") placeId: String?,
        @Body body: HashMap<String?, Any?>?
    ): Call<JsonObject>

    @GET("get-place-slots/{userId}/{placeId}")
    fun getSlotsPlace(
        @Header("Authorization") token: String?,
        @Path("userId") userId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject>

    @GET("get-bookings/{userId}")
    fun getUserVisits(
        @Header("Authorization") token: String?,
        @Path("userId") userId: String?
    ): Call<JsonObject>

    @GET("delete-booking/{userId}/{slotId}")
    fun deleteVisit(
        @Header("Authorization") token: String?,
        @Path("userId") userId: String?,
        @Path("slotId") slotId: String?
    ): Call<JsonObject>

    @DELETE("delete-slot/{slotId}/{userId}")
    fun deleteSlot(
        @Header("Authorization") token: String?,
        @Path("slotId") slotId: String?,
        @Path("userId") userId: String?
    ): Call<JsonObject>

    @POST("add-place")
    fun addPlace(
        @Header("Authorization") token: String?,
        @Body place: Place?
    ): Call<Void>

    @PUT("update-place/{placeId}")
    fun editPlace(
        @Header("Authorization") token: String?,
        @Body place: Place?,
        @Path("placeId") placeId: String?
    ): Call<Void>

    @POST("add-slot/{placeId}")
    fun addSlot(
        @Header("Authorization") token: String?,
        @Path("placeId") placeId: String?,
        @Body slot: Slot?
    ): Call<Void>

    @GET("change-favourite/{userId}/{placeId}")
    fun addRemoveFavourite(
        @Header("Authorization") token: String?,
        @Path("userId") userId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject?>?

    @POST("visit")
    fun planVisit(
        @Header("Authorization") token: String?,
        @Body body: HashMap<String?, Any?>?
    ): Call<JsonObject?>?
}
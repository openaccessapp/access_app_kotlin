package app.downloadaccess.resources.network

import app.downloadaccess.resources.models.Place
import app.downloadaccess.resources.models.Slot
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/api/user")
    fun getUserId(@Header("Authorization") token: String?): Call<JsonObject>

    @GET("/api/place-types")
    fun getPlaceTypes(@Header("Authorization") token: String?): Call<JsonObject>

    @GET("/api/place/{placeId}/approve/{approvedStatus}")
    fun setApproved(
        @Header("Authorization") token: String?,
        @Path("placeId") placeId: String?,
        @Path("approvedStatus") approvedStatus: Boolean
    ): Call<JsonObject?>?

    @GET("/api/place/{visitorId}")
    fun getAllPlaces(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @QueryMap map: HashMap<String, Any>
    ): Call<JsonObject>

    @GET("/api/place/{visitorId}/{placeId}")
    fun getSlotsPlace(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject>

    @GET("/api/user/{visitorId}/visits")
    fun getUserVisits(@Header("Authorization") token: String?, @Path("visitorId") visitorId: String?): Call<JsonObject>

    @DELETE("/api/user/{visitorId}/visit/{slotId}")
    fun deleteVisit(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Path("slotId") slotId: String?
    ): Call<JsonObject>

    @DELETE("/api/slot/{slotId}/{userId}")
    fun deleteSlot(
        @Header("Authorization") token: String?,
        @Path("slotId") slotId: String?,
        @Path("userId") userId: String?
    ): Call<JsonObject>

    @POST("/api/place/{visitorId}")
    fun addPlace(
        @Header("Authorization") token: String?,
        @Body place: Place?,
        @Path("visitorId") visitorId: String?
    ): Call<Void>

    @PUT("/api/place/{visitorId}/{placeId}")
    fun editPlace(
        @Header("Authorization") token: String?,
        @Body place: Place?,
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<Void>

    @POST("/api/slot/{placeId}/{visitorId}")
    fun addSlot(
        @Header("Authorization") token: String?,
        @Path("placeId") placeId: String?,
        @Path("visitorId") visitorId: String?,
        @Body slot: Slot?
    ): Call<Void>

    @GET("/api/user/{visitorId}/favourites/{placeId}")
    fun addRemoveFavourite(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject?>?

    @POST("/api/user/{visitorId}/visit")
    fun planVisit(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Body body: HashMap<String?, Any?>?
    ): Call<JsonObject?>?
}
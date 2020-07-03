package app.downloadaccess.resources.network

import app.downloadaccess.resources.models.Place
import app.downloadaccess.resources.models.Slot
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET("/api/user")
    fun getUserId(): Call<JsonObject>

    @GET("/api/place-types")
    fun getPlaceTypes(): Call<JsonObject>

    @GET("/api/place/{placeId}/approve/{approvedStatus}")
    fun setApproved(
        @Path("placeId") placeId: String?,
        @Path("approvedStatus") approvedStatus: Boolean
    ): Call<JsonObject?>?

    @GET("/api/place/{visitorId}")
    fun getAllPlaces(
        @Path("visitorId") visitorId: String?,
        @QueryMap map: HashMap<String, Any>
    ): Call<JsonObject>

    @GET("/api/place/{visitorId}/{placeId}")
    fun getSlotsPlace(
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject>

    @GET("/api/user/{visitorId}/visits")
    fun getUserVisits(@Path("visitorId") visitorId: String?): Call<JsonObject>

    @DELETE("/api/user/{visitorId}/visit/{slotId}")
    fun deleteVisit(
        @Path("visitorId") visitorId: String?,
        @Path("slotId") slotId: String?
    ): Call<JsonObject>

    @POST("/api/place/{visitorId}")
    fun addPlace(
        @Body place: Place?,
        @Path("visitorId") visitorId: String?
    ): Call<Void>

    @PUT("/api/place/{visitorId}/{placeId}")
    fun editPlace(
        @Body place: Place?,
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<Void>

    @POST("/api/slot/{placeId}")
    fun addSlot(
        @Path("placeId") placeId: String?,
        @Body slot: Slot?
    ): Call<Void>

    @GET("/api/user/{visitorId}/favourites/{placeId}")
    fun addRemoveFavourite(
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject?>?

    @POST("/api/user/{visitorId}/visit")
    fun planVisit(
        @Path("visitorId") visitorId: String?,
        @Body body: HashMap<String?, Any?>?
    ): Call<JsonObject?>?
}
package app.downloadaccess.resources.network

import app.downloadaccess.resources.models.Place
import app.downloadaccess.resources.models.Slot
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.*

interface RetrofitService {
    @GET(".netlify/functions/generate-user-id")
    fun getUserId(@Header("Authorization") token: String?): Call<JsonObject>

    @GET(".netlify/functions/get-place-types")
    fun getPlaceTypes(@Header("Authorization") token: String?): Call<JsonObject>

    @POST(".netlify/functions/approve-place/{placeId}/{approvedStatus}")
    fun setApproved(
        @Header("Authorization") token: String?,
        @Path("placeId") placeId: String?,
        @Path("approvedStatus") approvedStatus: Boolean
    ): Call<JsonObject?>?

    @POST(".netlify/functions/get-places/{visitorId}")
    fun getAllPlaces(
        @Header("Authorization") token: String?,
        @Path("visitorId") placeId: String?,
        @Body body: HashMap<String?, Any?>?
    ): Call<JsonObject>

    @POST(".netlify/functions/get-place-slots/{visitorId}/{placeId}")
    fun getSlotsPlace(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject>

    @GET(".netlify/functions/get-bookings/{visitorId}")
    fun getUserVisits(@Header("Authorization") token: String?, @Path("visitorId") visitorId: String?): Call<JsonObject>

    @GET(".netlify/functions/delete-booking/{visitorId}/{slotId}")
    fun deleteVisit(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Path("slotId") slotId: String?
    ): Call<JsonObject>

    @DELETE(".netlify/functions/delete-slot/{slotId}/{userId}")
    fun deleteSlot(
        @Header("Authorization") token: String?,
        @Path("slotId") slotId: String?,
        @Path("userId") userId: String?
    ): Call<JsonObject>

    @POST(".netlify/functions/add-place")
    fun addPlace(
        @Header("Authorization") token: String?,
        @Body place: Place?
    ): Call<Void>

    @PUT(".netlify/functions/update-place/{placeId}")
    fun editPlace(
        @Header("Authorization") token: String?,
        @Body place: Place?,
        @Path("placeId") placeId: String?
    ): Call<Void>

    @POST(".netlify/functions/add-slot/{placeId}")
    fun addSlot(
        @Header("Authorization") token: String?,
        @Path("placeId") placeId: String?,
        @Body slot: Slot?
    ): Call<Void>

    @GET(".netlify/functions/change-favourite/{visitorId}/{placeId}")
    fun addRemoveFavourite(
        @Header("Authorization") token: String?,
        @Path("visitorId") visitorId: String?,
        @Path("placeId") placeId: String?
    ): Call<JsonObject?>?

    @POST(".netlify/functions/visit")
    fun planVisit(
        @Header("Authorization") token: String?,
        @Body body: HashMap<String?, Any?>?
    ): Call<JsonObject?>?
}
package dahoum.wales.access_app

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.*

interface RetrofitService2 {
    @GET("/access")
    fun allPlaces(): Call<List<JsonObject>>

    @GET("/access/demo")
    fun getDemo(): Call<JsonObject>

    @PUT("/access/{placeId}")
    fun updatePlace(@Path("placeId") placeId: String, @Body body: HashMap<String, String>): Call<JsonObject>
}
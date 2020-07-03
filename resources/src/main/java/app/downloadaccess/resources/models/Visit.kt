package app.downloadaccess.resources.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Visit(
    @field:Expose @field:SerializedName("startTime") val startTime: String,
    val viewType: Int
) {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("type")
    @Expose
    val type: String? = null

    @SerializedName("endTime")
    @Expose
    val endTime: String? = null

    @SerializedName("visitors")
    @Expose
    val visitors: Int? = null

    @SerializedName("occupiedSlots")
    @Expose
    val occupiedSlots: Int? = null

    @SerializedName("maxSlots")
    @Expose
    val maxSlots: Int? = null

    @SerializedName("slotId")
    @Expose
    val slotId: String? = null

    @SerializedName("placeId")
    @Expose
    val placeId: String? = null

}
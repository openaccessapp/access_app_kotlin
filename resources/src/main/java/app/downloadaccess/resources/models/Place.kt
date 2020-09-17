package app.downloadaccess.resources.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Place : Serializable {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("typeId")
    @Expose
    var typeId = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("image")
    @Expose
    private var image: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("www")
    @Expose
    var www: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("location")
    @Expose
    var location: String? = null

    @SerializedName("isFavourite")
    @Expose
    var isFavourite: Boolean? = null

    @SerializedName("approved")
    @Expose
    var approved: Boolean? = false

    fun     setImage(image: String?) {
        this.image = image
    }

}
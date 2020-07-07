package org.msfox.maptaxitest.model


import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "vehicle_table", primaryKeys = ["bearing", "lat", "lng"])
data class Vehicle(
    @SerializedName("type")
    var type: String,
    @SerializedName("lat")
    var lat: Double,
    @SerializedName("lng")
    var lng: Double,
    @SerializedName("bearing")
    var bearing: Int,
    @SerializedName("image_url")
    var imageUrl: String
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Vehicle

        if (type != other.type) return false
        if (lat != other.lat) return false
        if (lng != other.lng) return false
        if (bearing != other.bearing) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + lat.hashCode()
        result = 31 * result + lng.hashCode()
        result = 31 * result + bearing
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}
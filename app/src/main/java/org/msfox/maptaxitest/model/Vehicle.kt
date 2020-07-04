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
)
package org.msfox.maptaxitest.model


import com.google.gson.annotations.SerializedName

data class Document(
    @SerializedName("vehicles")
    var vehicles: List<Vehicle>
)
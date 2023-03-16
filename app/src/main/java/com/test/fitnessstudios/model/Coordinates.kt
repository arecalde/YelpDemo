package com.test.fitnessstudios.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName


class Coordinates (

  @SerializedName("latitude"  ) var latitude  : Double? = null,
  @SerializedName("longitude" ) var longitude : Double? = null

) {
  fun toLatLng(): LatLng? {
    val lat = latitude ?: return null
    val lon = longitude ?: return null
    return LatLng(lat, lon)
  }
}
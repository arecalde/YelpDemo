package com.test.fitnessstudios.model

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

class NavigationResult (

    @SerializedName("geocoded_waypoints" ) var geocodedWaypoints : ArrayList<GeocodedWaypoints> = arrayListOf(),
    @SerializedName("routes"             ) var routes            : ArrayList<Routes>            = arrayListOf(),
    @SerializedName("status"             ) var status            : String?                      = null

) {
    var origin: LatLng? = null
    var destination: LatLng? = null
}
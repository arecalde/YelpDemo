package com.test.fitnessstudios.model

import com.google.gson.annotations.SerializedName
import com.test.fitnessstudios.model.Bounds
import com.test.fitnessstudios.model.Legs
import com.test.fitnessstudios.model.OverviewPolyline


data class Routes (

    @SerializedName("bounds"            ) var bounds           : Bounds?           = Bounds(),
    @SerializedName("copyrights"        ) var copyrights       : String?           = null,
    @SerializedName("legs"              ) var legs             : ArrayList<Legs>   = arrayListOf(),
    @SerializedName("overview_polyline" ) var overviewPolyline : OverviewPolyline? = OverviewPolyline(),
    @SerializedName("summary"           ) var summary          : String?           = null,
    @SerializedName("warnings"          ) var warnings         : ArrayList<String> = arrayListOf(),
    @SerializedName("waypoint_order"    ) var waypointOrder    : ArrayList<String> = arrayListOf()

)
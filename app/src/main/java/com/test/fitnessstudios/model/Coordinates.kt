package com.test.fitnessstudios.model

import com.google.gson.annotations.SerializedName


data class Coordinates (

  @SerializedName("latitude"  ) var latitude  : Double? = null,
  @SerializedName("longitude" ) var longitude : Double? = null

)
package com.test.fitnessstudios.model

import com.google.gson.annotations.SerializedName


data class Center (

  @SerializedName("longitude" ) var longitude : Double? = null,
  @SerializedName("latitude"  ) var latitude  : Double? = null

)
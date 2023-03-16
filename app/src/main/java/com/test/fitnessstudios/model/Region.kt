package com.test.fitnessstudios.model

import com.google.gson.annotations.SerializedName


data class Region (

  @SerializedName("center" ) var center : Center? = Center()

)
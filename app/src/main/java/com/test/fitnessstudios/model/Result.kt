package com.test.fitnessstudios.model

import com.google.gson.annotations.SerializedName


data class Result (

  @SerializedName("businesses" ) var businesses : ArrayList<Businesses> = arrayListOf(),
  @SerializedName("total"      ) var total      : Int?                  = null,
  @SerializedName("region"     ) var region     : Region?               = Region()

)
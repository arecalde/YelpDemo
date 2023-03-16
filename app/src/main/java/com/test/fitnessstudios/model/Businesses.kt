package com.test.fitnessstudios.model

import com.google.gson.annotations.SerializedName
import java.math.RoundingMode
import java.text.DecimalFormat

class Businesses (

    @SerializedName("id"            ) var id           : String?               = null,
    @SerializedName("alias"         ) var alias        : String?               = null,
    @SerializedName("name"          ) var name         : String?               = null,
    @SerializedName("image_url"     ) var imageUrl     : String?               = null,
    @SerializedName("is_closed"     ) var isClosed     : Boolean?              = null,
    @SerializedName("url"           ) var url          : String?               = null,
    @SerializedName("review_count"  ) var reviewCount  : Int?                  = null,
    @SerializedName("categories"    ) var categories   : ArrayList<Categories> = arrayListOf(),
    @SerializedName("rating"        ) var rating       : Double?                  = null,
    @SerializedName("coordinates"   ) var coordinates  : Coordinates?          = Coordinates(),
    @SerializedName("transactions"  ) var transactions : ArrayList<String>     = arrayListOf(),
    @SerializedName("location"      ) var location     : Location?             = Location(),
    @SerializedName("phone"         ) var phone        : String?               = null,
    @SerializedName("display_phone" ) var displayPhone : String?               = null,
    @SerializedName("distance"      ) var distance     : Double               = 0.0

) {
    fun getSubtitle(): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val roundedDistance = df.format(distance/1609.0)
        return "$$$$ · $roundedDistance mi"
    }
}
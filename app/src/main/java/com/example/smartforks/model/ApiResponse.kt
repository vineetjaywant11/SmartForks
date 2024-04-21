package com.example.smartforks.model
import com.google.gson.annotations.SerializedName


data class ApiResponse (

//  @SerializedName("Day"       ) var Day       : String?    = null,
  @SerializedName("Breakfast" ) var Breakfast : Breakfast? = Breakfast(),
  @SerializedName("Lunch"     ) var Lunch     : Lunch?     = Lunch(),
  @SerializedName("Dinner"    ) var Dinner    : Dinner?    = Dinner()

)
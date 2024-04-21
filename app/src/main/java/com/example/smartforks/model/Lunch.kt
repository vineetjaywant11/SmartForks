package com.example.smartforks.model
import com.google.gson.annotations.SerializedName


data class Lunch (

  @SerializedName("name"        ) var name        : String? = null,
  @SerializedName("ingredients" ) var ingredients : String? = null,
  @SerializedName("process"     ) var process     : String? = null,
  @SerializedName("macros"      ) var macros      : String? = null

)
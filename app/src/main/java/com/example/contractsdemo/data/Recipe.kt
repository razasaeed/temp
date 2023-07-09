package com.example.contractsdemo.data

import com.google.gson.annotations.SerializedName

data class Recipe(
    val heading: String,
    @SerializedName("recipe_image_url")
    val imageUrl: String,
    var isCustom: Boolean = false)
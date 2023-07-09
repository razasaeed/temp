package com.example.contractsdemo.data

import com.google.gson.annotations.SerializedName

data class NewRecipe(
    val heading: String,
    @SerializedName("recipe_image_url")
    val imageUrl: String,
    val detail: String,
    val steps: String,
    val ingredients: String,
    val methods: String
)
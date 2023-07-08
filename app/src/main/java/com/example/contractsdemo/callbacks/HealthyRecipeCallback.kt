package com.example.contractsdemo.callbacks

import com.example.contractsdemo.data.HealthyRecipeData

interface HealthyRecipeCallback {

    fun onRecipeClick(recipe: HealthyRecipeData)

}
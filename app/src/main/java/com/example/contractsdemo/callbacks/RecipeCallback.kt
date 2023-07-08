package com.example.contractsdemo.callbacks

import com.example.contractsdemo.data.Recipe

interface RecipeCallback {

    fun onRecipeClick(recipe: Recipe)

}
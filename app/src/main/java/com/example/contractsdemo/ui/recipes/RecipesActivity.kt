package com.example.contractsdemo.ui.recipes

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.contractsdemo.callbacks.RecipeCallback
import com.example.contractsdemo.data.Recipe
import com.example.contractsdemo.data.SharedData
import com.example.contractsdemo.data.SharedData.FOUR
import com.example.contractsdemo.data.SharedData.ONE
import com.example.contractsdemo.data.SharedData.RECIPE_NAME
import com.example.contractsdemo.data.SharedData.RECIPE_PIC
import com.example.contractsdemo.data.SharedData.THREE
import com.example.contractsdemo.data.SharedData.TWO
import com.example.contractsdemo.data.SharedData.first
import com.example.contractsdemo.data.SharedData.forth
import com.example.contractsdemo.data.SharedData.second
import com.example.contractsdemo.data.SharedData.third
import com.example.contractsdemo.databinding.ActivityRecipesBinding
import com.example.contractsdemo.ui.recipe_detail.RecipeDetailActivity
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class RecipesActivity : AppCompatActivity(), RecipeCallback {

    lateinit var binding: ActivityRecipesBinding
    val recipesList = ArrayList<Recipe>()
    lateinit var recipesHeadings: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        recipesHeadings = when (SharedData.OPTION) {
            TWO -> second
            THREE -> third
            FOUR -> forth
            else -> first
        }

        GetRecipe().execute("https://www.nhs.uk/start-for-life/baby/recipes-and-meal-ideas/")

        binding.apply {
            toolbar.tvTitle.text = "Recipes"
        }
    }

    private inner class GetRecipe : AsyncTask<String, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: String): Void? {
            try {
                // Connect to the web site
                val mBlogDocument: Document = Jsoup.connect(params[0]).get()

                try {

                    val elements: Elements =
                        mBlogDocument.getElementsByClass("nhsuk-promo__heading")
                    val imgs: Elements = mBlogDocument.select("img.nhsuk-promo__img")

                    recipesList.clear()
                    for (i in 0 until elements.size) {
                        val heading = elements[i].text()
                        val imageUrl = imgs[i].attr("src")
                        val recipe = Recipe(heading, imageUrl)
                        if (recipesHeadings.any { heading.startsWith(it) }) {
                            recipesList.add(recipe)
                        }
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            binding.pb.isVisible = false
            val adapter = RecipeAdapter(recipesList, this@RecipesActivity)
            binding.rvRecipes.adapter = adapter
        }
    }

    override fun onRecipeClick(recipe: Recipe) {
        RECIPE_NAME = recipe.heading
        RECIPE_PIC = recipe.imageUrl
        startActivity(Intent(this@RecipesActivity, RecipeDetailActivity::class.java))
    }


}
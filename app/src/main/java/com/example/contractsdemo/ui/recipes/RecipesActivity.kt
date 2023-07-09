package com.example.contractsdemo.ui.recipes

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.contractsdemo.callbacks.RecipeCallback
import com.example.contractsdemo.data.NewRecipe
import com.example.contractsdemo.data.Recipe
import com.example.contractsdemo.data.SharedData
import com.example.contractsdemo.data.SharedData.FOUR
import com.example.contractsdemo.data.SharedData.ONE
import com.example.contractsdemo.data.SharedData.RECIPE_NAME
import com.example.contractsdemo.data.SharedData.RECIPE_PIC
import com.example.contractsdemo.data.SharedData.THREE
import com.example.contractsdemo.data.SharedData.TWO
import com.example.contractsdemo.data.SharedData.customRecipe
import com.example.contractsdemo.data.SharedData.first
import com.example.contractsdemo.data.SharedData.forth
import com.example.contractsdemo.data.SharedData.isCustomCheck
import com.example.contractsdemo.data.SharedData.newRecipes
import com.example.contractsdemo.data.SharedData.second
import com.example.contractsdemo.data.SharedData.third
import com.example.contractsdemo.databinding.ActivityRecipesBinding
import com.example.contractsdemo.ui.add_recipe.AddRecipeActivity
import com.example.contractsdemo.ui.recipe_detail.RecipeDetailActivity
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class RecipesActivity : AppCompatActivity(), RecipeCallback {

    @Inject
    lateinit var client: OkHttpClient
    @Inject
    lateinit var gson: Gson

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
            toolbar.ivAdd.isVisible = true

            toolbar.ivAdd.setOnClickListener {
                startActivity(Intent(this@RecipesActivity, AddRecipeActivity::class.java))
            }
        }
    }

    private fun getRecipes() {
        val endpoint = "get_recipes.php"
        val url = SharedData.baseUrl + endpoint + "?type=${SharedData.OPTION}"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()

                if (response.isSuccessful && responseBody != null) {

                    try {
                        Log.d("checkk", responseBody.toString())

                        val recipes = gson.fromJson(responseBody, Array<Recipe>::class.java)
                        newRecipes = gson.fromJson(responseBody, Array<NewRecipe>::class.java)

                        for (recipe in recipes) {
                            recipe.isCustom = true
                            recipesList.add(recipe)
                        }

                        if (recipesList.size > 0) {
                            runOnUiThread {
                                binding.pb.isVisible = false
                                val adapter = RecipeAdapter(recipesList, this@RecipesActivity)
                                binding.rvRecipes.adapter = adapter
                            }
                        }

                        for (newRecipe in newRecipes) {
                            // Access newRecipe properties (e.g., newRecipe.heading, newRecipe.imageUrl, etc.)
                            // and perform desired operations
                        }
                    } catch (e: JsonSyntaxException) {
                        runOnUiThread {
                            binding.pb.isVisible = false
                            val adapter = RecipeAdapter(recipesList, this@RecipesActivity)
                            binding.rvRecipes.adapter = adapter
                        }
                    }

                } else {
                }
            }

            override fun onFailure(call: Call, e: IOException) {
            }
        })
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
            if (recipesList.size > 0) {
                getRecipes()
            }
        }
    }

    override fun onRecipeClick(recipe: Recipe) {
        RECIPE_NAME = recipe.heading
        RECIPE_PIC = recipe.imageUrl
        isCustomCheck = recipe.isCustom
        startActivity(Intent(this@RecipesActivity, RecipeDetailActivity::class.java))
    }


}
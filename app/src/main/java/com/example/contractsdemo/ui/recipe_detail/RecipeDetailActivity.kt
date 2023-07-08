package com.example.contractsdemo.ui.recipe_detail

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
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
import com.example.contractsdemo.databinding.ActivityRecipeDetailBinding
import com.example.contractsdemo.databinding.ActivityRecipesBinding
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException

class RecipeDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecipeDetailBinding
    val recipesList = ArrayList<Recipe>()
    lateinit var recipesHeadings: Array<String>
    lateinit var descirptionElement: Elements
    lateinit var stepsElement: Elements
    lateinit var ingredientsElement: Elements
    lateinit var methodsElement: Elements
    var stepsString = arrayListOf<String>()
    var ingredientsString = arrayListOf<String>()
    var methodsString = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val recipeName = RECIPE_NAME.lowercase().replace(" ", "-")
        GetRecipe().execute("https://www.nhs.uk/start-for-life/baby/recipes-and-meal-ideas/${recipeName}")

        binding.apply {
            toolbar.tvTitle.text = "Recipe Detail"
            tvHeading.text = "$RECIPE_NAME recipe"
            Glide.with(binding.root)
                .load(RECIPE_PIC)
                .into(ivHeader)
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

                    descirptionElement =
                        mBlogDocument.select("div[class=bh-recipe__description]").select("p")
                    stepsElement = mBlogDocument.getElementsByClass("nhsuk-u-margin-bottom-0")
                    for (i in 0 until stepsElement.size) {
                        stepsString.add(stepsElement[i].text())
                    }
                    ingredientsElement = mBlogDocument.select("div.nhsuk-grid-column-one-third")[1].select("ul").select("li")
                    for (i in 0 until ingredientsElement.size) {
                        ingredientsString.add(ingredientsElement[i].text())
                    }
                    methodsElement = mBlogDocument.select("div.bh-recipe-instructions__method ol li")
                    for (i in 1 until methodsElement.size) {
                        methodsString.add("$i- ${methodsElement[i].text()}")
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
            Log.d("gametest", methodsString.size.toString())
            binding.apply {
                tvDetail.text = descirptionElement.text()
                for (i in stepsString) {
                    tvSteps.text = "${tvSteps.text} \n $i"
                }
                for (i in ingredientsString) {
                    tvIngredients.text = "${tvIngredients.text} \n $i"
                }
                for (i in methodsString) {
                    tvMethods.text = "${tvMethods.text} \n $i"
                }
                pb.isVisible = false
            }
        }
    }


}
package com.example.contractsdemo.ui.add_recipe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.contractsdemo.data.SharedData
import com.example.contractsdemo.data.SharedData.baseUrl
import com.example.contractsdemo.databinding.ActivityAddRecipeBinding
import com.example.contractsdemo.webservices.ApiResponse
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

@AndroidEntryPoint
class AddRecipeActivity : AppCompatActivity() {

    @Inject
    lateinit var client: OkHttpClient
    @Inject
    lateinit var gson: Gson
    lateinit var binding: ActivityAddRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.apply {
            btnAddRecipe.setOnClickListener {
                validateFieldsAndAddRecipe()
            }
        }

    }

    private fun validateFieldsAndAddRecipe() {
        binding.apply {
            val heading = etHeading.editText.text.toString().trim()
            val imageUrl = etImageUrl.editText.text.toString().trim()
            val detail = etDetail.editText.text.toString().trim()
            val steps = etSteps.editText.text.toString().trim()
            val ingredients = etIngredients.editText.text.toString().trim()
            val methods = etMethods.editText.text.toString().trim()

            if (heading.isEmpty() || imageUrl.isEmpty() || detail.isEmpty() ||
                steps.isEmpty() || ingredients.isEmpty() || methods.isEmpty()
            ) {
                Toast.makeText(this@AddRecipeActivity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Call the API to add the recipe
                addRecipe(heading, imageUrl, detail, steps, ingredients, methods)
            }
        }
    }

    private fun addRecipe(
        heading: String,
        imageUrl: String,
        detail: String,
        steps: String,
        ingredients: String,
        methods: String
    ) {

        lifecycleScope.launch(Dispatchers.IO) {
            val endpoint = "insert_recipe.php"
            val requestBody = FormBody.Builder()
                .add("heading", heading)
                .add("recipe_image_url", imageUrl)
                .add("detail", detail)
                .add("steps", steps)
                .add("ingredients", ingredients)
                .add("methods", methods)
                .add("type", SharedData.OPTION.toString())
                .build()

            val request = Request.Builder()
                .url(baseUrl + endpoint)
                .post(requestBody)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    if (response.isSuccessful && responseBody != null) {
                        Log.d("checkflow", responseBody.toString())
                        val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                        runOnUiThread {
                            if (apiResponse.status == "success") {
                                Toast.makeText(
                                    this@AddRecipeActivity,
                                    "Recipe added successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                clearFields()
                            } else {
                                Toast.makeText(
                                    this@AddRecipeActivity,
                                    "Error in inserting data",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@AddRecipeActivity,
                                "Error in inserting data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onFailure(call: Call, e: IOException) {
                    // Handle network or other errors
                }
            })
        }
    }

    private fun clearFields() {
        binding.apply {
            etHeading.editText.setText("")
            etImageUrl.editText.setText("")
            etDetail.editText.setText("")
            etSteps.editText.setText("")
            etIngredients.editText.setText("")
            etMethods.editText.setText("")
        }
    }
}
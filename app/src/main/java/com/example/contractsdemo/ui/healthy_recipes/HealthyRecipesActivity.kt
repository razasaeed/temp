package com.example.contractsdemo.ui.healthy_recipes

import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.contractsdemo.callbacks.HealthyRecipeCallback
import com.example.contractsdemo.data.HealthyRecipeData
import com.example.contractsdemo.data.Recipe
import com.example.contractsdemo.databinding.ActivityHealthyRecipesBinding
import com.example.contractsdemo.databinding.DialogHealthyRecipesBinding
import com.example.contractsdemo.databinding.DialogRecoverPasswordBinding

class HealthyRecipesActivity : AppCompatActivity(), HealthyRecipeCallback {

    lateinit var binding: ActivityHealthyRecipesBinding
    lateinit var dialogHealthyRecipesBinding: DialogHealthyRecipesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHealthyRecipesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            toolbar.tvTitle.text = "Weight Gain Recipes"

            val adapter = HealthyRecipeAdapter( this@HealthyRecipesActivity)
            binding.rvRecipes.adapter = adapter
        }
    }

    override fun onRecipeClick(recipe: HealthyRecipeData) {
        showDetailDialog(recipe)
    }

    private fun showDetailDialog(recipe: HealthyRecipeData) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(recipe.name)

        dialogHealthyRecipesBinding = DialogHealthyRecipesBinding.inflate(LayoutInflater.from(this@HealthyRecipesActivity))
        builder.setView(dialogHealthyRecipesBinding.root)

        dialogHealthyRecipesBinding.etDetail.text = recipe.detail

        builder.setPositiveButton("Got it") { dialog, which ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}
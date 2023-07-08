package com.example.contractsdemo.ui.healthy_recipes

import com.example.contractsdemo.databinding.ItemCategorySquareBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contractsdemo.callbacks.HealthyRecipeCallback
import com.example.contractsdemo.data.HealthyRecipeData
import com.example.contractsdemo.data.SharedData

class HealthyRecipeAdapter(private val recipeCallback: HealthyRecipeCallback) :
    RecyclerView.Adapter<HealthyRecipeAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategorySquareBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val healthyRecipeData = HealthyRecipeData(
            SharedData.healthyRecipesNames[position],
            SharedData.healthyRecipesIcons[position],
            SharedData.healthyRecipesDescription[position]
        )
        holder.bind(healthyRecipeData)
    }

    override fun getItemCount(): Int {
        return SharedData.healthyRecipesNames.size
    }

    inner class CategoryViewHolder(private val binding: ItemCategorySquareBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: HealthyRecipeData) {
            binding.tvRecipe.text = recipe.name
            Glide.with(binding.root)
                .load(recipe.icon)
                .into(binding.ivRecipe)
            itemView.setOnClickListener {
                recipeCallback.onRecipeClick(recipe)
            }
        }
    }
}

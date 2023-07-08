package com.example.contractsdemo.ui.recipes

import com.example.contractsdemo.data.Recipe
import com.example.contractsdemo.databinding.ItemCategorySquareBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contractsdemo.callbacks.RecipeCallback

class RecipeAdapter(private val dataList: List<Recipe>, private val recipeCallback: RecipeCallback) :
    RecyclerView.Adapter<RecipeAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategorySquareBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val quote = dataList[position]
        holder.bind(quote)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class CategoryViewHolder(private val binding: ItemCategorySquareBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {
            binding.tvRecipe.text = recipe.heading
            Glide.with(binding.root)
                .load(recipe.imageUrl)
                .into(binding.ivRecipe)
            itemView.setOnClickListener {
                recipeCallback.onRecipeClick(recipe)
            }
        }
    }
}

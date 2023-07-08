package com.example.contractsdemo.ui.weight_gain

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contractsdemo.databinding.ActivityWeightGainBinding
import com.example.contractsdemo.ui.healthy_recipes.HealthyRecipesActivity

class WeightGainActivity : AppCompatActivity() {

    lateinit var binding: ActivityWeightGainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeightGainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            toolbar.tvTitle.text = "Weight Gain Foods"
            btnHealthyRecipes.setOnClickListener {
                startActivity(Intent(this@WeightGainActivity, HealthyRecipesActivity::class.java))
            }
        }
    }

}
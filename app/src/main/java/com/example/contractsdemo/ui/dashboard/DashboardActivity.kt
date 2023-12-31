package com.example.contractsdemo.ui.dashboard

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contractsdemo.R
import com.example.contractsdemo.data.SharedData.FOUR
import com.example.contractsdemo.data.SharedData.ONE
import com.example.contractsdemo.data.SharedData.OPTION
import com.example.contractsdemo.data.SharedData.THREE
import com.example.contractsdemo.data.SharedData.TWO
import com.example.contractsdemo.databinding.ActivityDashboardBinding
import com.example.contractsdemo.databinding.ActivityRegisterBinding
import com.example.contractsdemo.ui.recipes.RecipesActivity
import com.example.contractsdemo.ui.signin.SignInActivity
import com.example.contractsdemo.ui.weight_gain.WeightGainActivity

class DashboardActivity : AppCompatActivity() {

    lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            btnFirst.setOnClickListener {
                OPTION = ONE
                val intent = Intent(this@DashboardActivity, RecipesActivity::class.java)
                startActivity(intent)
            }
            btnSecond.setOnClickListener {
                OPTION = TWO
                val intent = Intent(this@DashboardActivity, RecipesActivity::class.java)
                startActivity(intent)
            }
            btnThird.setOnClickListener {
                OPTION = THREE
                val intent = Intent(this@DashboardActivity, RecipesActivity::class.java)
                startActivity(intent)
            }
            btnForth.setOnClickListener {
                OPTION = FOUR
                val intent = Intent(this@DashboardActivity, RecipesActivity::class.java)
                startActivity(intent)
            }
            btnFifth.setOnClickListener {
                val intent = Intent(this@DashboardActivity, WeightGainActivity::class.java)
                startActivity(intent)
            }
            btnSixth.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=child+health+clinics"))
                intent.setPackage("com.google.android.apps.maps") // Specify the package name to ensure it opens in Google Maps
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
        }
    }
}
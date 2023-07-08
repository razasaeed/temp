package com.example.contractsdemo.ui.welcome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contractsdemo.databinding.ActivityWelcomeBinding
import com.example.contractsdemo.ui.options.OptionsActivity

class WelcomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            btnNext.setOnClickListener {
                val intent = Intent(this@WelcomeActivity, OptionsActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
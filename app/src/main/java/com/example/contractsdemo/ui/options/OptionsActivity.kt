package com.example.contractsdemo.ui.options

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.contractsdemo.databinding.ActivityOptionsBinding
import com.example.contractsdemo.ui.register.RegisterActivity
import com.example.contractsdemo.ui.signin.SignInActivity

class OptionsActivity : AppCompatActivity() {

    lateinit var binding: ActivityOptionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOptionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            btnLogin.setOnClickListener {
                val intent = Intent(this@OptionsActivity, SignInActivity::class.java)
                startActivity(intent)
            }
            btnRegister.setOnClickListener {
                val intent = Intent(this@OptionsActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }

    }
}
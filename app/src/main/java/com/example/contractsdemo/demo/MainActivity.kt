package com.example.contractsdemo.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.contractsdemo.R
import com.example.contractsdemo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val viewModel: DemoViewModel by viewModels()

    init {
        collectState()
        collectEffect()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGetUsers.setOnClickListener {
            viewModel.sendEvent(DemoEvent.GetUsers)
        }
    }

    private fun collectState() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                if (state.isLoading) {
                    // show loading
                } else {
                    state.usersResult.let {
                        Log.d("usersResult", it?.size.toString())
                    }
                }
            }
        }
    }

    private fun collectEffect() {
        lifecycleScope.launchWhenStarted {
            viewModel.effect.collect { effect ->
                when (effect) {
                    is DemoEffect.ErrorWithGetUsers -> {
                        effect.message
                    }
                }
            }
        }
    }

}
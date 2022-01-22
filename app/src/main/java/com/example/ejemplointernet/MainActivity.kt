package com.example.ejemplointernet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import com.example.ejemplointernet.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()

        binding.etNumber.doAfterTextChanged {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.shouldEnableButton(binding.etNumber.text.toString())
            }
        }

        binding.bDescarga.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.sendGetPlanetRequest(binding.etNumber.text.toString())
            }
        }
    }

    private fun initObservers() {
        viewModel.buttonEnabled.observe(this) { enabled ->
            binding.bDescarga.isEnabled = enabled
        }
        viewModel.errorMessage.observe(this) { errorMessage ->
            binding.tvPlanet.text = errorMessage
        }
        viewModel.loadingViewVisible.observe(this) { visible ->
            binding.pbDownloading.visibility = if (visible) View.VISIBLE else View.GONE
        }
        viewModel.planetReceived.observe(this) { planetReceived ->
            binding.tvPlanet.text = planetReceived
        }
    }
}
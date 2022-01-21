package com.example.ejemplointernet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.ejemplointernet.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.coroutineContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bDescarga.setOnClickListener {
            binding.pbDownloading.visibility = View.VISIBLE

            val client = OkHttpClient()

            val request = Request.Builder()
            request.url("https://swapi.dev/api/planets/1/")


            val call = client.newCall(request.build())
            call.enqueue( object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    println(e.toString())
                    CoroutineScope(Dispatchers.Main).launch {
                        Toast.makeText(this@MainActivity, "Algo ha ido mal", Toast.LENGTH_SHORT).show()
                        binding.pbDownloading.visibility = View.GONE
                        binding.tvPlanet.text = "Algo ha ido mal"
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    println(response.toString())
                    response.body?.let { responseBody ->
                        val body = responseBody.string()
                        println(body)
                        val gson = Gson()
                        val planet = gson.fromJson(body, Planet::class.java)

                        println(planet)
                        CoroutineScope(Dispatchers.Main).launch {
                            binding.pbDownloading.visibility = View.GONE
                            Toast.makeText(this@MainActivity, "$planet", Toast.LENGTH_SHORT).show()
                            binding.tvPlanet.text = planet.name
                        }
                    }
                }
            })
        }
    }
}
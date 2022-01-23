package com.example.ejemplointernet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.IOException

class MainActivityViewModel : ViewModel() {


    private val _buttonEnabled by lazy { MediatorLiveData<Boolean>() }
    val buttonEnabled: LiveData<Boolean>
        get() = _buttonEnabled

    private val _loadingViewVisible by lazy { MediatorLiveData<Boolean>() }
    val loadingViewVisible: LiveData<Boolean>
        get() = _loadingViewVisible

    private val _errorMessage by lazy { MediatorLiveData<String>() }
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _planetReceived by lazy { MediatorLiveData<String>() }
    val planetReceived: LiveData<String>
        get() = _planetReceived

    private suspend fun setButtonEnabledOnMainThread(buttonEnabled: Boolean) = withContext(Dispatchers.Main) {
        _buttonEnabled.value = buttonEnabled
    }

    private suspend fun setPlanetReceivedOnMainThread(planetReceived: String) = withContext(Dispatchers.Main) {
        _planetReceived.value = planetReceived
    }

    private suspend fun setErrorMessageOnMainThread(errorMessage: String) = withContext(Dispatchers.Main) {
        _errorMessage.value = errorMessage
    }

    private suspend fun setLoadingViewVisibleOnMainThread(visible: Boolean) = withContext(Dispatchers.Main) {
        _loadingViewVisible.value = visible
    }

    suspend fun shouldEnableButton(text: String) {
        val number = text.toIntOrNull()
        setButtonEnabledOnMainThread(number != null && number >= 0)
    }

    private fun generateUrlPrivate(id: String) : String {
        return "https://swapi.dev/api/planets/${id.toInt()}/"
    }

    internal fun generateUrlInternal(id: String) : String {
        return "https://swapi.dev/api/planets/${id.toInt()}/"
    }

    suspend fun sendGetPlanetRequestCallbacks(id: String) = withContext(Dispatchers.IO) {
        setLoadingViewVisibleOnMainThread(true)
        val client = OkHttpClient()

        val request = Request.Builder()
        request.url(generateUrlPrivate(id))
        val call = client.newCall(request.build())

        call.enqueue( object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                CoroutineScope(Dispatchers.IO).launch {
                    println(e.toString())
                    setLoadingViewVisibleOnMainThread(false)
                    setErrorMessageOnMainThread("Algo ha ido mal")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                CoroutineScope(Dispatchers.IO).launch {
                    println(response.toString())
                    when (response.code) {
                        200 -> response.body?.let { responseBody ->
                            val body = responseBody.string()
                            println(body)
                            val gson = Gson()
                            val planet = gson.fromJson(body, Planet::class.java)

                            println(planet)

                            setLoadingViewVisibleOnMainThread(false)
                            setPlanetReceivedOnMainThread(planet.name)
                        }
                        else -> {

                            setLoadingViewVisibleOnMainThread(false)
                            setErrorMessageOnMainThread(response.code.toString())
                        }
                    }
                }
            }
        })
    }

    suspend fun sendGetPlanetRequestSynchronous(id: String) = withContext(Dispatchers.IO) {
        setLoadingViewVisibleOnMainThread(true)
        val client = OkHttpClient()

        val request = Request.Builder()
        request.url(generateUrlPrivate(id))
        val call = client.newCall(request.build())
        try {
            val response = call.execute()
            withContext(Dispatchers.Main) {
                println(response.toString())
                when (response.code) {
                    200 -> response.body?.let { responseBody ->
                        val body = responseBody.string()
                        println(body)
                        val gson = Gson()
                        val planet = gson.fromJson(body, Planet::class.java)

                        println(planet)

                        setLoadingViewVisibleOnMainThread(false)
                        setPlanetReceivedOnMainThread(planet.name)
                    }
                    else -> {

                        setLoadingViewVisibleOnMainThread(false)
                        setErrorMessageOnMainThread(response.code.toString())
                    }
                }
            }
        } catch (exception: IOException) {
            withContext(Dispatchers.Main){
                println(exception.toString())
                setLoadingViewVisibleOnMainThread(false)
                setErrorMessageOnMainThread("Algo ha ido mal")
            }
        }
    }
}
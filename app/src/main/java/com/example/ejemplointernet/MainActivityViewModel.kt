package com.example.ejemplointernet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivityViewModel : ViewModel() {


    private val _buttonEnabled by lazy { MediatorLiveData<Boolean>() }
    val buttonEnabled: LiveData<Boolean>
        get() = _buttonEnabled

    suspend fun shouldEnableButton(text: String) = withContext(Dispatchers.Main) {
        val number = text.toIntOrNull()
        _buttonEnabled.value = number != null && number >= 0
    }

}
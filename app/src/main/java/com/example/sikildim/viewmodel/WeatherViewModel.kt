package com.example.sikildim.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sikildim.api.Constant
import com.example.sikildim.api.NetworkResponse
import com.example.sikildim.api.RetrofitInstance
import com.example.sikildim.api.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel :ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherData = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherData : MutableLiveData<NetworkResponse<WeatherModel>> = _weatherData

    fun getData (city : String){
        _weatherData.value = NetworkResponse.Loading
        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(Constant.apiKey, city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherData.value = NetworkResponse.Succes(it)
                    }
                } else {
                    _weatherData.value = NetworkResponse.Error("Failed to load data")
                }
            }
            catch (e : Exception){
                _weatherData.value = NetworkResponse.Error("Failed to load data")

            }
        }

        Log.e("City Name",city)
    }


}
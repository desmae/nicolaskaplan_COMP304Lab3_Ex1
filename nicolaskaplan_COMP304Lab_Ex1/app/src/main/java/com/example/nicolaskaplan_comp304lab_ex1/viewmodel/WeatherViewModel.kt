package com.example.nicolaskaplan_comp304lab_ex1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.FavoriteLocation
import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.WeatherData
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.FavoriteLocationDao
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.WeatherRepository
import kotlinx.coroutines.launch

open class WeatherViewModel(
    private val repository: WeatherRepository,
    favoriteLocationDao: FavoriteLocationDao
) : ViewModel() {

    private val _favoriteLocations = MutableLiveData<List<FavoriteLocation>>()
    val favoriteLocations: LiveData<List<FavoriteLocation>> get() = _favoriteLocations

    fun fetchFavoriteLocations() {
        viewModelScope.launch {
            val locations = repository.getAllFavoriteLocations()
            _favoriteLocations.postValue(locations)
        }
    }
    protected val _weatherData = MutableLiveData<WeatherData?>()
    val weatherData: LiveData<WeatherData?> get() = _weatherData

    protected val _isError = MutableLiveData<Boolean?>()
    val isError: LiveData<Boolean?> get() = _isError

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getWeather(lat: String, lon: String, apiKey: String) {
        viewModelScope.launch {
            val result = repository.fetchWeather(lat, lon, apiKey)
            result.onSuccess {
                _weatherData.postValue(it)
                _isError.postValue(false)
                _errorMessage.postValue(null)
            }.onFailure { error ->
                _weatherData.postValue(null)
                _isError.postValue(true)
                _errorMessage.postValue(error.message ?: "unknown error")
            }
        }
    }
    fun saveFavoriteLocation(location: FavoriteLocation) {
        viewModelScope.launch {
            repository.saveFavoriteLocation(location)
        }
    }

}

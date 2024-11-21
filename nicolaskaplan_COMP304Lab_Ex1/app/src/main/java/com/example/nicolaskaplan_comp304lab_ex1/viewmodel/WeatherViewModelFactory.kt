package com.example.nicolaskaplan_comp304lab_ex1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.FavoriteLocationDao
import com.example.nicolaskaplan_comp304lab_ex1.networkServices.WeatherRepository

class WeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val favoriteLocationDao: FavoriteLocationDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WeatherViewModel(repository, favoriteLocationDao) as T
        }
        throw IllegalArgumentException("unknown viewmodel")
    }
}
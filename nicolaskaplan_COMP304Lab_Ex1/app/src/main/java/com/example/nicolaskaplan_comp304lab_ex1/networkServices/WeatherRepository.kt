package com.example.nicolaskaplan_comp304lab_ex1.networkServices

import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.FavoriteLocation
import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherRepository(
    private val weatherService: WeatherService,
    private val favoriteLocationDao: FavoriteLocationDao
) {
    suspend fun getAllFavoriteLocations(): List<FavoriteLocation> {
        return favoriteLocationDao.getAllLocations()
    }

    suspend fun fetchWeather(lat: String, lon: String, apiKey: String): Result<WeatherData> {
        return withContext(Dispatchers.IO) {
            try {
                val response = weatherService.getWeatherData(lat, lon, apiKey)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    suspend fun saveFavoriteLocation(location: FavoriteLocation) {
        favoriteLocationDao.insertLocation(location)
    }
}
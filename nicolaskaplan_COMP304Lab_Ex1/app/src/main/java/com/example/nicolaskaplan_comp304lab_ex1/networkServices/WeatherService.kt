package com.example.nicolaskaplan_comp304lab_ex1.networkServices

import com.example.nicolaskaplan_comp304lab_ex1.dataClasses.WeatherData
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): WeatherData
}
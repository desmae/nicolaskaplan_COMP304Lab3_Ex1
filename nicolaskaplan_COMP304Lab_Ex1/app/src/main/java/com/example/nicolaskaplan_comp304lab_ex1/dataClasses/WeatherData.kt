package com.example.nicolaskaplan_comp304lab_ex1.dataClasses

data class WeatherData(
    val coord: Coord,
    val weather: List<WeatherDescription>,
    val main: MainWeatherData, // grabs all the main weather data
    val wind: Wind
)
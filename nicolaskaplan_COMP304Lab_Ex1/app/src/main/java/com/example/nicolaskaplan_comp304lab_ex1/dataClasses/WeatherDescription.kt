package com.example.nicolaskaplan_comp304lab_ex1.dataClasses

data class WeatherDescription(
    val main: String, // would say something like Cloudy, Rainy, etc.
    val description: String, // would say something like "broken clouds"
    val icon: String // would maybe provide an icon if i can find textures for that
)
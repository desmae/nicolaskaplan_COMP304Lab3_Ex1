package com.example.nicolaskaplan_comp304lab_ex1.dataClasses

data class Wind(
    val speed: Double, // wind speed
    val deg: Int, // wind direction
    val gust: Double? // optional field???
)
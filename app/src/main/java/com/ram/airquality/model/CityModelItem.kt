package com.ram.airquality.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CityModelItem(
    @Json(name = "aqi")
    val aqi: Double,
    @Json(name = "city")
    val city: String
)
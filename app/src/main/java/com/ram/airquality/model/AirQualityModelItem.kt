package com.ram.airquality.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "cityWiseAirReading")
@JsonClass(generateAdapter = true)
data class AirQualityModelItem(
    @PrimaryKey
    @Json(name = "city")
    val city: String,
    @Json(name = "aqi")
    val aqi: Double?,
    var timing: String? = ""
)
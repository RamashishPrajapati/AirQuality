package com.ram.airquality.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "cityWiseAirReading")
@JsonClass(generateAdapter = true)
data class AirQualityModelItem(
    @Json(name = "city")
    val city: String?,
    @Json(name = "aqi")
    val aqi: Double?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
package com.ram.airquality.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ram.airquality.model.AirQualityModelItem

/**
 * Created by Ramashish Prajapati on 31,August,2021
 */
@Dao
interface AirQualityDao {

    @Insert
    fun insertCityDetails(cityList: AirQualityModelItem): Long

    @Update
    fun updateCityDetails(cityList: AirQualityModelItem): Int

    @Query("SELECT * FROM cityWiseAirReading")
    fun getAllCityDetails(): List<AirQualityModelItem>

    @Query("SELECT * from cityWiseAirReading WHERE city= :city")
    fun getItem(city: String): List<AirQualityModelItem?>?

    /*@Query("SELECT city FROM cityWiseAirReading WHERE city Like :city")
    fun getItem(city: String): String?*/

    fun insertOrUpdate(cityList: AirQualityModelItem) {
        try {
            val itemsFromDB = getItem(cityList.city)
            if (itemsFromDB!!.isEmpty())
                insertCityDetails(cityList)
            else
                updateCityDetails(cityList)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
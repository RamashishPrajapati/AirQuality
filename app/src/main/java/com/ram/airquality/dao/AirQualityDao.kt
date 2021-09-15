package com.ram.airquality.dao

import androidx.lifecycle.LiveData
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
    suspend fun insertCityDetails(cityList: AirQualityModelItem): Long

    @Update
    suspend fun updateCityDetails(cityList: AirQualityModelItem): Int

    @Query("SELECT * FROM cityWiseAirReading")
    fun getAllCityDetails(): LiveData<List<AirQualityModelItem>>

    @Query("SELECT city FROM cityWiseAirReading WHERE city Like :city")
    suspend fun getItem(city: String): String?

    suspend fun insertOrUpdate(cityList: AirQualityModelItem) {
        try {
            val itemsFromDB = getItem(cityList.city)
            if (itemsFromDB == null)
                insertCityDetails(cityList)
            else
                updateCityDetails(cityList)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
package com.ram.airquality.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.ram.airquality.dao.AirQualityDao
import com.ram.airquality.database.AirQualityDatabase
import com.ram.airquality.repository.AirQualityRepository
import java.net.URI

/**
 * Created by Ramashish Prajapati on 31,August,2021
 */

class AirQualityViewModel(application: Application) : AndroidViewModel(application) {
    private val airQualityRepository: AirQualityRepository
    private val airQualityDao: AirQualityDao = AirQualityDatabase.getDatabase(application)
        .airQualityDao()

    init {
        airQualityRepository = AirQualityRepository(airQualityDao)
    }

    val getCityAirQualityList = airQualityDao.getAllCityDetails()

    fun connectToWebSocket(url: URI) {
        airQualityRepository.createWebSocketClient(url)
    }

    fun disconnectWebSocket() {
        airQualityRepository.closeWebsockets()
    }

}
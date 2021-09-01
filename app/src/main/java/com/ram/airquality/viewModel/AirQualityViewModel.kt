package com.ram.airquality.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.ram.airquality.database.AirQualityDatabase
import com.ram.airquality.model.AirQualityModelItem
import com.ram.airquality.repository.AirQualityRespository
import java.net.URI

/**
 * Created by Ramashish Prajapati on 31,August,2021
 */

class AirQualityViewModel(application: Application) : AndroidViewModel(application) {
    private val airQualityRepository: AirQualityRespository

    init {
        val airQualityDao =
            AirQualityDatabase.getDatabase(application, viewModelScope, application.resources)
                .airQualityDao()
        airQualityRepository = AirQualityRespository(airQualityDao)
    }

    fun connectToWebSocket(url: URI) {
        airQualityRepository.createWebSocketClient(url)
    }

    fun getAirQualityData(): LiveData<List<AirQualityModelItem>> {
        return airQualityRepository.aireQualityReponse()
    }

}
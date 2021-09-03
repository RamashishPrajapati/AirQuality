package com.ram.airquality.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ram.airquality.dao.AirQualityDao
import com.ram.airquality.model.AirQualityModelItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.reflect.Type
import java.net.URI
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


/**
 * Created by Ramashish Prajapati on 31,August,2021
 */
class AirQualityRepository(private val airQualityDao: AirQualityDao) {
    private lateinit var webSocketClient: WebSocketClient
    private var airQualityList = MutableLiveData<List<AirQualityModelItem>>()
    private val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")


    fun createWebSocketClient(airQualityUri: URI?) {
        webSocketClient = object : WebSocketClient(airQualityUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(javaClass.simpleName, "onOpen")
            }

            override fun onMessage(message: String?) {
                Log.d(javaClass.simpleName, "onMessage: $message")
                showCityWiseAirQuality(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(javaClass.simpleName, "onClose")
            }

            override fun onError(ex: Exception?) {
                Log.e(javaClass.simpleName, "onError: ${ex?.message}")
            }
        }
        webSocketClient.connect()//to connect the webSocketClient to server
    }

    fun closeWebsockets() {
        webSocketClient.close()
    }

    @SuppressLint("SetTextI18n")
    private fun showCityWiseAirQuality(message: String?) {
        try {
            message?.let { it ->
                val moshi = Moshi.Builder().build()
                val type: Type = Types.newParameterizedType(
                    List::class.java,
                    AirQualityModelItem::class.java
                )
                val adapter: JsonAdapter<ArrayList<AirQualityModelItem>> = moshi.adapter(type)
                val response = adapter.fromJson(it)

                for (city in response!!.iterator()) {
                    airQualityDao.insertOrUpdate(
                        AirQualityModelItem(
                            city.city,
                            city.aqi,
                            getCurrentTime()
                        )
                    )
                }
                getCityDetailFromDB()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun airQualityRepose(): LiveData<List<AirQualityModelItem>> {
        return airQualityList
    }

    private fun getCityDetailFromDB() {

        val cityDetails =
            airQualityDao.getAllCityDetails() as ArrayList

        cityDetails.forEachIndexed { index, airQualityModelItem ->
            cityDetails[index] = AirQualityModelItem(
                airQualityModelItem.city,
                airQualityModelItem.aqi,
                getTimeDifference(airQualityModelItem.timing!!)
            )
        }
        return airQualityList.postValue(cityDetails)
    }

    private fun getCurrentTime(): String? {
        val current = LocalDateTime.now()
        return current.format(formatter)
    }

    private fun getTimeDifference(timeFromDB: String): String {
        val currentTime = LocalTime.parse(getCurrentTime(), formatter)
        val storeTime = LocalTime.parse(timeFromDB, formatter)
        val minutes = ChronoUnit.MINUTES.between(storeTime, currentTime)
        return when {
            minutes < 1 -> "A few second ago"
            minutes.equals(1) -> "A minutes ago"
            minutes in 2..59 -> "$minutes minutes ago"
            minutes >= 60 -> "$timeFromDB"
            else -> "Updating"
        }
    }
}
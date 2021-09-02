package com.ram.airquality.repository

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ram.airquality.dao.AirQualityDao
import com.ram.airquality.model.AirQualityModelItem
import com.ram.airquality.ui.MainActivity
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.reflect.Type
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Ramashish Prajapati on 31,August,2021
 */
class AirQualityRespository(private val airQualityDao: AirQualityDao) {
    private lateinit var webSocketClient: WebSocketClient
    private var airQualityList = MutableLiveData<List<AirQualityModelItem>>()

    public fun createWebSocketClient(coinbaseUri: URI?) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(MainActivity.TAG, "onOpen")
                //subscribe() //to websockets by writing a format if any to connect with websockets
            }

            override fun onMessage(message: String?) {
                Log.d(MainActivity.TAG, "onMessage: $message")
                showCityWiseAirQuality(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(MainActivity.TAG, "onClose")
                //unsubscribe() to websockets by writing a format if any to disconnect with websockets
            }

            override fun onError(ex: Exception?) {
                Log.e("createWebSocketClient", "onError: ${ex?.message}")
            }
        }
        webSocketClient.connect()//to connect the webSocketClient to server
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
                    val date = Calendar.getInstance().time
                    val formatter =
                        SimpleDateFormat.getTimeInstance() //or use getDateInstance()
                    val formattedDate = formatter.format(date)
                    airQualityDao.insertOrUpdate(
                        AirQualityModelItem(
                            city.city,
                            city.aqi,
                            formattedDate
                        )
                    )
                }
                getCityDetailFromDB()
                //airQualityList.postValue(response!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun airQualityRepose(): LiveData<List<AirQualityModelItem>> {
        return airQualityList
    }

    fun getCityDetailFromDB() {
        val cityDetails = airQualityDao.getAllCityDetails()
        return airQualityList.postValue(cityDetails)
    }

}
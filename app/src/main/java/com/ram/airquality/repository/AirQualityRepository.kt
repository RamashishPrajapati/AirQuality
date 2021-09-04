package com.ram.airquality.repository

import android.annotation.SuppressLint
import android.util.Log
import com.ram.airquality.dao.AirQualityDao
import com.ram.airquality.model.AirQualityModelItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.reflect.Type
import java.net.URI


/**
 * Created by Ramashish Prajapati on 31,August,2021
 */
class AirQualityRepository(private val airQualityDao: AirQualityDao) {
    private lateinit var webSocketClient: WebSocketClient

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
        if (webSocketClient != null)
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
                            System.currentTimeMillis()
                        )
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
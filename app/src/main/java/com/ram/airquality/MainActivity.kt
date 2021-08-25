package com.ram.airquality

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ram.airquality.adapter.CityAirAdapter
import com.ram.airquality.databinding.ActivityMainBinding
import com.ram.airquality.model.CityModelItem
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.lang.reflect.Type
import java.net.URI


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var webSocketClient: WebSocketClient
    private lateinit var cityAirAdapter: CityAirAdapter

    companion object {
        const val WEB_SOCKET_URL = "ws://city-ws.herokuapp.com/" //"wss://ws-feed.pro.coinbase.com"
        const val TAG = "CityAirQuality"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.rvCityAirlist.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            cityAirAdapter = CityAirAdapter()
            adapter = cityAirAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
    }

    override fun onPause() {
        super.onPause()
        webSocketClient.close()
    }

    private fun initWebSocket() {
        val coinbaseUri = URI(WEB_SOCKET_URL)

        createWebSocketClient(coinbaseUri)
    }

    private fun createWebSocketClient(coinbaseUri: URI?) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                //subscribe() to websockets by writing a format if any to connect with websockets
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                showCityWiseAirQuality(message)
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
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
        message?.let {
            val moshi = Moshi.Builder().build()
            val type: Type = Types.newParameterizedType(
                List::class.java,
                CityModelItem::class.java
            )
            val adapter: JsonAdapter<List<CityModelItem>> = moshi.adapter(type)
            val cityAirList = adapter.fromJson(message)
            runOnUiThread {
                cityAirAdapter.submitList(cityAirList)
                cityAirAdapter.notifyDataSetChanged()

            }
        }
    }

    private fun subscribe() {
        webSocketClient.send("")
    }

    private fun unsubscribe() {
        webSocketClient.send("")
    }
}
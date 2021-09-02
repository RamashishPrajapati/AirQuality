package com.ram.airquality.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ram.airquality.adapter.AirQualityAdapter
import com.ram.airquality.databinding.ActivityMainBinding
import com.ram.airquality.model.AirQualityModelItem
import com.ram.airquality.viewModel.AirQualityViewModel
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
    private lateinit var airQualityAdapter: AirQualityAdapter
    private var tempList = ArrayList<AirQualityModelItem>()
    private lateinit var airQualityViewModel: AirQualityViewModel

    companion object {
        const val WEB_SOCKET_URL = "ws://city-ws.herokuapp.com/" //"wss://ws-feed.pro.coinbase.com"
        const val TAG = "CityAirQuality"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val airQualityUri = URI(WEB_SOCKET_URL)
        airQualityViewModel = ViewModelProvider(this).get(AirQualityViewModel::class.java)
        airQualityViewModel.connectToWebSocket(airQualityUri)
        initRecyclerview()
        setUpObserver()
        airQualityViewModel.getAirQualityData()

    }

    private fun initRecyclerview() {
        binding.rvCityAirlist.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
            airQualityAdapter = AirQualityAdapter()
            adapter = airQualityAdapter
        }
    }

    private fun setUpObserver() {
        airQualityViewModel.getAirQualityData().observe(this, Observer {
            it.let {
                airQualityAdapter.submitList(it)
                    //binding.rvCityAirlist.invalidate()
                airQualityAdapter.notifyDataSetChanged()

            }
        })
    }

    override fun onResume() {
        super.onResume()
        initWebSocket()
    }

    override fun onPause() {
        super.onPause()
        //webSocketClient.close()
    }

    private fun initWebSocket() {
        val coinbaseUri = URI(WEB_SOCKET_URL)
        //createWebSocketClient(coinbaseUri)
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
        message?.let { it ->
            val moshi = Moshi.Builder().build()
            val type: Type = Types.newParameterizedType(
                List::class.java,
                AirQualityModelItem::class.java
            )

            val adapter: JsonAdapter<ArrayList<AirQualityModelItem>> = moshi.adapter(type)
            val cityAirList = adapter.fromJson(it)
            if (tempList.isNullOrEmpty()) {
                tempList = cityAirList!!
            } else {
                val differencelist = cityAirList!!.minus(tempList)
                Log.d("temp 2", "$tempList")
                Log.d("city 2", "$cityAirList")
                Log.d("differencelist", "$differencelist")
                Log.d("test", "....................................................")
            }
            runOnUiThread {
                airQualityAdapter.submitList(cityAirList)
                binding.rvCityAirlist.invalidate()
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
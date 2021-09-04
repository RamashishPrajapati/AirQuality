package com.ram.airquality.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ram.airquality.adapter.AirQualityAdapter
import com.ram.airquality.databinding.ActivityMainBinding
import com.ram.airquality.utility.Constants
import com.ram.airquality.viewModel.AirQualityViewModel
import java.net.URI


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var airQualityAdapter: AirQualityAdapter
    private lateinit var airQualityViewModel: AirQualityViewModel
    private lateinit var airQualityUri: URI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        airQualityUri = URI(Constants.AIR_QUALITY_WEB_SOCKET_URL)
        airQualityViewModel = ViewModelProvider(this).get(AirQualityViewModel::class.java)
        initRecyclerview()
        setUpObserver()
        initListener()
    }

    private fun initListener() {
        binding.btnRefresh.setOnClickListener {
            airQualityViewModel.connectToWebSocket(airQualityUri)
        }
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
        airQualityViewModel.getCityAirQualityList.observe(this, {
            it.let {
                airQualityAdapter.submitList(it)
                //airQualityAdapter.notifyDataSetChanged()
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        airQualityViewModel.disconnectWebSocket()
    }
}
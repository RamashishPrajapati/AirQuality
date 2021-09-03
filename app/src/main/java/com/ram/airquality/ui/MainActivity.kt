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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val airQualityUri = URI(Constants.AIR_QUALITY_WEB_SOCKET_URL)
        airQualityViewModel = ViewModelProvider(this).get(AirQualityViewModel::class.java)
        airQualityViewModel.connectToWebSocket(airQualityUri)
        initRecyclerview()
        setUpObserver()

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
        airQualityViewModel.getAirQualityData().observe(this, {
            it.let {
                airQualityAdapter.submitList(it)
                airQualityAdapter.notifyDataSetChanged()
                /*Replace the notifyDataSetChanged to notifyItemRangeChanged, figure out how*/
            }
        })
    }

    override fun onResume() {
        super.onResume()
        airQualityViewModel.getAirQualityData()

    }

    override fun onPause() {
        super.onPause()
        airQualityViewModel.disconnectWebSocket()
    }
}
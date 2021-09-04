package com.ram.airquality.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ram.airquality.R
import com.ram.airquality.databinding.ItemCityairBinding
import com.ram.airquality.model.AirQualityModelItem
import com.ram.airquality.utility.convertDurationToFormatted
import kotlinx.android.synthetic.main.item_cityair.view.*
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by Ramashish Prajapati on 26,August,2021
 */

class AirQualityAdapter :
    ListAdapter<AirQualityModelItem, AirQualityAdapter.ItemViewholder>(CityAirDiffDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ItemCityairBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    class ItemViewholder(binding: ItemCityairBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AirQualityModelItem) = with(itemView) {
            itemView.tvCityName.text = item.city
            itemView.tvAirQuality.text = roundOffDecimal(item.aqi!!).toString()
            itemView.tvTime.text =
                convertDurationToFormatted(item.timing!!, System.currentTimeMillis(), resources)

            /*To change the background color of container*/
            when (roundOffDecimal(item.aqi)) {
                in 0..50 -> {
                    itemView.container.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.good))
                }
                in 51..100 -> {
                    itemView.container.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.satisfactory))
                }
                in 101..200 -> {
                    itemView.container.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.moderate))
                }
                in 201..300 -> {
                    itemView.container.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.poor))
                }
                in 301..400 -> {
                    itemView.container.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.verypoor))
                }
                in 401..500 -> {
                    itemView.container.setBackgroundColor(ContextCompat.getColor(itemView.context,
                        R.color.sever))
                }


            }
        }
    }
}

fun roundOffDecimal(number: Double): Int? {
    val decimal = BigDecimal(number).setScale(2, RoundingMode.CEILING)
    return decimal.toInt()
}

/*Check the logic behind DiffUtil*/
class CityAirDiffDiffCallback : DiffUtil.ItemCallback<AirQualityModelItem>() {
    override fun areItemsTheSame(
        oldItem: AirQualityModelItem,
        newItem: AirQualityModelItem,
    ): Boolean {
        return oldItem.aqi == newItem.aqi
    }

    override fun areContentsTheSame(
        oldItem: AirQualityModelItem,
        newItem: AirQualityModelItem,
    ): Boolean {
        return oldItem.aqi == newItem.aqi
    }

}
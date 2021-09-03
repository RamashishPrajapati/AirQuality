package com.ram.airquality.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ram.airquality.databinding.ItemCityairBinding
import com.ram.airquality.model.AirQualityModelItem
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
            itemView.tvTime.text = item.timing
        }
    }
}

fun roundOffDecimal(number: Double): Double? {
    val decimal = BigDecimal(number).setScale(2, RoundingMode.CEILING)
    return decimal.toDouble()
}

/*Check the logic behind DiffUtil*/
class CityAirDiffDiffCallback : DiffUtil.ItemCallback<AirQualityModelItem>() {
    override fun areItemsTheSame(
        oldItem: AirQualityModelItem,
        newItem: AirQualityModelItem
    ): Boolean {
        return oldItem.city == newItem.city
    }

    override fun areContentsTheSame(
        oldItem: AirQualityModelItem,
        newItem: AirQualityModelItem
    ): Boolean {
        return oldItem.city == newItem.city
    }

    override fun getChangePayload(
        oldItem: AirQualityModelItem,
        newItem: AirQualityModelItem
    ): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}
package com.example.kotlinweathergr1919.view_viewmodel.historylist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweathergr1919.databinding.FragmentHistoryWeatherListRecyclerItemBinding
import com.example.kotlinweathergr1919.facade.entities.Weather
import com.example.kotlinweathergr1919.utils.SVG
import com.example.kotlinweathergr1919.utils.WEATHER_ICON
import com.example.kotlinweathergr1919.utils.loadSvg

class HistoryWeatherListAdapter(
    private var data: List<Weather> = listOf()
) : RecyclerView.Adapter<HistoryWeatherListAdapter.HistoryHolder>() {

    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val binding = FragmentHistoryWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return HistoryHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SetTextI18n")
        fun bind(weather: Weather) {
            FragmentHistoryWeatherListRecyclerItemBinding.bind(itemView).apply {
                tvCityName.text = weather.city.name
                tvTemperature.text = weather.temperature.toString()
                tvFeelsLike.text = weather.feelsLike.toString()
                tvLatLon.text = "${tvLatLon.text}" +
                        " ${String.format("%.5f", weather.city.lat)}," +
                        " ${String.format("%.5f", weather.city.lon)}"

                tvIcon.loadSvg("$WEATHER_ICON${weather.icon}$SVG")
            }
        }
    }
}
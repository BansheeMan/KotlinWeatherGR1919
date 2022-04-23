package com.example.kotlinweathergr1919.view.weatherlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinweathergr1919.databinding.FragmentWeatherListRecyclerItemBinding
import com.example.kotlinweathergr1919.repository.entities.Weather

class WeatherListAdapter(
    private val onItemListClickListener: OnItemListClickListener,
    private var data: List<Weather> = listOf()
) : RecyclerView.Adapter<WeatherListAdapter.CityHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun setData(dataNew: List<Weather>) {
        this.data = dataNew
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val binding = FragmentWeatherListRecyclerItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CityHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: Weather) {
            FragmentWeatherListRecyclerItemBinding.bind(itemView).apply {
                tvCityName.text = weather.city.name
                root.setOnClickListener {
                    onItemListClickListener.onItemClick(weather)
                }
            }
        }
    }
}

package com.example.kotlinweathergr1919.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentDetailsBinding
import com.example.kotlinweathergr1919.repository.OnServersResponse
import com.example.kotlinweathergr1919.repository.entities.Weather
import com.example.kotlinweathergr1919.repository.WeatherLoader
import com.example.kotlinweathergr1919.repository.entitiesDTO.WeatherDTO
import com.example.kotlinweathergr1919.utils.KEY_BUNDLE_WEATHER

class DetailsFragment : Fragment(), OnServersResponse {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var currentCityName: String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getParcelable<Weather>(KEY_BUNDLE_WEATHER)?.let {
            currentCityName = it.city.name
            WeatherLoader(this).loadWeather(it.city.lat, it.city.lon)
        }
    }

    private fun renderData(weatherDTO: WeatherDTO) {
        with(binding) {
            progressBar.visibility = View.GONE
            cityName.text = currentCityName
            temperatureValue.text = weatherDTO.fact.temperature.toString()
            feelsLikeValue.text = weatherDTO.fact.feelsLike.toString()
            cityCoordinates.text = String.format(
                getString(R.string.city_coordinates),
                weatherDTO.info.lat.toString(),
                weatherDTO.info.lon.toString()
            )
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onResponse(weatherDTO: WeatherDTO) {
        renderData(weatherDTO)
    }
}
package com.example.kotlinweathergr1919.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinweathergr1919.R
import com.example.kotlinweathergr1919.databinding.FragmentMainBinding
import com.example.kotlinweathergr1919.repository.Weather
import com.example.kotlinweathergr1919.viewmodel.AppState
import com.example.kotlinweathergr1919.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val observer = Observer<AppState> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    private fun renderData(appState: AppState) = with(binding) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                progressBar.visibility = View.GONE
                weatherGroup.visibility = View.VISIBLE
                setData(weatherData)
            }
            is AppState.Loading -> progressBar.visibility = View.VISIBLE
            is AppState.Error -> {
                progressBar.visibility = View.GONE
                cityName.visibility = View.VISIBLE
                cityName.text = appState.error.message
                Snackbar
                    .make(mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeather() }
                    .show()
            }
        }

    }

    private fun setData(weatherData: Weather) = with(binding) {
        cityName.text = weatherData.city.name
        temperatureValue.text = weatherData.temperature.toString()
        feelsLikeValue.text = weatherData.feelsLike.toString()
        cityCoordinates.text = String.format(
            getString(R.string.city_coordinates),
            weatherData.city.lat.toString(),
            weatherData.city.lon.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}